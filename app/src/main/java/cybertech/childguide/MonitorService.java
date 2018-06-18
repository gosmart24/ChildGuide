package cybertech.childguide;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitorService extends IntentService {

    TinyDB tinyDB;
    private String[] wordlist;
    private String childID;
    private String myKEYwords;

    public MonitorService() {
        super("MonitorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tinyDB = new TinyDB(MonitorService.this);
        childID = tinyDB.getString("childid");
        myKEYwords = tinyDB.getString("keywords");
        wordlist = myKEYwords.split(",");
        List<String> list = new ArrayList<>();
        Log.i("Thread", "Event thread started...");
        Log.i("Thread", "Event thread  Childid : " + childID);
        StringBuilder builder = new StringBuilder();
        Log.i("Thread", "variable created...");
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Log.i("Thread", "Cursor initialize...");
        Response.Listener<String> litener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Thread", "Monitor Words respond: Successfully Uploaded ");
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
        RequestQueue request = Volley.newRequestQueue(MonitorService.this);
        request.add(conncet);
    }

}
