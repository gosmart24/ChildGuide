package cybertech.childguide;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

public class AlarmService extends Service {
    AlarmManager alarmManager;
    Calendar calendar;


    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent callIntent = new Intent(AlarmService.this, CallReceiver.class);
        PendingIntent p = PendingIntent.getBroadcast(AlarmService.this, 100, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, p);
        return START_STICKY;
    }
}
