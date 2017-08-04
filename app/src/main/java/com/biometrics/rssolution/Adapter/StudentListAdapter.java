package com.biometrics.rssolution.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biometrics.rssolution.R;
import com.biometrics.rssolution.models.CityForecastBO;
import com.example.Mydatabse.MyDataBase;

public class StudentListAdapter extends ArrayAdapter<CityForecastBO> {
	Context con;
	ArrayList<CityForecastBO> data;
	
	public StudentListAdapter(Context context, int resource,
			ArrayList<CityForecastBO> list) {
		super(context, resource, list);
		// TODO Auto-generated constructor stub
		this.con = context;
		this.data = list;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = ((Activity) con).getLayoutInflater();
	View rowView= inflater.inflate(R.layout.student_list_item, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.studentname);
	TextView imageView = (TextView) rowView.findViewById(R.id.rollno);
	txtTitle.setText(data.get(position).getCity());
	imageView.setText(data.get(position).getState());
	//if(new MyDataBase(con).getStudentPhotoByrollno(data.get(position).getCity()).length()>10){
		//txtTitle.setTextColor(Color.GREEN);
		//imageView.setTextColor(Color.GREEN);
		if(new MyDataBase(con).getStudentFP1Byrollno(data.get(position).getCity()).length>100)
		{
			txtTitle.setTextColor(Color.GREEN);
			imageView.setTextColor(Color.GREEN);
		}
	//}
	return rowView;
	}
	
}
