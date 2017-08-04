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

import com.biometrics.rssolution.Adapter.StudentListAdapter;
import com.biometrics.rssolution.models.CityForecastBO;
import com.example.Mydatabse.MyDataBase;
import com.mantra.mfs100.MainActivity;

public class UserDetailActivity extends Activity implements OnItemClickListener,OnItemSelectedListener
{
	ListView list;
	MyDataBase mdb;
	ArrayList<CityForecastBO> students;
	String BatchId;
	StudentListAdapter adapter;
	Spinner spinner;
	
	
	ArrayList<String> spinlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fourth_layout);
		mdb = new MyDataBase(this);
		BatchId = mdb.getLatestBatchId();
		spinlist = mdb.getAllBatchId();
		spinner = (Spinner)findViewById(R.id.spinner1);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item,spinlist);
		dataAdapter.setDropDownViewResource
        (android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(this);
		if(MainActivity.userinserted){
			students = mdb.getNameAndRollbyBatch(BatchId);
			adapter = new StudentListAdapter(this, R.layout.student_list_item, MainActivity.StudentList);
		}else
		 adapter = new StudentListAdapter(this, R.layout.student_list_item, students);

		list=(ListView)findViewById(R.id.studentinfolist);
        list.setAdapter(adapter);
        if(getIntent().getExtras()!=null){
        	list.setOnItemClickListener(this);
        }
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long arg3) {
		// TODO Auto-generated method stub
		
		 CityForecastBO obj  = (CityForecastBO) parent.getItemAtPosition(pos); 
		 String rollno = obj.getCity();
		 String StudentName = obj.getState();
		 Intent i = new Intent(UserDetailActivity.this,MainActivity.class);
		 i.putExtra("rollno",rollno);
		 i.putExtra("StudentName",StudentName);
		 i.putExtra("user", "user");
		 startActivity(i);
		 finish();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		Log.i("Spinner","spinner called");
		BatchId = parent.getItemAtPosition(pos).toString();
		students = mdb.getNameAndRollbyBatch(BatchId);
		adapter = new StudentListAdapter(this, R.layout.student_list_item, students);
		list.setAdapter(adapter);
		
		adapter.notifyDataSetChanged();
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
