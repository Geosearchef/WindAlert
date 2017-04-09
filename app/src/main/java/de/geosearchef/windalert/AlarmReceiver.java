package de.geosearchef.windalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

/**
 * Created by Geosearchef on 09.04.2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

	public AlarmReceiver() {

	}

	public void onReceive(Context context, Intent intent) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
	}
}