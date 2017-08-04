package com.example.Mydatabse;

public class Student {
	String name;
	String LeftImpression;
	String RightImpression;
	String studentId;
	
	public Student(){
		
	}
	
	public Student(String name,String leftImpression,String rightImpression,String Id){
		
		this.name=name;
		this.LeftImpression=leftImpression;
		this.RightImpression=rightImpression;
		this.studentId = Id;
		
	}
	
	public String getStudentName(){
		return this.name;
	}
	
	public String getStudentId(){
		return this.studentId;
	}
	
	public String getLeftImpression(){
		return this.LeftImpression;
	}
	
	public String getRightImpression(){
		return this.RightImpression;
	}
	
	
	public void setStudentName(String studentName){
		this.name = studentName;
	}
	
	public void setStudentId(String id){
		this.studentId = id;
	}
	public void setLeftImpression(String Limpression){
		this.LeftImpression = Limpression;
	}
	public void setRightImpression(String Rimpression){
		this.RightImpression = Rimpression;
	}
}
