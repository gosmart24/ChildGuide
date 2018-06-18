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

public class KeyStrokes extends AppCompatActivity {

    ListView baseview;
    List<KeyLoggerModel> baseList;
    String childID;
    SharedPreferences sp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_strokes);
        baseview = (ListView) findViewById(R.id.keyLoggerBaseView);
        baseList = null;
        baseview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KeyStrokes.this);
                builder.setTitle("Event Type: " + baseList.get(position).getEventType());
                builder.setCancelable(true);
                builder.setNegativeButton("OK", null);
                builder.setMessage(baseList.get(position).getEventBody());
                builder.show();
            }
        });
        sp = PreferenceManager.getDefaultSharedPreferences(KeyStrokes.this);
        childID = sp.getString("childid", null);
        new loadKeyEvents().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();

    }

    private class loadKeyEvents extends AsyncTask<Void, Void, Void> {
        KeyLoggerAdapter adapter;
        String body;
        ArrayList smsData = new ArrayList();
        ArrayList date = new ArrayList();
        ArrayList searchData = new ArrayList();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(KeyStrokes.this, null, "loading please wait...", true, true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            baseview.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... params) {
            baseList = new ArrayList<>();

            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Thread", "Responds : " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONArray jsonArray = jsonObject.getJSONArray("keyevents");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject keyEvent = jsonArray.getJSONObject(i);
                                body = keyEvent.getString("body");
                            }

                            String[] bodyArray = body.split("______");
                            for (String item : bodyArray) {
                                if (item.startsWith("sms") || item.startsWith("search")) {
                                    smsData.add(item);
                                } else {
                                    date.add(item);
                                }

                            }
                            Log.i("Thread", "SMS DATA SIZE : " + smsData.size());
                            Log.i("Thread", "Search DATA SIZE : " + searchData.size());
                            Log.i("Thread", "Date DATA SIZE : " + date.size());

                            for (int i = 0; i < smsData.size(); i++) {
                                if (smsData.get(i).toString().startsWith("sms")) {
                                    if (smsData.get(i).toString().startsWith("sms_inbox")) {
                                        String body = smsData.get(i).toString().substring(9);
                                        baseList.add(new KeyLoggerModel("Text", date.get(i).toString(), body));
                                    } else if (smsData.get(i).toString().startsWith("sms_sent")) {
                                        String body = smsData.get(i).toString().substring(8);
                                        baseList.add(new KeyLoggerModel("Text", date.get(i).toString(), body));
                                    } else if (smsData.get(i).toString().startsWith("sms_draft")) {
                                        String body = smsData.get(i).toString().substring(9);
                                        baseList.add(new KeyLoggerModel("Text", date.get(i).toString(), body));
                                    }
                                } else {
                                    baseList.add(new KeyLoggerModel("Search", date.get(i).toString(), smsData.get(i).toString()));

                                }

                            }
                            if (baseList.size() > 0) {

                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(KeyStrokes.this);
                                builder.setTitle("Monitor Alert");
                                builder.setMessage("There is no key event record from your child");
                                builder.setCancelable(false);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(KeyStrokes.this, ParentHome.class));
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
                                    startActivity(new Intent(KeyStrokes.this, ParentHome.class));
                                }
                            };
                            MyDialog.dialogOK(KeyStrokes.this, jsonObject.getString("message"), lisener);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Log.i("Thread", "creating connection");
            ParentConnection connection = new ParentConnection(childID, false, listener);
            RequestQueue queue = Volley.newRequestQueue(KeyStrokes.this);
            queue.add(connection);
            Log.i("Thread", "adding to adapter ");
            adapter = new KeyLoggerAdapter(getApplicationContext(), baseList);
            return null;
        }
    }

}
