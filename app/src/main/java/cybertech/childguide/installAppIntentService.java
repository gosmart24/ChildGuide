package cybertech.childguide;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.List;


public class installAppIntentService extends IntentService {

    TinyDB tinyDB;
    private String childID;

    public installAppIntentService() {
        super("installAppIntentService");
    }

    // separating system apps from installed apps
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tinyDB = new TinyDB(installAppIntentService.this);
        childID = tinyDB.getString("childid");
        Log.i("Thread", "geting installed apps...");
        Log.i("Thread", "installed apps Childid : " + childID);
        StringBuilder builder = new StringBuilder();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                builder.append(appName + "______");
            }
        }
        Log.i("Thread", "installeapps  buffer size : " + builder.length());
        Response.Listener<String> listenerInstall = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Thread", "installed apps Response successfully uploaded : ");
            }
        };
        Log.i("Thread", "starting connection...");
        InstalledAppsConnection connection = new InstalledAppsConnection(childID, builder.toString(), listenerInstall);
        RequestQueue queue = Volley.newRequestQueue(installAppIntentService.this);
        queue.add(connection);
    }


}
