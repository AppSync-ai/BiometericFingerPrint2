package com.biometrics.rssolution;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Mydatabse.MyDataBase;

public class SelectBatchActivity extends Activity {

	private MyDataBase mdb;
	private ArrayList<String> spinlist;
	private Spinner spinner;
	public static String BatchId;
	int count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.select_batch_activity);
		mdb = new MyDataBase(this);
		//BatchId = mdb.getLatestBatchId();
		spinlist = mdb.getAllBatchId();
		//alldates = mdb.getAllDateByBatchId(BatchId);
		String batch = mdb.getLatestBatchId();
		
		((ImageView)findViewById(R.id.homeview)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		spinner = (Spinner) findViewById(R.id.selectBatchspinner);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinlist);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(count>=1){
				BatchId = parent.getItemAtPosition(pos).toString();
				if(BatchId.length()>4)
				startActivity(new Intent(SelectBatchActivity.this,AttendanceActivity.class));
				}
				else
					Toast.makeText(SelectBatchActivity.this, " please select a valid BatchId", Toast.LENGTH_SHORT).show();
				count++;
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
}
