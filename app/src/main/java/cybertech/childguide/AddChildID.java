package cybertech.childguide;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class AddChildID extends AppCompatActivity {

    EditText childID_ED;
    String childID;
    TextView display;
    TinyDB tinyDB;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_menu);
        childID_ED = (EditText) findViewById(R.id.newChildID);
        display = (TextView) findViewById(R.id.display_NewChildID);
        childID_ED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        tinyDB = new TinyDB(this);
    }

    public void onChildID(View view) {
        childID = childID_ED.getText().toString();
        if (!childID.isEmpty()) {
            chechID(childID);
        } else {
            display.setText(R.string.requiredfields);
        }
    }

    public void chechID(String ChildID) {
        progressDialog = ProgressDialog.show(AddChildID.this, null, "Verifying Child Id...please wait!", true, true);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("success");
                    if (status) {
                        String name = jsonObject.getString("name");
                        ArrayList<String> names = tinyDB.getListString("names");
                        ArrayList<String> childIDs = tinyDB.getListString("ids");
                        names.add(name);
                        childIDs.add(childID);
                        tinyDB.remove("names");
                        tinyDB.remove("ids");
                        tinyDB.putListString("ids", childIDs);
                        tinyDB.putListString("names", names);
                        DialogInterface.OnClickListener onOK = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(AddChildID.this, ParentHome.class));
                                finish();
                            }
                        };
                        showMSG("Status", "Your child id has been added successfully", onOK);
                    } else {
                        String msg = jsonObject.getString("message");
                        display.setText(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ParentConnection connection = new ParentConnection(ChildID, listener);
        RequestQueue queue = Volley.newRequestQueue(AddChildID.this);
        queue.add(connection);

    }

    private void showMSG(String title, String msg, DialogInterface.OnClickListener onOK) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help Info");
        builder.setNegativeButton("OK", onOK);
        builder.setCancelable(true);
        builder.setMessage(msg);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddChildID.this, ParentHome.class));
        finish();
    }
}
