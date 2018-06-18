package cybertech.childguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

    public CallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Thread","OnReceiver Method");
        Intent intent2 = new Intent(context, CallIntentService.class);
        //startActivity(intent);
        context.startService(intent2);
    }
}