package com.biometrics.rssolution.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.biometrics.rssolution.BatchManager;
import com.biometrics.rssolution.R;
import com.biometrics.rssolution.Helper.ReportItem;
import com.biometrics.rssolution.models.CityForecastBO;
import com.mantra.mfs100.MainActivity;

public class ReportAdapter extends ArrayAdapter<ReportItem> {
	Context con;
	ArrayList<ReportItem> data;
	
	public ReportAdapter(Context context, int resource,
			ArrayList<ReportItem> list) {
		super(context, resource, list);
		// TODO Auto-generated constructor stub
		this.con = context;
		this.data = list;
	}

	@Override
	public View getView(final int position, final View view, ViewGroup parent) {
	LayoutInflater inflater = ((Activity) con).getLayoutInflater();
	View rowView= inflater.inflate(R.layout.report_list_item, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.reportstudentname);
	TextView imageView = (TextView) rowView.findViewById(R.id.reportrollno);
	TextView time = (TextView) rowView.findViewById(R.id.time);
	txtTitle.setText(data.get(position).getName());
	imageView.setText(data.get(position).getId());
	time.setText(data.get(position).getTime());

	
	/*Button edit = (Button)rowView.findViewById(R.id.editview);
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
			data.remove(position);
			BatchManager.adapter.notifyDataSetChanged();
			
			
		}
	});
*/
	return rowView;
	}
	
}

