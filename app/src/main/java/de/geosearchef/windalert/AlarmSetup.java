package de.geosearchef.windalert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.format.DateUtils;
import android.text.format.Time;

/**
 * Created by Geosearchef on 09.04.2017.
 */

public class AlarmSetup extends BroadcastReceiver {

	public AlarmSetup() {

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			register(context);
		}
	}

	public static void register(Context context) {

		//AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent startServiceIntent = new Intent(context, AlarmReceiver.class);
		context.startService(startServiceIntent);

		//alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5L * 1000L, 5L * 1000L, pi);
		
		//Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		//vibrator.vibrate(2000);
		
		//Intent intent = new Intent(context, AlarmReceiver.class);
		//PendingIntent pendingIntent =
		//		PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		//AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5L * 1000L, pendingIntent);
	}
}
