package de.geosearchef.windalert;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

	private static final String CITY_ID = "3220838";
	private static JSONObject data;

	private static TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.textView);

		new Thread(new Runnable() {
			@Override
			public void run() {
				generateData(MainActivity.this, MainActivity.this);
			}
		}).start();

		AlarmSetup.register(this.getApplicationContext());
	}


	public static void generateData(Context context, Activity activity) {

		final StringBuilder displayString = new StringBuilder();
		data = getWeatherData(context);

		try {
			JSONArray list = data.getJSONArray("list");

			for(int i = 0;i < list.length();i++) {
				JSONObject entry = list.getJSONObject(i);

				Date date = new Date(entry.getLong("dt") * 1000L);
				float windSpeed = getWindSpeed(entry);


				displayString.append(getFormattedDate(date) + ":   " + getFormattedWindSpeed(windSpeed) + " km/h" + "\n");
			}

			if(displayString.length() > 0)
				displayString.deleteCharAt(displayString.length() - 1);

			if(textView != null && activity != null) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						textView.setText(displayString);
					}
				});
			}

			System.out.println(displayString);
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}

	private static String getFormattedDate(Date date) {
		String res = date.toLocaleString();
		return res.substring(0, res.length() - 3);
	}

	private static String getFormattedWindSpeed(float speed) {
		return String.format("%.1f", speed);
	}

	/**
	 * @return The wind speed in km/h
	 */
	private static float getWindSpeed(JSONObject weatherEntry) throws JSONException {
		return Float.parseFloat(weatherEntry.getJSONObject("wind").getString("speed")) * 3.6f;
	}

	private static JSONObject getWeatherData(Context context) {
		try {
			return new JSONObject(get("http://api.openweathermap.org/data/2.5/forecast?id=" + CITY_ID + "&APPID=" + context.getResources().getString(R.string.api_key), context));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String get(String url, Context context) {
		try {
			HttpClient client = HttpClientBuilder.create().build();

			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);

			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer res = new StringBuffer();
			String line = "";
			while((line = reader.readLine()) != null) {
				res.append(line);
			}
			return res.toString();
		} catch(IOException e) {
			if(context != null) {
				Toast.makeText(context, "Error while requesting weather data!", Toast.LENGTH_LONG).show();
			}
			e.printStackTrace();
			return null;
		}
	}
}
