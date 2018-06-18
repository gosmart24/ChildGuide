package cybertech.childguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ParentHome extends AppCompatActivity {

    TextView childName;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);
        childName = (TextView) findViewById(R.id.childNAME);
        tinyDB = new TinyDB(this);
        childName.setText(getChildName());
    }

    public void onLoadInstalledApps(View view) {
        startActivity(new Intent(ParentHome.this, InstalledApps.class));
    }

    public void onLoadLocations(View view) {
        startActivity(new Intent(ParentHome.this, RecentLocations.class));
    }

    public void onWordMonitor(View view) {
        startActivity(new Intent(ParentHome.this, WordMonitor.class));
    }

    public void onLoadBrowserHistory(View view) {
        startActivity(new Intent(ParentHome.this, BrowserHistory.class));
    }

    public void onRecentCallLogs(View view) {
        startActivity(new Intent(ParentHome.this, RecentCallLogs.class));
    }

    public void onLoadKeyStrokes(View view) {
        startActivity(new Intent(ParentHome.this, KeyStrokes.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.parentmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addchild) {
            startActivity(new Intent(ParentHome.this, AddChildID.class));
            finish();
        } else if (item.getItemId() == R.id.changeUsernamePass) {
            startActivity(new Intent(ParentHome.this, ChangeUserPassword.class));
            finish();
        } else if (item.getItemId() == R.id.selectedID) {
            startActivity(new Intent(ParentHome.this, DefaultChildID.class));
            finish();
        }else if (item.getItemId() == R.id.exitApp) {
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public String getChildName() {
        String name = null;
        String childID = tinyDB.getString("childid");
        ArrayList<String> childIDs = tinyDB.getListString("ids");
        ArrayList<String> names = tinyDB.getListString("names");
        for (int i = 0; i < childIDs.size(); i++) {
            if (childIDs.get(i).equals(childID)) {
                name = names.get(i);
            }
        }
        return name;
    }

}






