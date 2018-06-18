package cybertech.childguide;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Monitor extends AppCompatActivity {

    public static String childID;
    public static String keyWords;
    static TinyDB tinyDB;
    EditText ed_childName, ed_parentPhoneNO, ed_parentEmail, ed_locationInterval, ed_keyWords;
    TextView tv_ChildID, display;
    String childName, parentPhoneNO, parentEmail;
    int locationInterval;
    ProgressDialog progressDialog;
    private RequestQueue queue;
    private Location location;
    private boolean canGetLocation;
    private LocationListener listener;
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private Calendar calendar;
    private AlarmManager alarmManager;

    public static String generateID() {
        String numbers = "0123456789";
        String aTOz = "ABCDEFGHIJKLMNOPQRSTVWXYZ";
        int len = 8;
        // Using random method
        Random rndm_method = new SecureRandom();
        char[] otp = new char[len];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i > 5) {
                otp[i] = aTOz.charAt(rndm_method.nextInt(numbers.length()));
            } else if (i <= 0) {
                int rand = 0;
                while (rand <= 0) {
                    int temp = rndm_method.nextInt(numbers.length());
                    rand = (temp == 0) ? rndm_method.nextInt(numbers.length()) : temp;
                }
                otp[i] = numbers.charAt(rand);
            } else {
                otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
            }
            stringBuilder.append(otp[i]);
        }
        childID = stringBuilder.toString();
        return childID;
    }

    public static void NetworkAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        DialogInterface.OnClickListener nolistener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, MainActivity.class));
            }
        };
        builder.setNegativeButton("No", nolistener);
        DialogInterface.OnClickListener onyes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
            }
        };
        builder.setPositiveButton("Yes", onyes);
        builder.setTitle("Connectivity Alert");
        builder.setMessage("Network Connectivity is Required\n do you want to switch on to data connectivity? cost may apply");
        builder.show();

    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        return isConnected;
    }

    public static String getChildID() {
        return tinyDB.getString("childid");
    }

    public static String getKeyWords() {
        return tinyDB.getString("keywords");
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    //Getting current location of the child.
    public void getCurrentLocation() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                canGetLocation = true;
                if (location != null) {
                    setLocation(location);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.i("Thread", " Location: Lat: " + latitude + " long: " + longitude);
                }
                sendLocation();
                Log.i("Thread", "location : lat: " + latitude + " long: " + longitude);
                stopUsingGPS();
                enableGPS(false);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
            }
        } else {
            enableGPS(true);
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
            }
            // startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    public void enableGPS(boolean enable) {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", enable);
        sendBroadcast(intent);

    }

    private void sendLocation() {
        if (canGetLocation()) {
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a").format(new Date());
            Response.Listener<String> listen = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Thread", "Location send successfully!");
                }
            };
            ChildConnection connection = new ChildConnection(childID, String.valueOf(getLatitude()), String.valueOf(getLongitude()), listen, date);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(connection);
        } else {
            getCurrentLocation();
            Log.i("Thread", "Cannot get Location !");
        }

    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(listener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        tinyDB = new TinyDB(this);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 1);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ed_childName = (EditText) findViewById(R.id.et_childName);
        ed_parentPhoneNO = (EditText) findViewById(R.id.et_parentPhoneNO);
        ed_parentEmail = (EditText) findViewById(R.id.et_parentEMAIL);
        ed_locationInterval = (EditText) findViewById(R.id.et_locationInterval);
        ed_keyWords = (EditText) findViewById(R.id.et_keyWords);
        tv_ChildID = (TextView) findViewById(R.id.tv_childID);
        display = (TextView) findViewById(R.id.tv_display);
        generateID();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        tv_ChildID.setText(childID);
        if (!checkConnection(Monitor.this)) {
            NetworkAlert(Monitor.this);
        }
    }

    public void RegisterChild(View view) {
        if (!checkConnection(Monitor.this)) {
            NetworkAlert(Monitor.this);
        } else {
            childName = ed_childName.getText().toString();
            parentPhoneNO = ed_parentPhoneNO.getText().toString();
            parentEmail = ed_parentEmail.getText().toString();
            locationInterval = Integer.parseInt(ed_locationInterval.getText().toString());
            keyWords = ed_keyWords.getText().toString();
            locationInterval = 1000 * 60 * locationInterval;
            if (!childName.isEmpty() && !parentPhoneNO.isEmpty()) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                savePref(childID, childName, keyWords, parentPhoneNO, parentEmail, locationInterval, true);
                                DialogInterface.OnClickListener onsuccess = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Location Updates
//                                        Intent locationUpdates = new Intent(Monitor.this, GPSTracker.class);
//                                        locationUpdates.putExtra("childid", getChildID());
//                                        startService(locationUpdates);

                                        // logs Updates
                                        Intent monitorIntent = new Intent(Monitor.this, MonitorReceiver.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(Monitor.this, 100, monitorIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), locationInterval, pendingIntent);

                                        try {
                                            PackageManager pm = getPackageManager();
                                            pm.setComponentEnabledSetting(new ComponentName(Monitor.this, MainActivity.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                                        } catch (Exception e) {
                                            Log.i("Thread", "errors E : " + e.getMessage());
                                            e.printStackTrace();
                                        }
                                        finish();

                                    }
                                };
                                MyDialog.dialogOK(Monitor.this, "Registration is successful!.\n Your child monitoring id is: " + childID
                                        + "\nPlease keep this id for later use.\n Thank you", onsuccess);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                progressDialog = ProgressDialog.show(Monitor.this, null, "Creating profile...Please wait", true, true, null);
                ChildConnection connection = new ChildConnection(childID, childName, parentPhoneNO, parentEmail, listener);
                queue = Volley.newRequestQueue(Monitor.this);
                queue.add(connection);

            } else {
                display.setText(R.string.requiredfields);
            }
        }
    }

    public void savePref(String childID, String name, String keywords, String phoneNO, String email, int locationInterval, boolean monitor) {
        tinyDB.putString("childid", childID);
        tinyDB.putString("name", name);
        tinyDB.putString("keywords", keywords);
        tinyDB.putString("phoneno", phoneNO);
        tinyDB.putString("email", email);
        tinyDB.putInt("locationinterval", locationInterval);
        tinyDB.putBoolean("monitor", monitor);


    }
}