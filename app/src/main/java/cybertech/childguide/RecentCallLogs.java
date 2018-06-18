package cybertech.childguide;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RecentCallLogs extends AppCompatActivity {

    ListView baseView;
    List<CallModel> list;
    String childId;
    SharedPreferences sp;
    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Thread", "onCreate : ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_call_logs);
        baseView = (ListView) findViewById(R.id.callBaseListView);
        baseView.deferNotifyDataSetChanged();
        list = null;
        baseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecentCallLogs.this);
                builder.setTitle("Call History: " + list
                        .get(position).getCallName());
                builder.setMessage("Name: " + list.get(position).getCallName() +
                        "\nPhone No: " + list.get(position).getCallNo() +
                        "\nType: " + list.get(position).getCallType() +
                        "\nDuration: " + list.get(position).getCallDuration() + " Seconds" +
                        "\nTime: " + list.get(position).getCallDate());
                builder.setNegativeButton("Ok", null);
                builder.setCancelable(true);
                builder.show();
            }
        });
        sp = PreferenceManager.getDefaultSharedPreferences(RecentCallLogs.this);
        childId = sp.getString("childid", null);
        new callLogs().execute();
        Log.i("Thread", "out of onCreate ");
    }

    public class callLogs extends AsyncTask<Void, Void, Void> {
        List<String> namelist = new ArrayList<>();
        List<String> nolist = new ArrayList<>();
        List<String> datelist = new ArrayList<>();
        List<String> typelist = new ArrayList<>();
        List<String> durationlist = new ArrayList<>();
        private CallAdapter adapter;

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Thread", "Background");
            list = new ArrayList<>();
            final Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.i("Thread", "response : " + response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONArray logs = jsonObject.getJSONArray("callLogs");
                            for (int i = 0; i < logs.length(); i++) {
                                JSONObject object = logs.getJSONObject(i);
                                String calllogs = object.getString("name");
                                String[] callArray = calllogs.split(";");
                                //Log.i("Thread", "split array length : " + callArray.length);
                                for (String call : callArray) {
                                    if (call.startsWith("name")) {
                                        String name = call.substring(4);
                                        namelist.add(name);
                                    } else if (call.startsWith("no")) {
                                        String number = call.substring(2);
                                        nolist.add(number);
                                    } else if (call.startsWith("date")) {
                                        String date = call.substring(4);
                                        datelist.add(date);
                                    } else if (call.startsWith("type")) {
                                        String type = call.substring(4);
                                        typelist.add(type);
                                    } else if (call.startsWith("duration")) {
                                        String duration = call.substring(8);
                                        durationlist.add(duration);
                                    }
                                }
                                for (int j = 0; j < namelist.size(); j++) {
                                    list.add(new CallModel(namelist.get(j), nolist.get(j), datelist.get(j), typelist.get(j), durationlist.get(j)));
                                }
                                Log.i("Thread", "Call logs list size : " + list.size());
                            }
                            if (list.size() > 0) {

                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RecentCallLogs.this);
                                builder.setTitle("Monitor Alert");
                                builder.setMessage("There is no call record from your child");
                                builder.setCancelable(false);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(RecentCallLogs.this, ParentHome.class));
                                    }
                                });
                                builder.show();
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        } else {
                            DialogInterface.OnClickListener lisener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(RecentCallLogs.this, ParentHome.class));
                                }
                            };
                            MyDialog.dialogOK(RecentCallLogs.this, jsonObject.getString("message"), lisener);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            int duration = 0;
            ParentConnection connection = new ParentConnection(childId, duration, listener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(connection);
            adapter = new CallAdapter(RecentCallLogs.this, list);
            Log.i("Thread", "list size : " + list.size());

            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Thread", "preExecut");
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RecentCallLogs.this, null, "loading calllogs please wait...",true,true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("Thread", "PostExecute");
            super.onPostExecute(aVoid);
            baseView.setAdapter(adapter);
        }

    }


}

