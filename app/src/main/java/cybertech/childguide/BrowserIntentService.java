package cybertech.childguide;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Browser;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BrowserIntentService extends IntentService {

    TinyDB tinyDB;
    private String childID;
    private StringBuffer builder;

    public BrowserIntentService() {
        super("BrowserIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        tinyDB = new TinyDB(BrowserIntentService.this);
        childID = tinyDB.getString("childid");
        Log.i("Thread", "Browser  Childid : " + childID);
        builder = new StringBuffer();
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
                Log.i("Thread", "Browser Response : Successfully uploaded");
            }
        };
        // Log.i("Thread", "starting connection ");
        ChildConnection connection = new ChildConnection(childID, "", builder.toString(), listenerBrowser);
        RequestQueue request = Volley.newRequestQueue(BrowserIntentService.this);
        request.add(connection);
    }
}
