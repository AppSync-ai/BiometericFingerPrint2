package com.biometrics.rssolution.Helper;

public class ReportItem {
	
	String name,id,Time;
	
	public ReportItem(){
		
	}
	
public ReportItem(String name,String id,String time){
		this.name =name;
		this.id = id;
		this.Time = time;
	}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getTime() {
	return Time;
}

public void setTime(String time) {
	Time = time;
}



}
