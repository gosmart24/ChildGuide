package cybertech.childguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Contro extends AppCompatActivity {

    static ConnectivityManager connectivityManager;
    EditText et_trackeringID, et_Username, et_Password, et_ConfirmPass;
    String trackeringID, username, password, confirmPassword;
    TextView display;
    SharedPreferences sp;
    TinyDB tinyDB;
    ProgressDialog progressDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contro);

        et_trackeringID = (EditText) findViewById(R.id.trackingID);
        et_Username = (EditText) findViewById(R.id.parentUsername);
        et_Password = (EditText) findViewById(R.id.parentPassword);
        et_ConfirmPass = (EditText) findViewById(R.id.parentConfirmPass);
        display = (TextView) findViewById(R.id.tv_Display);
        tinyDB = new TinyDB(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        et_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!checkConnection(Contro.this)) {
            NetworkAlert(Contro.this);
        }
    }

    public void onSubmit(View view) {
        trackeringID = et_trackeringID.getText().toString();
        username = et_Username.getText().toString();
        password = et_Password.getText().toString();
        confirmPassword = et_ConfirmPass.getText().toString();
        if (!trackeringID.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (password.equals(confirmPassword)) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status) {
                                tinyDB.putString("username", username.trim());
                                tinyDB.putString("password", password.trim());
                                tinyDB.putBoolean("control", true);
                                ArrayList<String> names = new ArrayList<>();
                                names.add(jsonObject.getString("name"));
                                tinyDB.putListString("names", names);
                                ArrayList<String> ChildIDs = new ArrayList<>();
                                ChildIDs.add(trackeringID);
                                tinyDB.putListString("ids", ChildIDs);
                                // setting default child ID.
                                tinyDB.putString("childid", trackeringID);

                                startActivity(new Intent(Contro.this, Login.class));
                                finish();
                            } else {
                                String msg = jsonObject.getString("message");
                                display.setText(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                progressDialog = ProgressDialog.show(Contro.this, null, "Verifying Child Id...please wait!", true, true);
                ParentConnection connection = new ParentConnection(trackeringID, listener);
                RequestQueue queue = Volley.newRequestQueue(Contro.this);
                queue.add(connection);

            } else {
                display.setText(R.string.passwordmismatch);
                et_ConfirmPass.setText("");
                et_Password.setText("");
            }
        } else {
            display.setText(R.string.requiredfields);
        }
    }
}
