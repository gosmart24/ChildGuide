package cybertech.childguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WordMonitorReceiver extends BroadcastReceiver {
    public WordMonitorReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Thread", "Monitor Words OnReceiver Method");
        Intent intent2 = new Intent(context, MonitorService.class);
        context.startService(intent2);
    }
}
