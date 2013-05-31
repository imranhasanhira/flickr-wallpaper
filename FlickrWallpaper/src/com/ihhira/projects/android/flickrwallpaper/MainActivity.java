package com.ihhira.projects.android.flickrwallpaper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

				}

			}
		};

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

		setContentView(R.layout.activity_main);

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
