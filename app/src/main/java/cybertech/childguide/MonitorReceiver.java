package cybertech.childguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MonitorReceiver extends BroadcastReceiver {

    Intent UpdateIntents;

    public MonitorReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                UpdateIntents = new Intent(context, SMSIntentService.class);
            } else if (i == 1) {
                UpdateIntents = new Intent(context, CallIntentService.class);
            } else if (i == 2) {
                UpdateIntents = new Intent(context, installAppIntentService.class);
            } else if (i == 3) {
                UpdateIntents = new Intent(context, BrowserIntentService.class);
            } else if (i == 4) {
                UpdateIntents = new Intent(context, MonitorService.class);
            }else if (i == 5) {
                UpdateIntents = new Intent(context, LocationUpdates.class);
            }
            context.startService(UpdateIntents);

        }
    }


}
