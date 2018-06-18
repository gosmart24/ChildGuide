package cybertech.childguide;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstalledApps extends AppCompatActivity {

    ListView baseList;
    List<AppList> installedApps;
    InstalledAppAdapter installedAppAdapter;
    String childID;
    SharedPreferences sp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_apps);
        baseList = (ListView) findViewById(R.id.baseListView);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        childID = sp.getString("childid", null);
        installedApps = null;
        new installApplicaons().execute();
    }

    // separating system apps from installed apps
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();
    }

    //displaying installed apps in parent list in background.
    public class installApplicaons extends AsyncTask<Void, Void, Void> {
        String[] appNames;

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Thread", "");
            installedApps = new ArrayList<>();
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Thread", "Response : " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("success");
                        if (status) {
                            JSONArray jsonArray = jsonObject.getJSONArray("installedapps");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String appname = object.getString("appname");
                                appNames = appname.split("______");
                                //  installedApps.add(new AppList(object.getString("appname")));
                            }

                            for (String item : appNames) {
                                installedApps.add(new AppList(item));
                            }

                            if (installedApps.size() > 0) {
                                installedAppAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(InstalledApps.this);
                                builder.setTitle("Monitor Alert");
                                builder.setMessage("There is no installed apps record from your child");
                                builder.setCancelable(false);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(InstalledApps.this, ParentHome.class));
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            String msg = jsonObject.getString("message");
                            MyDialog.dialogOK(getApplicationContext(), msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Log.i("Thread", "starting connection");

            InstalledAppsConnection connection = new InstalledAppsConnection(childID, listener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(connection);
            installedAppAdapter = new InstalledAppAdapter(InstalledApps.this, installedApps);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Thread", "OnPreExecute");
            progressDialog = ProgressDialog.show(InstalledApps.this, null, "loading installed applications...",true,true);
        }

        @Override
        protected void onPostExecute(Void appLists) {
            super.onPostExecute(appLists);
            Log.i("Thread", "OnPostExecute");
            baseList.setAdapter(installedAppAdapter);
        }
    }

}
