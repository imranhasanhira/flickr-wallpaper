package com.ihhira.projects.android.flickrwallpaper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WallpaperChangerService extends Service {

	@Override
	public void onCreate() {
		Log.d("FW", "OnCreate()");
		super.onCreate();
		
		
		//TODO task
		
		stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("FW", "OnBind()");
		return null;
	}

	private void onstart() {
		Log.d("FW", "OnStart()");

	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("FW", "OnUnbind()");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.d("FW", "OnDestroy()");
		super.onDestroy();
	}

}
