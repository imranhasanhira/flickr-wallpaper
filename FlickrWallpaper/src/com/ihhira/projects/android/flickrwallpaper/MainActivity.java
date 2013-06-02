package com.ihhira.projects.android.flickrwallpaper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	PendingIntent pendingIntent = null;

	boolean serviceStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Button b = (Button) findViewById(R.id.actionServiceButton);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Button b = (Button) arg0;

				if (!serviceStarted) {
					Log.d("FW", "Starting the service...");
					Intent intent = new Intent(MainActivity.this,
							WallpaperChangerService.class);

					pendingIntent = PendingIntent.getService(MainActivity.this,
							0, intent, 0);

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.add(Calendar.SECOND, 10);

					AlarmManager alamrManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					alamrManager.set(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(), pendingIntent);
					Log.d("FW", "Service started.");
					b.setBackgroundResource(R.drawable.stop_blue);
					serviceStarted = true;
				} else {
					Log.d("FW", "Stopping the service...");
					AlarmManager alamrManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					alamrManager.cancel(pendingIntent);
					Log.d("FW", "Service stopped.");
					b.setBackgroundResource(R.drawable.start_blue);
					serviceStarted = false;
				}
			}
		});

	}

	void doShit() {
		AsyncTask<Integer, String, Object> task = new AsyncTask<Integer, String, Object>() {

			@Override
			protected void onProgressUpdate(String... values) {

			}

			@Override
			protected Object doInBackground(Integer... arg0) {
				String jsonString = "";
				try {
					jsonString = getJSON();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jsonString = jsonString.substring(14, jsonString.length() - 1);
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = null;
				try {
					jsonObject = (JSONObject) parser.parse(jsonString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (jsonObject.get("stat").equals("ok")) {
					JSONObject photosObject = (JSONObject) jsonObject
							.get("photos");
					List<JSONObject> photos = ((List<JSONObject>) photosObject
							.get("photo"));
					for (JSONObject o : photos) {
						Log.e("", ((String) o.get("title")));
						String farmId = (String) o.get("farm_id");
						String serverID = (String) o.get("server_id");
						String photoID = (String) o.get("photo_id");
						String photoSecret = (String) o.get("secret");
						String resolution = "b";

						String photoURLString = "http://farm" + farmId
								+ ".staticflickr.com/" + serverID + "/"
								+ photoID + "_" + photoSecret + "_"
								+ resolution + ".jpg";
						setWallpaperFromURLString(photoURLString);
						break;
					}
				}
				// String urlString =
				// "http://farm8.staticflickr.com/7442/8845228315_cd86bbefeb_b.jpg";

				return null;
			}

			private void setWallpaperFromURLString(String urlString) {
				URL url = null;
				try {
					url = new URL(urlString);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					WallpaperManager.getInstance(MainActivity.this).setStream(
							url.openConnection().getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	String getJSON() throws MalformedURLException, IOException {
		URL url = new URL(
				"http://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=02a93fd3b676066e183b63db3dae5719&page=1&per_page=30&format=json");
		InputStream is = url.openConnection().getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[1024];
		for (int read = isr.read(buffer, 0, 1024); read > 0; read = isr.read(
				buffer, 0, 1024)) {
			sb.append(buffer, 0, read);
		}
		// Log.e("", sb.toString());
		return sb.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
