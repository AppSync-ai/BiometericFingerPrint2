package com.biometrics.rssolution;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.biometrics.rssolution.models.CityForecastBO;
import com.biometrics.rssolution.soapreq.AsyncTaskManager;
import com.biometrics.rssolution.soapreq.GetCityForcastByZipCodeTask;
import com.biometrics.rssolution.soapreq.InsertImeiDetailsTask;
import com.biometrics.rssolution.soapreq.OnAsyncTaskCompleteListener;
import com.example.Mydatabse.MyDataBase;
import com.mantra.mfs100.MainActivity;

public class InsertBatchActivity extends Activity implements
		OnItemSelectedListener {

	EditText vtpnum, imeinum, password;
	ImageView login;
	private String id, vtpno;
	private AsyncTaskManager taskManager;
	private Dialog dialog;
	MyDataBase mdb;
	String IMEI, VTP, DESC;
	private ArrayList<String> spinlist;
	private Spinner spinner;
	String BatchId = "null";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_batchactivity);
		mdb = new MyDataBase(InsertBatchActivity.this);
		vtpnum = (EditText) findViewById(R.id.regnumedit);
		VTP = mdb.getLatestVtp();
		vtpnum.setText(VTP);
		vtpnum.setKeyListener(null);
		imeinum = (EditText) findViewById(R.id.imeiedit);
		password = (EditText) findViewById(R.id.passedit);
		login = (ImageView) findViewById(R.id.login);
		spinner = (Spinner) findViewById(R.id.spinner);
		spinlist = mdb.getAllBatchId();
		HashMap<String, String> ids = new HashMap<String, String>();
		for(int i =0;i<spinlist.size();i++){
			if(spinlist.get(i)!=null||spinlist.get(i).length()>1)
				ids.put(spinlist.get(i), spinlist.get(i));
		}
		spinlist = new ArrayList<String>(ids.values());
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinlist);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(this);

		taskManager = new AsyncTaskManager(this);

		TelephonyManager mngr = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
		id = mngr.getDeviceId();
		imeinum.setText(id);
		imeinum.setKeyListener(null);

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// getCityForcast();
				// VTP = vtpnum.getText().toString();
				// IMEI = imeinum.getText().toString();
				
				
				if (password.getText().toString().equals(BatchId)) {
					DESC = BatchId;
					Log.i("InsertB","in 1st con Desc: "+DESC);

					// password.getText().toString();
					DESC.trim();
					if (DESC.length() != 0 || !DESC.equals("")) {
						// "07070020SS.10214002";
						//
						//
						mdb.insertBatchId(DESC, VTP);
						// insertIMEIDetails();
						startActivity(new Intent(InsertBatchActivity.this,
								MainActivity.class));
						finish();
						// TODO Auto-generated method stub
						// startActivity(new Intent(InsertImeiActivity.this,
						// HomeActivity.class));
					}
				 else
					showToastMessage("please enter a valid BatchId");
				}
				
				else if(password.getText().toString().length()>3){
					DESC = 
					 password.getText().toString();
					Log.i("InsertB","in 2nd con Desc: "+DESC);
					DESC.trim();
					if (DESC.length() != 0 || !DESC.equals("")) {
						
						mdb.insertBatchId(DESC, VTP);
						// insertIMEIDetails();
						startActivity(new Intent(InsertBatchActivity.this,
								MainActivity.class));
						finish();
						// TODO Auto-generated method stub
						// startActivity(new Intent(InsertImeiActivity.this,
						// HomeActivity.class));
					}
				 else
					showToastMessage("please enter a valid BatchId");
				}
				else
					showToastMessage("please enter a valid BatchId");
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (taskManager == null) {
			taskManager = new AsyncTaskManager(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissDialog();
	}

	private void insertIMEIDetails() {
		InsertImeiDetailsTask task = new InsertImeiDetailsTask(IMEI, VTP, DESC);
		taskManager.executeTask(task, task.createRequest(), "login with imei ",
				new OnAsyncTaskCompleteListener<Integer>() {

					@Override
					public void onTaskCompleteSuccess(Integer result) {
						// updateForecastDetails(result);
						OnIMEIRegister(result);
						// Log.i("nik", "Insert IMEI Result Code:" + result);
						// Log.i("nik","id :"+result.getState());
					}

					@Override
					public void onTaskFailed(Exception cause) {
						// Log.e(TAG, cause.getMessage(), cause);
						showToastMessage("An error occured while attempting to invoke the InsertImei web service");
					}
				});
		// }
	}

	private void OnIMEIRegister(Integer resultCode) {
		if (resultCode != null) {
			// Log.i("nik", "Insert IMEI Result Code:" + resultCode.toString());
			if ((resultCode.toString().equals("-3")) || (resultCode == 1)) {
				Log.i("nik", "Insert IMEI Result Code: in call" + resultCode);

				// mdb.insertImeiDetails(IMEI, VTP,DESC );

				// mdb.insertImeiDetails("myimei", "20707", "desc");

				startActivity(new Intent(InsertBatchActivity.this,
						MainActivity.class));
			}
		}
	}

	private void showToastMessage(String messageId) {
		Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
	}

	private void dismissDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long arg3) {
		// TODO Auto-generated method stub
		BatchId = parent.getItemAtPosition(pos).toString();
		password.setText(BatchId);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
