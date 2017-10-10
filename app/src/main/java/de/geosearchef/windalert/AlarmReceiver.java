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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Date;

/**
 * Created by Geosearchef on 09.04.2017.
 */

public class AlarmReceiver extends IntentService {
	
	
	public AlarmReceiver() {
		super("WindAlertSerivce");
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		System.out.println("Checking wind...");

		//Get wind data
		JSONObject data = MainActivity.getWeatherData(this);

		try {
			JSONArray list = data.getJSONArray("list");

			Date beginningDate = null;
			float maxSpeedOverLimit = 0f;

			Date endingDate = null;
			search:
			for(int i = 0;i < list.length();i++) {
				JSONObject entry = list.getJSONObject(i);

				Date date = new Date(entry.getLong("dt") * 1000L);
				float windSpeed = MainActivity.getWindSpeed(entry);

				if(windSpeed < 16f) {
					if(maxSpeedOverLimit != 0f) {
						endingDate = date;
						break search;
					}

					beginningDate = null;
				}

				if(windSpeed >= 16f && beginningDate == null) {
					beginningDate = date;
				}

				if(windSpeed >= 21f) {
					maxSpeedOverLimit = Math.max(maxSpeedOverLimit, windSpeed);
				}
			}

			if(maxSpeedOverLimit != 0f) {
				showWarningNotification(MainActivity.getFormattedWindSpeed(maxSpeedOverLimit) + " km/h:   " + beginningDate.getDate() + "." + (beginningDate.getMonth() + 1) + ". " + beginningDate.getHours() + " Uhr  -  " + endingDate.getDate() + "." + (endingDate.getMonth() + 1) + ". " + endingDate.getHours() + " Uhr");
			}

		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		
		scheduleNextUpdate();
	}

	private void showWarningNotification(String warning) {
		Intent i = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		Notification n = new Notification.Builder(this)
				.setContentTitle("Wind warning")
				.setContentText(warning)
				.setContentIntent(pIntent)
				.setSmallIcon(R.drawable.wind)
				.setAutoCancel(true)
				.build();


		NotificationManager notificationManager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, n);
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
//		Toast.makeText(this, "WindAlert active!", Toast.LENGTH_LONG).show();
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.err.println("started");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}