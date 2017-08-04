package com.biometrics.rssolution;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.biometrics.rssolution.Helper.PrefenrenceHelper;
import com.biometrics.rssolution.soapreq.AsyncTaskManager;
import com.biometrics.rssolution.soapreq.InsertImeiDetailsTask;
import com.biometrics.rssolution.soapreq.OnAsyncTaskCompleteListener;
import com.example.Mydatabse.MyDataBase;
import com.mantra.mfs100.MainActivity;

public class InsertImeiActivity extends Activity {

	EditText vtpnum, imeinum, password;
	ImageView login;
	private String id, vtpno;
	private AsyncTaskManager taskManager;
	private Dialog dialog;
	MyDataBase mdb;
	String IMEI, VTP, DESC;
	public String masterPassword, normalpassword;
	PrefenrenceHelper prefs;
	String Master = "master";
	String Password = "password";
	String VTPPref = "vtpnumber";
	Button forgot;
	private  String val1="";
	AlertDialog d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		vtpnum = (EditText) findViewById(R.id.regnumedit);
		imeinum = (EditText) findViewById(R.id.imeiedit);
		password = (EditText) findViewById(R.id.passedit);
		forgot = (Button) findViewById(R.id.forgot);

		login = (ImageView) findViewById(R.id.login);
		prefs = new PrefenrenceHelper(this);
		// prefs.SavePreferences(Master, null);
		// prefs.SavePreferences(Password, null);
		// prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//
		// Editor editor = prefs.edit();
		// editor.putString("MasterPassword", "");
		// editor.putString("Password", "");
		// editor.commit();
		//
		
		if(!prefs.GetPreferences(VTPPref).equals("null")){
			String vtpno = prefs.GetPreferences(VTPPref);
			vtpnum.setText(vtpno);
			vtpnum.setKeyListener(null);
		}
		
		prefs.SavePreferences(Master, "rss110092");
		if (prefs.GetPreferences(Master).equals("null")) {
			//showInputDialog();
		}

		if (prefs.GetPreferences(Password).equals("null")) {
			showToastMessage("please enter and save password for the first time");
		} else {
			forgot.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showForgotDialog();
				}
			});
		}
		//
		// if (prefs.getString("Password",
		// null).length()>0||prefs.getString("Password", null)!=null) {
		// // etc
		// }
		//
		if (!isNetworkConnected())
			showToastMessage("please connect to internet");
		taskManager = new AsyncTaskManager(this);

		TelephonyManager mngr = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
		id = mngr.getDeviceId();
		imeinum.setText(id);
		imeinum.setKeyListener(null);

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//showToastMessage("please enter a valid BatchId");
				String temp = null;
                if(prefs.GetPreferences(VTPPref).equals("null")){
				VTP = vtpnum.getText().toString().trim();
				if(VTP.length()>0)
				prefs.SavePreferences(VTPPref, VTP);
                }
                else
                	VTP=prefs.GetPreferences(VTPPref);
				 //"107070020";
				//vtpnum.getText().toString().trim();
				IMEI = imeinum.getText().toString().trim();
				DESC = " ";
				// password.getText().toString();
				normalpassword = 
						password.getText().toString().trim();

				if (!prefs.GetPreferences(Password).equals("null")) {
					// etc
					temp = prefs.GetPreferences(Password);
				} else {
					if(normalpassword.length()>0)
					prefs.SavePreferences(Password, normalpassword);
					else showToastMessage("password length should be greater than zero");
					temp = normalpassword;
				}
				
				
				  /*  String NAMESPACE1 = "http://tempuri.org/";
					 String URL1= "http://onlinerealsoft.com/vtp.asmx?op=VTPIMEICheck1";
					  String SOAP_ACTION1 = "http://tempuri.org/VTPIMEICheck1";
					  String USER_DAILY_ATENDENCE_METHOD_NAME1 = "VTPIMEICheck1"; 
					   SoapObject response1;
					
					

			       	try {
							
							Log.i("IMEINo", IMEI);
						
						
							
							SoapObject request1 = new SoapObject(NAMESPACE1,USER_DAILY_ATENDENCE_METHOD_NAME1);
							PropertyInfo loginname1 = new PropertyInfo();
							loginname1.setName("IMEINo");
							loginname1.setValue(IMEI);
							loginname1.setType(String.class);
							request1.addProperty(loginname1);

						

							SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
									SoapEnvelope.VER11);
							envelope1.dotNet = true;
							
							envelope1.setOutputSoapObject(request1);
							
							HttpTransportSE androidHttpTransport1 = new HttpTransportSE(URL1);
							androidHttpTransport1.debug = true;
							androidHttpTransport1.call(SOAP_ACTION1, envelope1);
							
							response1 = (SoapObject)envelope1.bodyIn;
							response1 = (SoapObject) envelope1.getResponse();
							
							
							
							showToastMessage("sharad12");
							
							
							
							
						String	results1 = response1.toString();
						
						showToastMessage(results1);
							

						} catch (Exception e) {
							e.printStackTrace();
						}*/
			       	
			       	
			       	
			       	

				if (VTP.length() != 0 && DESC.length() != 0) {
					if (isNetworkConnected()) {
						if (temp.equals(normalpassword))
							
						{
							 val1=	com.mantra.mfs100.CommonMethod.getPrefsData(getBaseContext(), com.mantra.mfs100.Constants.LoginValue, "").trim();
							//if(val1.equalsIgnoreCase("0"))
							//{
								
						//	startActivity(new Intent(InsertImeiActivity.this,
									//MainActivity.class));
							//}
								
								if(val1.equalsIgnoreCase("1"))
							{
								
							startActivity(new Intent(InsertImeiActivity.this,
									MainActivity.class));
							}	
									
							else	if(val1.equalsIgnoreCase("-3"))
							{
								
							startActivity(new Intent(InsertImeiActivity.this,
									MainActivity.class));
							}
								 
							
						    
							else
							{
								
								insertIMEIDetails1();
							}
						}
						else
							showToastMessage("Wrong Password");
					} else
						showToastMessage("please connect to internet");
				} else
					showToastMessage("please enter valid credentials");

				

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

	private void insertIMEIDetails1() {
	
		
		
       	
	
			
			InsertImeiDetailsTask task = new InsertImeiDetailsTask(IMEI, VTP, DESC);
			taskManager.executeTask(task, task.createRequest(), "Please Wait While Register Device ",
					new OnAsyncTaskCompleteListener<Integer>() {

						@Override
						public void onTaskCompleteSuccess(Integer result) {
							
							if(result==1|| result==-3 )
							{
							com.mantra.mfs100.CommonMethod.setPrefsData(getBaseContext(),com.mantra.mfs100.Constants.LoginValue,result.toString());
							}
							OnIMEIRegister(result);
							
							ShowMessage(result);
						
						}

						@Override
						public void onTaskFailed(Exception cause) {
							
							showToastMessage("An error occured while attempting to invoke the InsertImei web service");
						}
					});
		
	}
	
	void ShowMessage(int errocode)
	{
		if(errocode==-1)
		{
			Toast.makeText(this, "IMEI Number Is Mandatory", Toast.LENGTH_LONG).show();
		}
		
		else if (errocode==-2)
		{
			Toast.makeText(this, "VTP Registration Number Is Mandatory", Toast.LENGTH_LONG).show();
		}
		else if (errocode==-3)
		{
			Toast.makeText(this, "IMEI Number Already Exists", Toast.LENGTH_LONG).show();
		}

		else if (errocode==-4)
		{
			Toast.makeText(this, "Error Occurred", Toast.LENGTH_LONG).show();
		}
		
		else if (errocode==0)
		{
			Toast.makeText(this, "No Record Inserted", Toast.LENGTH_LONG).show();
		}

		else if (errocode==1)
		{
			Toast.makeText(this, "Record Successfully Inserted", Toast.LENGTH_LONG).show();
		}

	}

	private void OnIMEIRegister(Integer resultCode) {
		if (resultCode != null) {
			// Log.i("nik", "Insert IMEI Result Code:" + resultCode.toString());
			if ((resultCode.toString().equals("-3")) || (resultCode == 1 )) {
				Log.i("nik", "Insert IMEI Result Code: in call" + resultCode);
				mdb = new MyDataBase(InsertImeiActivity.this);
				mdb.insertImeiDetails(IMEI, VTP, DESC);

				// mdb.insertImeiDetails("myimei", "20707", "desc");

				//startActivity(new Intent(InsertImeiActivity.this,
						//InsertBatchActivity.class));
				
				
				startActivity(new Intent(InsertImeiActivity.this,
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

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	protected void showInputDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Password");
		alert.setMessage("Enter Master Password");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {

				String value = input.getText().toString();

				masterPassword = value;
				prefs.SavePreferences(Master, masterPassword);

				// Do something with value!
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}

				});

		alert.show();
	}

	protected void showForgotDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Password");
		alert.setMessage("Enter Master Password");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {

				String value = input.getText().toString();

				masterPassword = value;
				String prevMaster = prefs.GetPreferences(Master);
				if (masterPassword.equals(prevMaster)) {
					d = new AlertDialog.Builder(InsertImeiActivity.this)
							.setTitle("MasterPassword")
							.setMessage(
									"password saved by admin is : "
											+ prefs.GetPreferences(Password))
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// continue with delete
											d.dismiss();
											;
										}
									})
							.setNegativeButton(android.R.string.no,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// do nothing
											d.dismiss();
										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				}
				else{
					showToastMessage("Master password entered is wrong");
				}

				// Do something with value!
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}

				});

		alert.show();
	}

}
