package com.biometrics.rssolution;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.biometrics.rssolution.Adapter.ReportAdapter;
import com.biometrics.rssolution.Adapter.StudentListAdapter;
import com.biometrics.rssolution.Helper.ReportItem;
import com.biometrics.rssolution.models.CityForecastBO;
import com.example.Mydatabse.MyDataBase;
import com.mantra.mfs100.MainActivity;

public class ReportActivity extends Activity implements OnItemClickListener,
		OnItemSelectedListener {
	ListView list;
	MyDataBase mdb;
	ArrayList<ReportItem> students;
	String BatchId =null;
	ReportAdapter adapter;
	Spinner spinner;
	Spinner spinner2;
	String Date=null;
	ArrayList<String> alldates;
	ArrayList<String> spinlist;
	boolean batchSelected=false;
	boolean dateSelected = false;
	int count=0;
	int numofspinner =1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_activity);
		mdb = new MyDataBase(this);
		//BatchId = mdb.getLatestBatchId();
		spinlist = mdb.getAllBatchId();
		//alldates = mdb.getAllDateByBatchId(BatchId);
		String batch = mdb.getLatestBatchId();
		alldates = mdb.getAllDateByBatchId(batch);
		for(int i =0;i<alldates.size();i++){
			Log.i("dbadel","dates : "+ alldates.get(i));
		}
		
		list = (ListView) findViewById(R.id.studentinfolist);
		spinner = (Spinner) findViewById(R.id.report_spinner1);
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
				Log.i("Report", "spinner1 called and count is "+count +" "+parent.getItemAtPosition(pos).toString());
				//students = mdb.getNameAndRollbyDate(Date);
				if(count<numofspinner){
					count++;
				}else{
					BatchId = parent.getItemAtPosition(pos).toString();
					if(BatchId.length()>4){
				alldates = mdb.getAllDateByBatchId(BatchId);
//				if(alldates.contains(null))
				alldates.remove(0);
				for(int i =0;i<alldates.size();i++){
					Log.i("dbadel","from spinner dates : "+ alldates.get(i));
				}
				ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(ReportActivity.this,
						android.R.layout.simple_spinner_item, alldates);
				dataAdapter2
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner2.setAdapter(dataAdapter2);
				spinner2.setVisibility(View.VISIBLE);
				batchSelected=true;
					}
					else 
						showToastMessage("select a valid BatchId");
				}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		spinner2 = (Spinner)findViewById(R.id.report_spinner2);
		//spinner2.setVisibility(View.INVISIBLE);
//		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, alldates);
//		dataAdapter2
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		if(alldates!=null)
//		spinner2.setAdapter(dataAdapter2);
		
		//if(spinner2.getVisibility()==View.INVISIBLE)
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(batchSelected&&alldates.size()>0){
				
					Date = parent.getItemAtPosition(pos).toString();
					Log.i("Report","in date selection date is : "+Date);
				if(Date!=null||!Date.equals("")){
					Log.i("Report","in date selection");
					students = mdb.getNameAndRollbyDate(Date,BatchId);
					if(students!=null)
					for(int i =0;i<students.size();i++){
						Log.i("Report","student name: "+students.get(i).getTime());
					}
					adapter = new ReportAdapter(ReportActivity.this, R.layout.student_list_item,
					students);
					list.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					count++;	
				}
					else{
						Log.i("Report","spinner2 clicked Date in null");
						showToastMessage("select a valid Date");
					}
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
			//students = mdb.getNameAndRollbyDate(Date);
//			adapter = new ReportAdapter(this, R.layout.student_list_item,
//					students);
		

		if (students != null)
			list.setAdapter(adapter);
		else
			Toast.makeText(this, "no students in database", Toast.LENGTH_SHORT)
					.show();
		if (getIntent().getExtras() != null) {
			list.setOnItemClickListener(this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long arg3) {
		// TODO Auto-generated method stub

		CityForecastBO obj = (CityForecastBO) parent.getItemAtPosition(pos);
		String rollno = obj.getCity();
		String StudentName = obj.getState();
		Intent i = new Intent(ReportActivity.this, MainActivity.class);
		i.putExtra("rollno", rollno);
		i.putExtra("StudentName", StudentName);
		i.putExtra("user", "user");
		startActivity(i);
		finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long arg3) {
		// TODO Auto-generated method stub

		Log.i("Report", "spinner called  "+v.getId());
		switch (v.getId()) {

		case R.id.report_spinner1: {
			
			Log.i("Report", "spinner1 called");
			BatchId = parent.getItemAtPosition(pos).toString();
			//students = mdb.getNameAndRollbyDate(Date);
			alldates = mdb.getAllDateByBatchId(BatchId);
			spinner2.setVisibility(View.VISIBLE);
			ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, alldates);
			dataAdapter2
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner2.setAdapter(dataAdapter2);
			/*adapter = new StudentListAdapter(this, R.layout.student_list_item,
					students);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();*/

		}
			break;
		case R.id.report_spinner2: {
			if(Date!=null){
			Log.i("Report","in date selection");
			Date = parent.getItemAtPosition(pos).toString();
			students = mdb.getNameAndRollbyDate(Date,BatchId);
			adapter = new ReportAdapter(this, R.layout.student_list_item,
			students);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			}
			else
				Log.i("Report","spinner2 clicked Date in null");
			
		}
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	private void showToastMessage(String messageId) {
		Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
	}

}
