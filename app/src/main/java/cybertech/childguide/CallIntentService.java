package cybertech.childguide;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CallIntentService extends IntentService {

    TinyDB tinyDB;
    private String childID;
    private String phnumber;
    private String callduration;
    private String callName;
    private String dateCall;
    private String callTypeStr;

    public CallIntentService() {
        super("CallIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tinyDB = new TinyDB(CallIntentService.this);
        childID = tinyDB.getString("childid");
        // Call Logs........................DONE........................................................
        List<CallModel> list = new ArrayList<>();
        Log.i("Thread", "call logs starting");
        Log.i("Thread", "call logs Childid : " + childID);
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
        mCursor.close();
        Log.i("Thread", "call logs appended..");

        Log.i("Thread", "call logs connection started");
        Log.i("Thread", "call logs:" + callBuilder.toString());
        ChildConnection connect;
        RequestQueue request = Volley.newRequestQueue(CallIntentService.this);
        Response.Listener<String> listen = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.i("Thread", "response: " + response);
                Log.i("Thread", "call logs finished uploading successfully!");
            }
        };
        connect = new ChildConnection(childID, callBuilder, listen);
        request.add(connect);
        // Log.i("Thread", "call logs connection finished upload");
    }

}
