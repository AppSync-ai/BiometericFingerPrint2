package com.biometrics.rssolution.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.biometrics.rssolution.BatchManager;
import com.biometrics.rssolution.R;
import com.biometrics.rssolution.models.CityForecastBO;
import com.example.Mydatabse.MyDataBase;
import com.mantra.mfs100.MainActivity;

public class StudentBatchAdapter extends ArrayAdapter<CityForecastBO> {
	Context con;
	ArrayList<CityForecastBO> data;
	
	public StudentBatchAdapter(Context context, int resource,
			ArrayList<CityForecastBO> list) {
		super(context, resource, list);
		// TODO Auto-generated constructor stub
		this.con = context;
		this.data = list;
	}

	@Override
	public View getView(final int position, final View view, ViewGroup parent) {
	LayoutInflater inflater = ((Activity) con).getLayoutInflater();
	View rowView= inflater.inflate(R.layout.listview_item, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.stuname);
	TextView imageView = (TextView) rowView.findViewById(R.id.stuid);
	txtTitle.setText(data.get(position).getCity());
	imageView.setText(data.get(position).getState());
	if(new MyDataBase(con).getStudentPhotoByrollno(data.get(position).getCity()).length()>10){
		txtTitle.setTextColor(Color.GREEN);
		imageView.setTextColor(Color.GREEN);

	}
	Button edit = (Button)rowView.findViewById(R.id.editview);
	edit.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent(con,MainActivity.class);
			i.putExtra("StudentName",data.get(position).getState());
			i.putExtra("rollno",data.get(position).getCity());
			i.putExtra("user", "user");
			i.putExtra("batch", "batch");
			con.startActivity(i);
			((Activity) con).finish();
		}
	});
	Button delete = (Button)rowView.findViewById(R.id.deleteview);
	delete.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			data.remove(position);
//			BatchManager.adapter.notifyDataSetChanged();
//			Log.i("dbadel","before deleting roll no : "+data.get(position).getState()+" data size is : "+data.size());
			if(position>=0&&position<data.size())
			new MyDataBase(con).deleteUser(data.get(position).getCity());
			data.remove(position);

			BatchManager.adapter.notifyDataSetChanged();
//		//	Nikhil � 1 min
			
		}
	});

	return rowView;
	}
	
}
