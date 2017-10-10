package de.geosearchef.windalert;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.widget.Toast;

import java.sql.SQLOutput;

/**
 * Created by Geosearchef on 09.04.2017.
 */

public class AlarmReceiver extends IntentService {
	
	
	public AlarmReceiver() {
		super("WindAlertSerivce");
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		Handler mHandler = new Handler(getMainLooper());
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Checking wind...", Toast.LENGTH_SHORT).show();
			}
		});
		System.out.println("Checking wind...");
		
		// prepare intent which is triggered if the
		// notification is selected
		
		Intent i = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		Notification n = new Notification.Builder(this)
				.setContentTitle("Wind warning")
				.setContentText("Strong winds on --- at ---.")
				.setContentIntent(pIntent)
				.setSmallIcon(R.drawable.wind)
				.setAutoCancel(true)
				.build();
		
		
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		//notificationManager.notify(0, n);
		
		
		scheduleNextUpdate();
	}
	
	private void scheduleNextUpdate() {
		Intent intent = new Intent(this, this.getClass());
		PendingIntent pendingIntent =
				PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 30L * 60L * 1000L, pendingIntent);
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "WindAlert active!", Toast.LENGTH_LONG).show();
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.err.println("started");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}