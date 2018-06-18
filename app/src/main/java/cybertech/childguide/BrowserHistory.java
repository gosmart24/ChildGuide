package cybertech.childguide;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrowserHistory extends AppCompatActivity {

    List<BrowserModel> listStorage;
    ListView base;
    String childID;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_history);
        base = (ListView) findViewById(R.id.browserBaseView);
        listStorage = null;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        childID = sp.getString("childid", null);
        base.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBrowser(listStorage.get(position).getSearchSite());
            }
        });
        new TestingData().execute();
    }

    private void openBrowser(final String URL) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BrowserHistory.this);
        builder.setCancelable(true);
        builder.setTitle("Browser Alert");
        builder.setMessage("Do you want to visit the site?");
        builder.setNegativeButton("No", null);
        DialogInterface.OnClickListener onOpen = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(URL);
                Intent intent = new Intent();
                intent.setData(uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        builder.setPositiveButton("Yes", onOpen);
        builder.show();
    }

    public class TestingData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progress;
        ArrayList titlelist = new ArrayList();
        ArrayList datelist = new ArrayList();
        ArrayList siteURLlist = new ArrayList();
        private BrowserAdapter adapter;

        @Override
        protected Void doInBackground(Void... params) {
            listStorage = new ArrayList<>();
            long browser = 01222;
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Browser", "Response : " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONArray jsonArray = jsonObject.getJSONArray("browserhistory");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobject = jsonArray.getJSONObject(i);
                                String object = jobject.getString("site");
                                String histories[] = object.split("______");
                                for (String item : histories) {
                                    if (item.startsWith("title")) {
                                        String title = item.substring(5);
                                        titlelist.add(title);
                                    } else if (item.startsWith("date")) {
                                        String date = item.substring(4);
                                        datelist.add(date);
                                    } else if (item.startsWith("url")) {
                                        String url = item.substring(3);
                                        siteURLlist.add(url);
                                    }
                                }
                            }
                            Log.i("Browser", "title size : " + titlelist.size());

                            for (int i = 0; i < titlelist.size(); i++) {
                                listStorage.add(new BrowserModel(titlelist.get(i).toString(), siteURLlist.get(i).toString(), datelist.get(i).toString()));
                            }
                            if (listStorage.size() > 0) {
                                adapter.notifyDataSetChanged();
                                progress.dismiss();
                            } else {
                                progress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(BrowserHistory.this);
                                builder.setTitle("Monitor Alert");
                                builder.setMessage("There is no Browser histories from your child");
                                builder.setCancelable(false);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(BrowserHistory.this, ParentHome.class));
                                    }
                                });
                                builder.show();
                            }

                        } else {
                            progress.dismiss();
                            MyDialog.dialogOK(BrowserHistory.this, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ParentConnection connection = new ParentConnection(childID, browser, listener);
            RequestQueue queue = Volley.newRequestQueue(BrowserHistory.this);
            queue.add(connection);
            adapter = new BrowserAdapter(getApplicationContext(), listStorage);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(BrowserHistory.this, null, "loading please wait...",true,true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            base.setAdapter(adapter);
        }
    }

}

