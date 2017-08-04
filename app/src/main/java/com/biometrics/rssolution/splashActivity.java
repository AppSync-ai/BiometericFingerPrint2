package com.biometrics.rssolution;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashActivity extends Activity {
	Handler handler;
	Timer timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_first);
		handler = new Handler();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(new Intent(splashActivity.this,InsertImeiActivity.class));
				
				finish();

			}
		}, 3000);
		
	}
}
