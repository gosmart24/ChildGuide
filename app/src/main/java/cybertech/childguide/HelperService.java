package cybertech.childguide;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Browser;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelperService extends Service {
    String myKEYwords;  // sp.getString("keywords", "abotion,pregnancy,pregnant,xxx,XXX,sex,pornography")
    String[] wordlist;
    private String callName;
    private String dateCall;
    private String callTypeStr;
    private String phnumber;
    private String callduration;
    private boolean isLocationComplete = false;
    private boolean isCallogComplete = false;
    private boolean isBrowserComplete = false;
    private boolean isMonitorComplete = false;
    private boolean isInstallappComlete = false;
    private boolean isKeyEventComplete = false;
    private String childID;
    Thread MonitorWords = new Thread(new Runnable() {

        private void sendMonitorWords() {
            List<String> list = new ArrayList<>();
            Log.i("Thread", "Event thread started...");
            StringBuilder builder = new StringBuilder();
            Log.i("Thread", "variable created...");
            Uri uri = Uri.parse("content://sms");
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            Log.i("Thread", "Cursor initialize...");
            Response.Listener<String> litener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    isMonitorComplete = true;
                    if (isInstallappComlete && isBrowserComplete && isCallogComplete && isKeyEventComplete && isLocationComplete) {
                        stopSelf();
                    }
                    Log.i("Thread", "respond: " + response);
                }
            };
            if (cursor != null && cursor.moveToFirst()) {
                Log.i("Thread", "loading SMS...");
                for (int i = 0; i < cursor.getCount(); i++) {
                    String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    Date smsDayTime = new Date(Long.valueOf(date));
                    String smsDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(smsDayTime);
                    String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    switch (Integer.parseInt(type)) {
                        case 1:
                            for (String item : wordlist) {
                                if (body.contains(item)) {
                                    builder.append("sms_inbox" + body + "\n From: " + number + "______smsdate" + smsDate + "______");
                                }
                            }
                            break;

                        case 2:
                            for (String item : wordlist) {
                                if (body.contains(item)) {
                                    builder.append("sms_sent" + body + "\n To: " + number + "______smsdate" + smsDate + "______");
                                }
                            }
                            break;

                        case 3:
                            for (String item : wordlist) {
                                if (body.contains(item)) {
                                    builder.append("sms_draft" + body + "\n For: " + number + "______smsdate" + smsDate + "______");
                                }
                            }
                            break;
                    }
                    cursor.moveToNext();
                }
                // Search part.
                String order = Browser.SearchColumns.DATE + " DESC";
                Cursor mCur = getContentResolver().query(Browser.SEARCHES_URI, Browser.SEARCHES_PROJECTION, null, null, order);
                if (mCur != null) {
                    mCur.moveToFirst();
                    while (!mCur.isAfterLast()) {
                        String search = mCur.getString(Browser.SEARCHES_PROJECTION_SEARCH_INDEX);
                        Date date = new Date(mCur.getLong(Browser.SEARCHES_PROJECTION_DATE_INDEX));
                        String siteDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(date);
                        // adding to builder.
                        for (String item : wordlist) {
                            if (search.contains(item)) {
                                builder.append("search:" + search + "______sitedate" + siteDate + "______");
                            }
                        }
                        mCur.moveToNext();
                    }

                }
                if (mCur != null) {
                    mCur.close();
                }
            }
            Log.i("Thread", "Monitor words size: " + builder.length());
            if (cursor != null) {
                cursor.close();
            }
            InstalledAppsConnection conncet = new InstalledAppsConnection(childID, builder, myKEYwords, litener);
            RequestQueue request = Volley.newRequestQueue(HelperService.this);
            request.add(conncet);
        }

        @Override
        public void run() {
            sendMonitorWords();
        }
    });
    Thread InstalledAppThread = new Thread(new Runnable() {
        // separating system apps from installed apps
        private boolean isSystemPackage(PackageInfo pkgInfo) {
            return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
        }

        private void sendInstalledApps() {
            Log.i("Thread", "geting installed apps...");
            StringBuilder builder = new StringBuilder();
            List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);
                if ((isSystemPackage(p) == false)) {
                    String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                    builder.append(appName + "______");
                }
            }
            Log.i("Thread", "installeapps  buffer size : " + builder.length());
            Response.Listener<String> listenerInstall = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    isInstallappComlete = true;
                    if (isMonitorComplete && isBrowserComplete && isCallogComplete && isKeyEventComplete && isLocationComplete) {
                        stopSelf();
                    }
                    // Log.i("Thread", "installed apps Response : " + response);
                }
            };
            Log.i("Thread", "starting connection...");
            InstalledAppsConnection connection = new InstalledAppsConnection(childID, builder.toString(), listenerInstall);
            RequestQueue queue = Volley.newRequestQueue(HelperService.this);
            queue.add(connection);
        }


        @Override
        public void run() {
            Log.i("Thread", "InstalledApp thread has started");
            sendInstalledApps();
        }
    });
    Thread EventThread = new Thread(new Runnable() {
        // SMS MESSAGES..///////////////////////////////////////////////////////////////////////////////
        private void sendEvents() {
            Log.i("Thread", "Event thread started...");
            List<KeyLoggerModel> list = new ArrayList<>();
            StringBuilder smsBuffer = new StringBuilder();
            smsBuffer.append("");
            Uri uri = Uri.parse("content://sms");
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            Response.Listener<String> litener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Thread", "uploaded successfully!");
                    isKeyEventComplete = true;
                    if (isMonitorComplete && isBrowserComplete && isCallogComplete && isInstallappComlete && isLocationComplete) {
                        stopSelf();
                    }
                }
            };
            if (cursor != null && cursor.moveToFirst()) {
                Log.i("Thread", "loading SMS...");
                for (int i = 0; i < cursor.getCount(); i++) {
                    String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    Date smsDayTime = new Date(Long.valueOf(date));
                    String smsDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(smsDayTime);
                    String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    switch (Integer.parseInt(type)) {
                        case 1:
                            smsBuffer.append("sms_inbox" + body + "-----" + number + "______" + smsDate + "______");
                            break;

                        case 2:
                            smsBuffer.append("sms_sent" + body + "-----" + number + "______" + smsDate + "______");
                            break;

                        case 3:
                            smsBuffer.append("sms_draft" + body + "-----" + number + "______" + smsDate + "______");
                            break;
                    }
                    list.add(new KeyLoggerModel("Text", smsDayTime, smsBuffer.toString()));
                    cursor.moveToNext();
                }
                Log.i("Thread", "smsBuffer size: " + smsBuffer.length());
                //Log.i("Thread", " SMS Event: " + smsBuffer.toString());
                String order = Browser.SearchColumns.DATE + " DESC";
                Cursor mCur = getContentResolver().query(Browser.SEARCHES_URI, Browser.SEARCHES_PROJECTION, null, null, order);
                if (mCur != null) {
                    mCur.moveToFirst();
                    while (!mCur.isAfterLast()) {
                        String search = mCur.getString(Browser.SEARCHES_PROJECTION_SEARCH_INDEX);
                        Date date = new Date(mCur.getLong(Browser.SEARCHES_PROJECTION_DATE_INDEX));
                        String siteDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(date);
                        // adding original list with for sending to server.
                        smsBuffer.append("search:" + search + "______" + siteDate + "______");
                        //list.add(new KeyLoggerModel("Search", siteDate, search));
                        mCur.moveToNext();
                    }
                }
                mCur.close();
            }
            cursor.close();
            Log.i("Thread", "Event connection started...");
            Log.i("Thread", "Event list size : " + list.size());
            ChildConnection con = new ChildConnection(childID, litener, smsBuffer);
            RequestQueue queue = Volley.newRequestQueue(HelperService.this);
            queue.add(con);
        }

        @Override
        public void run() {
            Log.i("Thread", " Events thread has started");
            sendEvents();
        }
    });
    Thread browserThread = new Thread(new Runnable() {
        StringBuilder builder = new StringBuilder();

        // Browser History ........DONE.............................................................
        private void sendBrowserHistories() {
            List<BrowserModel> list = new ArrayList<>();
            String order = Browser.BookmarkColumns.DATE + " DESC";
            Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, null, null, order);
            mCur.moveToFirst();

            while (mCur.isAfterLast() == false) {

                String siteURL = mCur.getString(Browser.HISTORY_PROJECTION_URL_INDEX);
                String title = mCur.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX);
                Date date = new Date(mCur.getLong(Browser.HISTORY_PROJECTION_DATE_INDEX));

                String siteDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date);
                builder.append("title" + title + "______date" + siteDate + "______url" + siteURL + "______");
                list.add(new BrowserModel(title, siteURL, siteDate));
                mCur.moveToNext();
            }
            Log.i("Thread", "list size : " + list.size());
            mCur.close();

            Response.Listener<String> listenerBrowser = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Log.i("Thread", "Response : " + response);
                    isBrowserComplete = true;
                    if (isMonitorComplete && isInstallappComlete && isCallogComplete && isKeyEventComplete && isLocationComplete) {
                        stopSelf();
                    }
                }
            };
            // Log.i("Thread", "starting connection ");
            ChildConnection connection = new ChildConnection(childID, "", builder.toString(), listenerBrowser);
            RequestQueue request = Volley.newRequestQueue(HelperService.this);
            request.add(connection);


        }

        @Override
        public void run() {
            Log.i("Thread", "BrowserHistory thread has started");
            sendBrowserHistories();
        }
    });
    Thread CallLogsThread = new Thread(new Runnable() {
        // finished building thread.......................................................
        private void sendCallLogs() {
            // Call Logs........................DONE........................................................
            List<CallModel> list = new ArrayList<>();
            Log.i("Thread", "call logs starting");
            String sortcalls = CallLog.Calls.DATE + " DESC";
            Cursor mCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, sortcalls);
            int number = mCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int date = mCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = mCursor.getColumnIndex(CallLog.Calls.DURATION);
            int type = mCursor.getColumnIndex(CallLog.Calls.TYPE);
            int name = mCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            // Log.i("Thread", "call logs getting");
            StringBuilder callBuilder = new StringBuilder();
            while (mCursor.moveToNext()) {
                phnumber = mCursor.getString(number);
                callduration = mCursor.getString(duration);
                String calltype = mCursor.getString(type);
                String calldate = mCursor.getString(date);
                callName = (mCursor.getString(name) != null) ? mCursor.getString(name) : "No Name";
                Date d = new Date(Long.valueOf(calldate));
                dateCall = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(d);
                callTypeStr = "";
                switch (Integer.parseInt(calltype)) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "Outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "Received";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "Missed";
                        break;
                }
                // Log.i("Thread", "call logs adding to list");
                list.add(new CallModel(callName, phnumber, dateCall, callTypeStr, callduration));
                callBuilder.append("name" + callName + ";no" + phnumber + ";date" + dateCall + ";type" + callTypeStr + ";duration" + callduration + ";");
            }

            //Log.i("Thread", "call logs connection started");
            //Log.i("Thread", "call logs:" + callBuilder.toString());
            ChildConnection connect;
            RequestQueue request = Volley.newRequestQueue(HelperService.this);
            Response.Listener<String> listen = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Log.i("Thread", "response: " + response);
                    Log.i("Thread", "call logs finished uploading successfully!");
                    isCallogComplete = true;
                    if (isMonitorComplete && isBrowserComplete && isCallogComplete && isKeyEventComplete && isLocationComplete) {
                        stopSelf();
                    }
                }
            };
            connect = new ChildConnection(childID, callBuilder, listen);
            request.add(connect);
            // Log.i("Thread", "call logs connection finished upload");
        }

        @Override
        public void run() {
            Log.i("Thread", "CallLogs thread has started");
            sendCallLogs();
        }
    });

    public HelperService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        childID = intent.getStringExtra("childid");
        myKEYwords = intent.getStringExtra("keywords");
        wordlist = myKEYwords.split(",");
        Toast.makeText(this, "Helper service has successfully started", Toast.LENGTH_LONG).show();

        InstalledAppThread.start();
        EventThread.start();
        CallLogsThread.start();
        browserThread.start();
        MonitorWords.start();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;

    }

}