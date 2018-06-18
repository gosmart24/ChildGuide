package cybertech.childguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class RecentLocations extends AppCompatActivity {

    public static List<LocationModel> list;
    static ListView baseList;
    private static String childid;
    SharedPreferences sp;
    ProgressDialog progress;
    private String limit;
    private LocationAdapter locationAdapter;

    public static List<LocationModel> getList() {
        return list;
    }

    // Location Alert Dialog for showing child location when the list is selected.
    public void showLocation(final Context context, String latitude, String longitude, String Date, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogInterface.OnClickListener onYes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(intent);
            }
        };
        builder.setPositiveButton("Yes", onYes);
        builder.setNegativeButton("NO", null);
        builder.setCancelable(false);
        builder.setTitle("Child Location");
        builder.setMessage("Your Child was Here : \n" + "Latitude : " + latitude + "\nLongitude : " + longitude + "\nTime : "
                + Date + "\n Do you want to view Recent Locations on map View?");
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progress.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_locations);
        baseList = (ListView) findViewById(R.id.baselist_location);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        childid = sp.getString("childid", null);
        limit = sp.getString("limit", "20");
        list = new ArrayList<>();
        baseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String latitude = list.get(position).getChildLatitude();
                String longitude = list.get(position).getChildLongitude();
                String date = list.get(position).getLocationDate();
                Intent mapIntent = new Intent(RecentLocations.this, MapsActivity.class);
                showLocation(RecentLocations.this, latitude, longitude, date, mapIntent);

            }
        });
        new loadLocations().execute();
    }

    public class loadLocations extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(RecentLocations.this, null, "loading please wait...",true,true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            baseList.setAdapter(locationAdapter);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONArray jsonArray = jsonObject.getJSONArray("locations");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject location = jsonArray.getJSONObject(i);
                                list.add(new LocationModel(location.getString("latitude")
                                        , location.getString("longitude")
                                        , location.getString("locationtime")));
                            }
                            if (list.size() > 0) {
                                locationAdapter.notifyDataSetChanged();
                                progress.dismiss();
                            } else {
                                progress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RecentLocations.this);
                                builder.setTitle("Monitor Alert");
                                builder.setMessage("There is no location record from your child");
                                builder.setCancelable(false);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(RecentLocations.this, ParentHome.class));
                                    }
                                });
                                builder.show();
                            }

                        } else {
                            MyDialog.dialogOK(RecentLocations.this, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ParentConnection connection = new ParentConnection(childid, limit, listener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(connection);
            locationAdapter = new LocationAdapter(RecentLocations.this, list);
            return null;
        }
    }

}