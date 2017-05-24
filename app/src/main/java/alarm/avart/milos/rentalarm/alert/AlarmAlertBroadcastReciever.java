package alarm.avart.milos.rentalarm.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import alarm.avart.milos.rentalarm.MainActivity;
import alarm.avart.milos.rentalarm.NicknameActivity;
import alarm.avart.milos.rentalarm.alarm.Alarm;
import alarm.avart.milos.rentalarm.service.AlarmServiceBroadcastReciever;

/**
 * Created by My Star on 8/6/2016.
 */
public class AlarmAlertBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mathAlarmServiceIntent = new Intent(context, AlarmServiceBroadcastReciever.class);
        context.sendBroadcast(mathAlarmServiceIntent, null);

        StaticWakeLock.lockOn(context);
        Bundle bundle = intent.getExtras();
        final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        Intent mathAlarmAlertActivityIntent;

        mathAlarmAlertActivityIntent = new Intent(context, alarm.avart.milos.rentalarm.MainActivity.class);

        mathAlarmAlertActivityIntent.putExtra("alarm", alarm);

        mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(mathAlarmAlertActivityIntent);
    }
}
