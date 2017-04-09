package de.geosearchef.windalert;

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

	private final String CITY_ID = "3220838";
	private JSONObject data;

	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.textView);

		new Thread(new Runnable() {
			@Override
			public void run() {
				generateData();
			}
		}).start();

		AlarmSetup.register(this.getApplicationContext());
	}


	public void generateData() {

		final StringBuilder displayString = new StringBuilder();
		data = getWeatherData();

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

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					textView.setText(displayString);
				}
			});

			System.out.println(displayString);
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}

	private String getFormattedDate(Date date) {
		String res = date.toLocaleString();
		return res.substring(0, res.length() - 3);
	}

	private String getFormattedWindSpeed(float speed) {
		return String.format("%.1f", speed);
	}

	/**
	 * @return The wind speed in km/h
	 */
	private float getWindSpeed(JSONObject weatherEntry) throws JSONException {
		return Float.parseFloat(weatherEntry.getJSONObject("wind").getString("speed")) * 3.6f;
	}

	private JSONObject getWeatherData() {
		try {
			return new JSONObject(get("http://api.openweathermap.org/data/2.5/forecast?id=" + CITY_ID + "&APPID=" + getResources().getString(R.string.api_key)));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String get(String url) {
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
			Toast.makeText(this, "Error while requesting weather data!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return null;
		}
	}
}
