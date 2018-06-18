package cybertech.childguide;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateService extends Service {

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    TinyDB tinyDB = new TinyDB(UpdateService.this);
    private String childID = sp.getString("childid", "");
    private String myKEYwords = sp.getString("keywords", "");
    private IBinder mBinder = new MyBinder();
    private String[] wordlist;
    boolean isCalllogsAvailable = false;
    boolean isSMSAvailable = false;
    boolean isBrowserHistorysAvailable = false;
   // boolean isCalllogsAvailable = false;

    public UpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

      //  if ()
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // Monitor Words ...............................................................................
    public void sendMonitorWords(String body, String number, String smsDate, String myKEYwords) {
        StringBuilder builder = new StringBuilder();
        Response.Listener<String> litener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Thread", "respond: " + response);
            }
        };
        builder.append("sms_sent" + body + "\n To: " + number + "______smsdate" + smsDate + "______");
        InstalledAppsConnection conncet = new InstalledAppsConnection(childID, builder, myKEYwords, litener);
        RequestQueue request = Volley.newRequestQueue(UpdateService.this);
        request.add(conncet);
    }

    public boolean isMonitorWordFound(String body, String[] wordlist) {
        for (String item : wordlist) {
            if (body.contains(item)) {
                return true;
            }
        }
        return false;
    }

    // Send newly installed apps
    public void sendInstalledApps() {
      String appNames =  tinyDB.getString("newApps");
        Log.i("Thread", "geting installed apps...");
        Response.Listener<String> listenerInstall = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.i("Thread", "installed apps Response : " + response);
            }
        };
        Log.i("Thread", "starting connection...");
        InstalledAppsConnection connection = new InstalledAppsConnection(childID, appNames, listenerInstall);
        RequestQueue queue = Volley.newRequestQueue(UpdateService.this);
        queue.add(connection);
    }

    // Send SMS newly received
    public void sendSMSOrSearchWord(String body, String number, String smsDate) {
        Log.i("Thread", "Event thread started...");
        StringBuilder smsBuffer = new StringBuilder();
        Response.Listener<String> litener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Thread", "uploaded successfully!");
            }
        };
        smsBuffer.append("sms_inbox" + body + "-----" + number + "______" + smsDate + "______");
        ChildConnection con = new ChildConnection(childID, litener, smsBuffer);
        RequestQueue queue = Volley.newRequestQueue(UpdateService.this);
        queue.add(con);
    }

    // Send BrowserHistories.
    public void sendBrowserHistories(String title, String siteDate, String siteURL) {
        StringBuilder builder = new StringBuilder();
        builder.append("title" + title + "______date" + siteDate + "______url" + siteURL + "______");
        Response.Listener<String> listenerBrowser = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.i("Thread", "Response : " + response);
            }
        };
        // Log.i("Thread", "starting connection ");
        ChildConnection connection = new ChildConnection(childID, "", builder.toString(), listenerBrowser);
        RequestQueue request = Volley.newRequestQueue(UpdateService.this);
        request.add(connection);
    }

    // Send newly call records received.
    public void sendCallLogs(String callName, String phnumber, String dateCall, String callTypeStr, String callduration) {
        StringBuilder callBuilder = new StringBuilder();
        callBuilder.append("name" + callName + ";no" + phnumber + ";date" + dateCall + ";type" + callTypeStr + ";duration" + callduration + ";");
        ChildConnection connect;
        RequestQueue request = Volley.newRequestQueue(UpdateService.this);
        Response.Listener<String> listen = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Thread", "call logs finished uploading successfully!");
            }
        };
        connect = new ChildConnection(childID, callBuilder, listen);
        request.add(connect);
    }

    // Send recent Location.
    private void sendLocation(String latitude, String longitude) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a").format(new Date());
        Response.Listener<String> listen = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.i("Thread", "Location send successfully!");
            }
        };
        ChildConnection connection = new ChildConnection(childID, latitude, longitude, listen, date);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(connection);
    }

    public class MyBinder extends Binder {
        UpdateService getService() {
            return UpdateService.this;
        }
    }
}
