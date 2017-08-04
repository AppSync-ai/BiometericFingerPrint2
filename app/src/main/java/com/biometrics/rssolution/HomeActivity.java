/*package com.biometrics.rssolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HomeActivity extends Activity implements OnClickListener {

	ImageView connectDevise, batchManager, userDetail, enrolluser,
			makeAttendane, helpDesk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_layout);

		connectDevise = (ImageView) findViewById(R.id.connectdevice);
		connectDevise.setOnClickListener(this);
		batchManager = (ImageView) findViewById(R.id.batchmanager);
		batchManager.setOnClickListener(this);
		userDetail = (ImageView) findViewById(R.id.userdetails);
		userDetail.setOnClickListener(this);
		enrolluser = (ImageView) findViewById(R.id.enrolluser);
		
		enrolluser.setOnClickListener(this);
		
		makeAttendane = (ImageView) findViewById(R.id.attendence);
		
		makeAttendane.setOnClickListener(this);
		helpDesk = (ImageView) findViewById(R.id.helpview);
		
		helpDesk.setOnClickListener(this);
		
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.connectdevice: {
             //new MainActivity().Init_Sensor();
		}
			break;
		case R.id.batchmanager: {
			
			startActivity(new Intent(HomeActivity.this,BatchManager.class));
		
		}
			break;
		case R.id.userdetails: {
			
			startActivity(new Intent(HomeActivity.this,UserDetailActivity.class));
		}
			break;
		case R.id.enrolluser: {
			startActivity(new Intent(HomeActivity.this,EnrollUserActivity.class));
		}
			break;
		case R.id.attendence: {
			
			startActivity(new Intent(HomeActivity.this,AttendanceActivity.class));
		}
			break;
		case R.id.helpview: {

			
			
		}
			break;

		}
	}
}
*/