package cybertech.childguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Thread","SMS OnReceiver Method");
        Intent intent2 = new Intent(context, SMSIntentService.class);
        context.startService(intent2);
    }

}
