package com.biometrics.rssolution;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.biometrics.rssolution.Adapter.StudentBatchAdapter;
import com.biometrics.rssolution.Adapter.StudentListAdapter;
import com.biometrics.rssolution.models.CityForecastBO;
import com.example.Mydatabse.MyDataBase;

public class BatchManager extends Activity implements OnItemClickListener, OnItemSelectedListener {

	//EditText regNum;
	//ImageView batchbt;
	ListView list;
	ArrayList<CityForecastBO> studentlist;
	MyDataBase mdb;
	String BatchId;
	private ArrayList<String> spinlist;
	private Spinner spinner;
	private ArrayList<CityForecastBO> students;
	public static StudentBatchAdapter adapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eight_layout);
		mdb =new MyDataBase(this);
		//regNum = (EditText)findViewById(R.id.regnumedit);
		
		BatchId = mdb.getLatestBatchId();
		studentlist = new ArrayList<CityForecastBO>();
		studentlist = mdb.getNameAndRollbyBatch(BatchId);
		spinlist = mdb.getAllBatchId();
		spinner = (Spinner)findViewById(R.id.spinner);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item,spinlist);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(this);
		
		//regNum.setText(BatchId);
		Log.i("Batch"," list size : "+studentlist.size());
		adapter = new StudentBatchAdapter(this, R.layout.student_list_item, studentlist);
		
		list=(ListView)findViewById(R.id.Batchinfolist);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	adapter.notifyDataSetChanged();
		
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		BatchId = parent.getItemAtPosition(pos).toString();
		students = mdb.getNameAndRollbyBatch(BatchId);
		adapter = new StudentBatchAdapter(this, R.layout.student_list_item, students);
		list.setAdapter(adapter);
		
		adapter.notifyDataSetChanged();
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
