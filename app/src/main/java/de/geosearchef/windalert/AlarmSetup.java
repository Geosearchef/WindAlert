package de.geosearchef.windalert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Vibrator;

/**
 * Created by Geosearchef on 09.04.2017.
 */

public class AlarmSetup extends BroadcastReceiver {

	public AlarmSetup() {

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			register(context);
		}
	}

	public static void register(Context context) {

		//AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		//Intent i = new Intent(context, AlarmReceiver.class);
		//PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

		//alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5L * 3L * 1000L, 5L * 3L * 1000L, pi);

		//Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		//vibrator.vibrate(2000);
	}
}
