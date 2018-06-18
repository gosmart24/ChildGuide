package cybertech.childguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstallAppsReceiver extends BroadcastReceiver {
    public InstallAppsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Thread","OnReceiver Method");
        Intent intent2 = new Intent(context, installAppIntentService.class);
        context.startService(intent2);
    }
}
