package cybertech.childguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DefaultChildID extends AppCompatActivity {

    ListView childrenList;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_child_id);
        tinyDB = new TinyDB(this);
        childrenList = (ListView) findViewById(R.id.spinner);
        final ArrayList<String> childrenArray = tinyDB.getListString("names");
        final ArrayList<String> childrenIDArray = tinyDB.getListString("ids");
        childrenList.setAdapter(new DefaultchildAdapter(this, childrenArray, childrenIDArray));
        childrenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DefaultChildID.this);
                builder.setTitle("Status : " + childrenArray.get(position).toUpperCase() + " - " + childrenIDArray.get(position));
                builder.setNegativeButton("No", null);
                DialogInterface.OnClickListener onset = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tinyDB.remove("childid");
                        tinyDB.putString("childid", tinyDB.getListString("ids").get(position));
                        AlertDialog.Builder builder = new AlertDialog.Builder(DefaultChildID.this);
                        builder.setTitle("Help Info");
                        DialogInterface.OnClickListener onOK = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(DefaultChildID.this, ParentHome.class));
                                finish();
                            }
                        };
                        builder.setNegativeButton("OK", onOK);
                        builder.setCancelable(false);
                        builder.setMessage("Your default child has been set to " + childrenArray.get(position).toUpperCase());
                        builder.show();

                    }
                };
                builder.setPositiveButton("Yes", onset);
                builder.setCancelable(true);
                builder.setMessage("Do you want to set " + childrenArray.get(position).toUpperCase() + " as default Child?");
                builder.show();
            }
        });
        //childrenList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, childrenArray));
        showMSG("Help Info", "Set a child as default to view his Activity records");
    }
    private void showMSG(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setNegativeButton("OK", null);
        builder.setCancelable(true);
        builder.setMessage(msg);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DefaultChildID.this, ParentHome.class));
        finish();
    }
}
