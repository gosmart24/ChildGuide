package cybertech.childguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tinyDB = new TinyDB(this);
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        calendar = Calendar.getInstance();
//        tinyDB.putString("childid", childID);
        boolean isParent = tinyDB.getBoolean("control");
        boolean isChild = tinyDB.getBoolean("monitor");

        if (isParent) {
            // call to the parent login activity.
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else if (isChild) {
            // call the service class. to start background processes.l
            Intent intent = new Intent(MainActivity.this, HelperService.class);
            MainActivity.this.startService(intent);
            finish();
        } else {
            // app is just started for first time display all menu.
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    public void onMonitor(View view) {
        startActivity(new Intent(MainActivity.this, Monitor.class));
        finish();
    }

    public void onControl(View view) {
        Log.i("Thread", "start clicked!");
        startActivity(new Intent(MainActivity.this, Contro.class));
        finish();
    }

}
