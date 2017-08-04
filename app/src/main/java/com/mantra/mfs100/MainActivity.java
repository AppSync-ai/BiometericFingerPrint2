package com.mantra.mfs100;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.util.EncodingUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;


import com.biometrics.rssolution.BatchManager;
import com.biometrics.rssolution.HelpActivity;
import com.biometrics.rssolution.InsertBatchActivity;
import com.biometrics.rssolution.InsertImeiActivity;
import com.biometrics.rssolution.R;
import com.biometrics.rssolution.ReportActivity;
import com.biometrics.rssolution.SelectBatchActivity;
import com.biometrics.rssolution.UserDetailActivity;
import com.biometrics.rssolution.models.CityForecastBO;
import com.biometrics.rssolution.models.GPS;
import com.biometrics.rssolution.soapreq.AsyncTaskManager;
import com.biometrics.rssolution.soapreq.EnrollStudentTask;
import com.biometrics.rssolution.soapreq.GetCityForcastByZipCodeTask;
import com.biometrics.rssolution.soapreq.OnAsyncTaskCompleteListener;
import com.biometrics.rssolutions.Gps.GPSTracker;
import com.example.Mydatabse.MyDataBase;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {
	ImageView save;
	private static final int CAMERA_REQUEST = 0;
	protected static final int USERDETAILS = 0;
	protected static final int ENROLL = 1;
	ImageView connectDevice, batchManager, userDetail, enrolluser,
			makeAttendane, helpDesk, report;
	Button einit;
	int StopClick = 0;
	int busy = 1;
	byte[] rawdata;
	byte[] Iso_19794_2_Template;
	byte[] Enroll_Template;
	byte[] Enroll_Template2;
	byte[] Verify_Template;
	public boolean init = false;

	String EncodedEnroll_Templateone, EncodedEnroll_Templatetwo, sendingFP1,
			sendingFP2;
	public static String stringPhoto;
	TextView txtStatus, enrolltext, namevalue, enrollstatus;
	Bitmap bi_view = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
	UsbManager mManager;
	UsbDevice mDevice;
	UsbDeviceConnection mDeviceConnection;
	UsbInterface mInterface;
	PendingIntent mPermissionIntent = null;
	long gbldevice = 0;
	int gblquality = 0;
	SharedPreferences settings;
	int mfsVer = 0;
	String fileName = "";
	String imageEncoded = "";
	String Storagepath = "/sdcard/MFS100/"; // mfs100.hex file will be stored
	MyDataBase mdb; // here
	final String ACTION_USB_PERMISSION = "com.mantra.mfs100.USB_PERMISSION"; // Change
	RelativeLayout mainthird, include;
	AsyncTaskManager taskManager;
	private Dialog dialog;
	private ImageView firstThumb;
	private ImageView SecondThumb;
	public static ImageView picview;
	String studentName, rollNo, VTP, BatchId, ImeiNo;
	public static boolean userinserted;
	public static ArrayList<CityForecastBO> StudentList;
	TextView batterylevel, LocValue, imeitextview, vtptext, vtptextf;
	GPSTracker gps;
	private String id;

	public static final String PREF_ONE_TIME = "add";

	SharedPreferences statusPref;

	SharedPreferences.Editor editer;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_layout);

		taskManager = new AsyncTaskManager(this);
		batterylevel = (TextView) findViewById(R.id.battvaluetext);
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		LocValue = (TextView) findViewById(R.id.locvaluetext);
		vtptext = (TextView) findViewById(R.id.vtptext);
		vtptext.setText(new MyDataBase(this).getLatestVtp());
		enrollstatus = (TextView) findViewById(R.id.enrollstatus);
		gps = new GPSTracker(this);
		statusPref = getSharedPreferences(MainActivity.PREF_ONE_TIME, 0);
		editer = statusPref.edit();
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
		
			// String loc = gps.getLocation().toString();
			Geocoder gcd = new Geocoder(this, Locale.getDefault());
			List<Address> addresses = null;
			try {
				addresses = gcd.getFromLocation(latitude, longitude, 1);
				if (addresses.size() > 0)
					// System.out.println(addresses.get(0).getLocality());
					LocValue.setText(addresses.get(0).getAddressLine(0));
				editer.putString("location", addresses.get(0).getAddressLine(0)
						+ "lat is " + addresses.get(0));
				editer.commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// Toast.makeText(this, "location not found",
				// Toast.LENGTH_SHORT).show();
				double latitude1 = new GPS(MainActivity.this).getLatitude();
				double longitude1 = new GPS(MainActivity.this).getLongitude();
				Log.d("loc", "value of latitude is" + latitude1
						+ "value of longitude " + longitude1);
				Toast.makeText(
						this,
						"value of latitude is" + latitude1
								+ "value of longitude " + longitude1,
						Toast.LENGTH_SHORT).show();
				// String loc = gps.getLocation().toString();
				Geocoder gcd1 = new Geocoder(this, Locale.getDefault());
				List<Address> addresses1 = null;
				try {
					addresses1 = gcd1.getFromLocation(latitude, longitude, 1);
					if (addresses1.size() > 0)
						// System.out.println(addresses.get(0).getLocality());
						LocValue.setText(addresses1.get(0).getAddressLine(0));
					editer.putString("location", addresses1.get(0)
							.getAddressLine(0));
					editer.commit();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}

		} else {
			Toast.makeText(this, "location cannot be found", Toast.LENGTH_SHORT)
					.show();
		}
		TelephonyManager mngr = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
		ImeiNo = mngr.getDeviceId();
		imeitextview = (TextView) findViewById(R.id.imeivaluetext);
		imeitextview.setText(ImeiNo);
		mainthird = (RelativeLayout) findViewById(R.id.thirdmain);
		include = (RelativeLayout) findViewById(R.id.include);
		include.setVisibility(View.INVISIBLE);
		connectDevice = (ImageView) findViewById(R.id.connectdevice);
		connectDevice.setOnClickListener(this);
		connectDevice.setVisibility(View.GONE);
		batchManager = (ImageView) findViewById(R.id.batchmanager);
		batchManager.setOnClickListener(this);
		userDetail = (ImageView) findViewById(R.id.userdetails);
		userDetail.setOnClickListener(this);
		enrolluser = (ImageView) findViewById(R.id.enrolluser);

		((ImageView) findViewById(R.id.batchview))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(MainActivity.this,
								InsertBatchActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					}
				});
		enrolluser.setOnClickListener(this);
		enrolltext = (TextView) findViewById(R.id.enrolltext);
		namevalue = (TextView) findViewById(R.id.namevaluetext);
		makeAttendane = (ImageView) findViewById(R.id.attendence);

		makeAttendane.setOnClickListener(this);
		helpDesk = (ImageView) findViewById(R.id.helpview);

		helpDesk.setOnClickListener(this);
		report = (ImageView) findViewById(R.id.report);
		report.setOnClickListener(this);
try
{
		picview = (ImageView) findViewById(R.id.studentpicview);
}

catch(Exception ere)
{
	
}
		Context context = this.getApplicationContext();
		File outputDir = context.getCacheDir();
		txtStatus = (TextView) findViewById(R.id.status);
		VTP = new MyDataBase(this).getLatestVtp();
		BatchId = new MyDataBase(this).getLatestBatchId();
		Log.i("dba", "value of VTP and BatchId :" + VTP + " " + BatchId);

		vtptextf = (TextView) findViewById(R.id.vtptextfifth);
		if (getIntent().getStringExtra("user") != null) {
			Log.i("Thumb", "From enroll page");
			mainthird.setVisibility(View.INVISIBLE);
			include.setVisibility(View.VISIBLE);
			vtptextf.setText(new MyDataBase(this).getLatestVtp());
			studentName = getIntent().getStringExtra("StudentName");
			rollNo = getIntent().getStringExtra("rollno");
			enrolltext.setText(rollNo);
			namevalue.setText(studentName);
			try
			{
			String flag = new MyDataBase(this).getStudentPhotoByrollno(rollNo);
			}
			catch(Exception eer)
			{}
			//if (flag.length() > 10) {
				//enrollstatus.setText("Student Already enrolled");
			//}
			if (init == true) {
				int ret = mfs100api.MFS100Uninit(gbldevice);
				if (ret != 0) {
					mfs100api.CheckError(ret);
					SetTextonuiThread(mfs100api.ErrorString);
				} else {
					SetTextonuiThread("Uninit Success");
					gbldevice = 0;
					Log.i("Thumb", "uninit success");
				}
			}

			createDirIfNotExists(Storagepath);
			Storagepath = outputDir.getPath() + "/";
			settings = PreferenceManager.getDefaultSharedPreferences(this);
			Toast.makeText(this, "Selected MFS Version" + mfsVer,
					Toast.LENGTH_SHORT).show();
			mfsVer = Integer.parseInt(settings.getString("MFSVer",
					String.valueOf(mfsVer)));
			Toast.makeText(this, "Selected MFS Version" + mfsVer,
					Toast.LENGTH_SHORT).show();

			byte[] a = new byte[316 * 354];
			long w = 316;
			long h = 354;
			WriteBmp316_354(a, w, h);
			// Finger1.refreshDrawableState();
			a = null;

			// int ver = mfs100api.MFS100GetVersion();
			// SetTextonuiThread("MFS100API Version =  " + String.valueOf(ver));

			mManager = ((UsbManager) getSystemService(Context.USB_SERVICE));
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_USB_PERMISSION);
			filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
			filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
			registerReceiver(mUsbReceiver, filter);

			// for On create init

			HashMap<String, UsbDevice> deviceList = mManager.getDeviceList();
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			// Searching MFS100
			Boolean MFS100Found = false;
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				long productid = device.getProductId();
				long vendorid = device.getVendorId();
				if (productid == 34323 && vendorid == 1204) {

					PendingIntent mPermissionIntent = null;
					mPermissionIntent = PendingIntent.getBroadcast(this, 0,
							new Intent(ACTION_USB_PERMISSION), 0);
					mManager.requestPermission(device, mPermissionIntent);
					MFS100Found = true;
					SetTextonuiThread("MFS100 Found with default VID and PID...");
					break;
				} else if (productid == 4101 && vendorid == 1204) {
					PendingIntent mPermissionIntent = null;
					mPermissionIntent = PendingIntent.getBroadcast(this, 0,
							new Intent(ACTION_USB_PERMISSION), 0);
					mManager.requestPermission(device, mPermissionIntent);
					MFS100Found = true;
					SetTextonuiThread("MFS100 Found with Mantra VID and PID...");
					break;
				} else {

					SetTextonuiThread("MFS100 Fingerprint Sensor not Found");

				}
			}
			if (MFS100Found == false) {

				SetTextonuiThread("MFS100 Fingerprint Sensor not Found");

			}

		}

		einit = (Button) findViewById(R.id.einit);
		einit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				{
					mfsVer = Integer.parseInt(settings.getString("MFSVer",
							String.valueOf(mfsVer)));
					Log.i("init", "init clicked");
				}
				if (gbldevice == 0) {
					try {
						if (FindDeviceAndRequestPermission(34323, 1204) == 0) {
							// SetTextonuiThread("FingerPrint Scanner Found with Default VID and PID...");

						} else if (FindDeviceAndRequestPermission(4101, 1204) == 0) {
							// SetTextonuiThread("FingerPrint Scanner Found with Mantra VID and PID...");
						} else {
						}

						return;
					} catch (Exception ex) {
					}
				}
			}
		});

		save = (ImageView) findViewById(R.id.saveview);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// // TODO Auto-generated method stub
				// byte[] b = EncodedEnroll_Templateone.getBytes();
				// for(int i=0;i<b.length;i++){
				// Log.i("ebytes"," valu : "+i+" "+b[i]);
				// }
				
			
				
				if (rollNo != null && VTP != null && BatchId != null
						 && sendingFP1 != null
						&& sendingFP2 != null )
						 {
					//Log.i("ebytes", "Enroll_template size: "
							//+ Enroll_Template.length
							//+ " Enroll_template2 size: "
							//+ Enroll_Template2.length);
					enrollStudent();
					
		
					
				} else
				{
					Toast.makeText(MainActivity.this,
							" please enter all the details", Toast.LENGTH_LONG)
							.show();}
			}
		});
		try
		{
		picview.setScaleType(ScaleType.FIT_XY);
		}
		catch(Exception ee){}
		picview.setOnClickListener(new View.OnClickListener() {

			private File f = new File(Environment.getExternalStorageDirectory()
					.toString() + "/camera/");
			private String camera = "Front";

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent cameraIntent = new Intent(
				// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				Intent picintent = new Intent(getApplicationContext(),
						CamActivity.class);
				startActivity(picintent);
				/*
				 * cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				 * Uri.fromFile(f)); // Log.v("", "Camera Id-" + camId);
				 * 
				 * if (!TextUtils.isEmpty(camera)) { if
				 * (camera.equalsIgnoreCase("Front")) { Log.v("", "Inside if");
				 * cameraIntent.putExtra( "android.intent.extras.CAMERA_FACING",
				 * Camera.CameraInfo.CAMERA_FACING_FRONT);
				 * 
				 * } else { Log.v("", "Inside else"); cameraIntent.putExtra(
				 * "android.intent.extras.CAMERA_FACING",
				 * Camera.CameraInfo.CAMERA_FACING_BACK);
				 * 
				 * } }
				 */
				//startActivityForResult(cameraIntent, CAMERA_REQUEST);

			}
		});

		String vtp = new MyDataBase(this).getLatestVtp();
		Log.i("dba", "vtp :" + vtp);

		firstThumb = (ImageView) findViewById(R.id.firstthumb);
		SecondThumb = (ImageView) findViewById(R.id.secondthumb);
		Log.i("Thumb", "thumb initialized in oncreate");
		firstThumb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.i("Thumb", "first thumb clicked");
				
				try {
					
					
					CAPTURE("EnrollFMR", firstThumb);
				} catch (Exception e) {
				} finally {
					
				}
			}
		});

		SecondThumb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("secondThumb", "secondThumb clicked");

				try {
					// btnCapture1.setEnabled(false);
					// btnMatchFinger.setEnabled(false);
					CAPTURE("EnrollFMR", SecondThumb);
				} catch (Exception e) {
				} finally {
					// btnCapture1.setEnabled(true);
					// btnMatchFinger.setEnabled(true);
				}
			}
		});

		// scanner initiation code starts

		// SetTextonuiThread("Selected MFS Version :- " +
		// settings.getString("MFSVer", String.valueOf(mfsVer)));

		// scanner initiation code ends

		// init code starts

		// TODO Auto-generated method stub

		// {
		// mfsVer = Integer.parseInt(settings.getString("MFSVer",
		// String.valueOf(mfsVer)));
		// Log.i("init", "init clicked");
		// }
		// if (gbldevice == 0) {
		// try {
		// if (FindDeviceAndRequestPermission(34323, 1204) == 0) {
		// //
		// SetTextonuiThread("FingerPrint Scanner Found with Default VID and PID...");
		//
		// } else if (FindDeviceAndRequestPermission(4101, 1204) == 0) {
		// //
		// SetTextonuiThread("FingerPrint Scanner Found with Mantra VID and PID...");
		// } else {
		// }
		//
		// return;
		// } catch (Exception ex) {
		// }
		// }

		// init code ends

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.connectdevice: {
			// new MainActivity().Init_Sensor();
			{
				mfsVer = Integer.parseInt(settings.getString("MFSVer",
						String.valueOf(mfsVer)));
				Log.i("init", "init clicked");
			}
			if (gbldevice == 0) {
				try {
					if (FindDeviceAndRequestPermission(34323, 1204) == 0) {
						// SetTextonuiThread("FingerPrint Scanner Found with Default VID and PID...");

					} else if (FindDeviceAndRequestPermission(4101, 1204) == 0) {
						// SetTextonuiThread("FingerPrint Scanner Found with Mantra VID and PID...");
					} else {
					}

					return;
				} catch (Exception ex) {
				}
			}
		}
			break;
		case R.id.batchmanager: {

			// if (!userinserted)
			getCityForcast(10);
			// else
			//
			// startActivity(new Intent(MainActivity.this, BatchManager.class));

		}
			break;
		case R.id.userdetails: {
			if (init == true) {
				int ret = mfs100api.MFS100Uninit(gbldevice);
				if (ret != 0) {
					mfs100api.CheckError(ret);
					SetTextonuiThread(mfs100api.ErrorString);
				} else {
					SetTextonuiThread("Uninit Success");
					gbldevice = 0;
					init = false;
					Log.i("Thumb", "uninit success");
				}
			}
			// if (!userinserted)
			getCityForcast(USERDETAILS);
			// else
			//
			// startActivity(new Intent(MainActivity.this,
			// UserDetailActivity.class));
			// startActivity(new
			// Intent(MainActivity.this,UserDetailActivity.class));
		}
			break;
		case R.id.enrolluser: {
			// startActivity(new
			// Intent(MainActivity.this,EnrollUserActivity.class));
			// mainthird.setVisibility(View.INVISIBLE);
			// include.setVisibility(View.VISIBLE);
			if (init == true) {
				int ret = mfs100api.MFS100Uninit(gbldevice);
				if (ret != 0) {
					mfs100api.CheckError(ret);
					SetTextonuiThread(mfs100api.ErrorString);
				} else {
					SetTextonuiThread("Uninit Success");
					gbldevice = 0;
					init = false;
					Log.i("Thumb", "uninit success");
				}
			}
			// if (!userinserted)
			getCityForcast(ENROLL);

			// else
			//
			// startActivity(new Intent(MainActivity.this,
			// UserDetailActivity.class).putExtra("enroll", "enroll"));
		}
			break;
		case R.id.attendence: {
			if (init == true) {
				int ret = mfs100api.MFS100Uninit(gbldevice);
				if (ret != 0) {
					mfs100api.CheckError(ret);
					SetTextonuiThread(mfs100api.ErrorString);
				} else {
					SetTextonuiThread("Uninit Success");
					gbldevice = 0;
					Log.i("Thumb", "uninit success");
					init = false;
				}
			}

			startActivity(new Intent(MainActivity.this,
					SelectBatchActivity.class));
		}
			break;
		case R.id.helpview: {
			startActivity(new Intent(MainActivity.this, HelpActivity.class));

		}
			break;
		case R.id.report: {
			startActivity(new Intent(MainActivity.this, ReportActivity.class));
		}
			break;

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (taskManager == null) {
			taskManager = new AsyncTaskManager(this);
		}
	}

	private void getCityForcast(final int value) {
		// if (StringUtils.isEmpty(zipCode)) {
		// Builder builder = new Builder(this);
		// builder.setTitle("Missing ZIP code");
		// builder.setMessage("Please specify the ZIP code and try again.");
		// builder.setIcon(android.R.drawable.ic_dialog_alert);
		//
		// builder.setPositiveButton("OK", null);
		//
		// displayNewDialog(builder);
		// } else {
		GetCityForcastByZipCodeTask task = new GetCityForcastByZipCodeTask(
				ImeiNo, VTP, BatchId);
		taskManager.executeTask(task, task.createRequest(),
				"Getting Students Details ",
				new OnAsyncTaskCompleteListener<ArrayList<CityForecastBO>>() {

					@Override
					public void onTaskCompleteSuccess(
							ArrayList<CityForecastBO> result) {
						// updateForecastDetails(result);

						Log.i("nik", "name:" + result.get(0).getCity());
						Log.i("nik", "id :" + result.get(0).getState());
						Log.i("nik", "size :" + result.size());
						StudentList = result;
						for (int i = 0; i < result.size(); i++) {
							new MyDataBase(MainActivity.this)
									.insertStudentDetails(BatchId, VTP, result
											.get(i).getState(), result.get(i)
											.getCity());
							//
						}
						if (value == USERDETAILS) {
							startActivity(new Intent(MainActivity.this,
									UserDetailActivity.class));
							userinserted = true;
						}
						if (value == ENROLL) {

							startActivity(new Intent(MainActivity.this,
									UserDetailActivity.class).putExtra(
									"enroll", "enroll"));
							userinserted = true;
						}
						if (value == 10) {
							startActivity(new Intent(MainActivity.this,
									BatchManager.class));
							userinserted = true;

						}
					}

					@Override
					public void onTaskFailed(Exception cause) {
						// Log.e(TAG, cause.getMessage(), cause);
						showToastMessage("Please Create Batch First !");
					}
				});
		// }
	}

	private void showToastMessage(String messageId) {
		Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
	}

	private void dismissDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	int FindDeviceAndRequestPermission(int PID, int VID) {
		int rs = 0;
		try {
			HashMap<String, UsbDevice> deviceList = mManager.getDeviceList();
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				long productid = device.getProductId();
				long vendorid = device.getVendorId();
				if (productid == PID && vendorid == VID) {
					PendingIntent mPermissionIntent = null;
					mPermissionIntent = PendingIntent.getBroadcast(this, 0,
							new Intent(ACTION_USB_PERMISSION), 0);
					mManager.requestPermission(device, mPermissionIntent);
					rs = 0;
					return rs;
				}
			}
			rs = -1;
			return rs;
		} catch (Exception ex) {
			return -1;
		}
	}

	public void load_firmware() {
		try {
			int fd = mDeviceConnection.getFileDescriptor();
			long ret = mfs100api.MFS100LoadFirmware(fd, Storagepath,
					mfsVer);

			if (ret == 0) {
				// Firmware Successfully Loaded
				SetTextonuiThread("Load Firmware Sucess");
				Toast.makeText(this, "Firmware loaded", Toast.LENGTH_LONG)
						.show();
				mDeviceConnection.close();
				if (FindDeviceAndRequestPermission(4101, 1204) == 0) {
					SetTextonuiThread("MFS100 Scanner Found with Mantra VID and PID...");
				} else {
					SetTextonuiThread("MFS100 Scanner Not Found");
				}
			} else {
				// Firmware Not loaded
				Toast.makeText(this, "Firmware not loaded", Toast.LENGTH_LONG)
						.show();
				SetTextonuiThread("Firmware Not Loaded. Error Value:- "
						+ String.valueOf(ret));
			}
		} catch (Exception ex) {

		}
	}

	void SetTextonuiThread(final String str) {

		txtStatus.post(new Runnable() {
			@Override
			public void run() {

				// Editable tmp;
				// tmp = txtStatus.getText();
				// tmp = tmp.append(str + "\n");
				// txtStatus.setText(tmp, BufferType.EDITABLE);
				txtStatus.setText(str, BufferType.EDITABLE);

			}
		});
	}

	public static boolean createDirIfNotExists(String path) {
		boolean ret = true;

		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				ret = false;
			}
		}
		return ret;
	}

	BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				SetTextonuiThread("Device Attached");
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				busy = 0;
				if (gbldevice != 0) {
					mfs100api.MFS100StopXcan(gbldevice);
					mfs100api.MFS100Uninit(gbldevice);
					gbldevice = 0;
					// btnCapture1.setEnabled(false);
					// btnStopCapture.setEnabled(false);
					// btnExtract.setEnabled(false);
					// btnInit.setEnabled(true);
					// btnUninit.setEnabled(false);
					// btnMatchFinger.setEnabled(false);
					SetTextonuiThread("Device Removed");

				}
			} else if (ACTION_USB_PERMISSION.equals(action)) {

				synchronized (this) {
					UsbDevice device = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);

					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {
							if (device.getProductId() == 34323) {
								// Loading Firmware..
								mDeviceConnection = mManager.openDevice(device);
								mInterface = device.getInterface(0);
								load_firmware();
							} else if (device.getProductId() == 4101) {
								// Initializing Sensor..
								mDeviceConnection = mManager.openDevice(device);
								mInterface = device.getInterface(0);
								Init_Sensor();
							}
						}
					} else {
						try {
							Toast.makeText(context, "Permission Denied",
									Toast.LENGTH_LONG).show();
						} catch (Exception ex) {
						}
					}
				}
			}

		}
	};

	public void Init_Sensor() {
		try {
			int fd = mDeviceConnection.getFileDescriptor();

			byte[] serial = new byte[8];
			long devicevar = 0;
			devicevar = mfs100api
					.MFS100Init(80, 16384, Storagepath, fd, serial);

			int ret = mfs100api.MFS100GetInitErrorCode();
			if (ret != 0) {
				mfs100api.CheckError(ret);
				SetTextonuiThread(mfs100api.ErrorString);
				return;
			} else {
				gbldevice = devicevar;
			}
			String str = EncodingUtils.getAsciiString(serial, 0, 7);
			SetTextonuiThread("Serial No of Device is " + str);

			SetTextonuiThread("gbldevice = " + gbldevice);
			if (gbldevice == 0) {
				mfs100api.CheckError((int) gbldevice);
				SetTextonuiThread(mfs100api.ErrorString);
			} else {
				SetTextonuiThread("Sensor Initialized.., Proceed for Scan...");
				// btnCapture1.setEnabled(true);
				// btnStopCapture.setEnabled(true);
				// btnExtract.setEnabled(true);
				// btnInit.setEnabled(false);
				// btnUninit.setEnabled(true);
				// btnMatchFinger.setEnabled(true);
				init = true;
			}
		} catch (Exception ex) {
		}

	}

	public void WriteBmp316_354(byte[] bytearray, long width, long height) {

		Bitmap bi = Bitmap.createBitmap(316, 354, Bitmap.Config.RGB_565);

		for (int i = 0; i < 354; i++) {
			for (int j = 0; j < 316; j++) {
				int pixelAlpha = 255;
				int red = Color.red(bytearray[j + (i * 316)]);
				int green = Color.green(bytearray[j + (i * 316)]);
				int blue = Color.blue(bytearray[j + (i * 316)]);
				int new_pixel = Color.argb(pixelAlpha, red, green, blue);
				bi.setPixel(j, 353 - i, new_pixel);
			}
		}
		// Finger1.setImageBitmap(bi);
	}
	private void contineousfingerscan(String isoFileName, final View v) {
		try {
			// CommonMethods.writeLog("1");
			bi_view = Bitmap.createBitmap(316, 354, Bitmap.Config.ALPHA_8);
			StopClick = 0;
			gblquality = 0;
			if (isoFileName == "EnrollFMR") {
				fileName = "EnrollFMR";
			} else {
				fileName = "VerifyFMR";
			}
			rawdata = new byte[316 * 354];
		
			// CommonMethods.writeLog("2");
			new Thread(new Runnable() {
				@Override
				public void run() {
					int ret = 0;
					ret = mfs100api.MFS100StartXcan(gbldevice, 0, 0);
					if (ret != 0) {
						mfs100api.CheckError(ret);
						SetTextonuiThread(mfs100api.ErrorString);
						return;
					}
					int busy = 1;
					int Quality = 0;

					while (busy == 1) {
						ret = mfs100api.MFS100GetPreviewBitmapData(gbldevice,
								rawdata, bi_view);
						if (ret != 0) {
							mfs100api.CheckError(ret);
							SetTextonuiThread(mfs100api.ErrorString);
							return;
						}

						
						Quality = mfs100api.MFS100GetQuality(gbldevice);
						// CommonMethods.writeLog("3");
						if (Quality == mfs100api.MFS100_E_ALREADY_START_STOP) {
							SetTextonuiThread("MFS100 Stopped from Timeout");
							StopClick = 1;
							return;
						}
						
						
						if (Quality >= 50 || StopClick == 1) {
							gblquality = Quality;

							ret = mfs100api.MFS100StopXcan(gbldevice);
							busy = 0;
						
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							
							ret = mfs100api.MFS100GetFinalBitmapData(gbldevice,
									rawdata, bi_view);
							// CommonMethods.writeLog("5");
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							//if (v.getId() == R.id.firstthumb) {
								firstThumb.post(new Runnable() {

									@Override
									public void run() {
										firstThumb.setImageBitmap(bi_view);
									}
								});
							//}
							//else {
								//SecondThumb.post(new Runnable() {

									//@Override
									//public void run() {
									//	SecondThumb.setImageBitmap(bi_view);
									//}
								//});
							//}

							byte[] tmp_Template = new byte[2500];
							int ISO_Template_Size = mfs100api
									.MFS100ExtractISOTemplate(gbldevice,
											rawdata, tmp_Template);

							if (fileName == "EnrollFMR") {

								Enroll_Template = new byte[ISO_Template_Size];
								Enroll_Template = Arrays.copyOf(tmp_Template,
										ISO_Template_Size);
								for (int i = 0; i < Enroll_Template.length; i++) {
									Log.i("eeby", "enroll template size : " + i
											+ " " + Enroll_Template[i]);
								}

								try {
									String root = Environment
											.getExternalStorageDirectory()
											.toString()
											+ "/MFS100_Fingers/";

									File fl = new File(root);
									if (!fl.exists()) {
										fl.mkdirs();
									}
									//if (v.getId() == R.id.firstthumb) {
										sendingFP1 = Base64
												.encodeToString(
														Enroll_Template,
														Base64.DEFAULT);
										EncodedEnroll_Templateone = new String(
												Enroll_Template, "UTF-8");
										 new String(Enroll_Template);
											showToastMessage(sendingFP1);
										 
									//}
									
									//if (v.getId() == R.id.secondthumb) {
									//	sendingFP2 = Base64
										//		.encodeToString(
										//				Enroll_Template,
											//			Base64.DEFAULT);
										//// EncodedEnroll_Templatetwo = new
										//// String(Enroll_Template);
										//Enroll_Template2 = Enroll_Template;
									//}

									File myFile4 = new File(Environment
											.getExternalStorageDirectory()
											+ "/MFS100_Fingers/",
											"EnrolledISOTemplate.txt");
									if (!myFile4.exists())
										myFile4.createNewFile();
									FileOutputStream fos4;
									fos4 = new FileOutputStream(myFile4);
									fos4.write(EncodedEnroll_Templateone
											.getBytes());
									fos4.flush();
									fos4.close();

									Bitmap bi = null;
									bi = Bitmap.createBitmap(
											bi_view.getWidth(),
											bi_view.getHeight(),
											Bitmap.Config.ARGB_8888);
									Canvas c = new Canvas(bi);
									Paint paint = new Paint();
									ColorMatrix cm = new ColorMatrix();
									cm.setSaturation(0);
									paint.setFilterBitmap(true);
									ColorMatrixColorFilter f = new ColorMatrixColorFilter(
											cm);
									paint.setColorFilter(f);
									c.drawBitmap(bi_view, 0, 0, paint);
									ByteArrayOutputStream stream = new ByteArrayOutputStream();
									if (bi != null) {
										if (bi.compress(
												Bitmap.CompressFormat.PNG, 100,
												stream)) {
											byte[] array = stream.toByteArray();

											File flb1 = new File(root);
											if (!flb1.exists()) {
												flb1.mkdirs();
											}
											File myFileb1 = new File(
													Environment
															.getExternalStorageDirectory()
															+ "/MFS100_Fingers/",
													"OriginalBitmapFingerImage.png");
											if (!myFileb1.exists())
												myFileb1.createNewFile();
											FileOutputStream fos;
											fos = new FileOutputStream(myFileb1);
											fos.write(array);
											fos.flush();
											fos.close();

											String EncodedImageString = Base64
													.encodeToString(array,
															Base64.DEFAULT);

											File myFile3 = new File(
													Environment
															.getExternalStorageDirectory()
															+ "/MFS100_Fingers/",
													"Base64EncodedFinger.txt");
											if (!myFile3.exists())
												myFile3.createNewFile();
											FileOutputStream fos3;
											fos3 = new FileOutputStream(myFile3);
											fos3.write(EncodedImageString
													.getBytes());
											fos3.flush();
											fos3.close();
										}
									}

								} catch (Exception ex) { // TODO
									// CommonMethods.writeLog("Exception : "
									// + ex.toString());
									ex.printStackTrace();
								}
								SetTextonuiThread("Finger Captured Successfully");
							} else {

								Verify_Template = new byte[ISO_Template_Size];
								Verify_Template = Arrays.copyOf(tmp_Template,
										ISO_Template_Size);
								int retMatch = mfs100api.MFS100MatchISO(
										gbldevice, Verify_Template,
										Enroll_Template, 180);
								SetTextonuiThread("FingerPrint Matching score: "
										+ String.valueOf(retMatch));
							}
						}
					}
				}
			}).start();
		} catch (Exception ex) {
			// CommonMethods.writeLog("Exception in ContinuesScan(). Message:- "
			// + ex.getMessage());
		} /*
		 * finally { btnCapture1.post(new Runnable() {
		 * 
		 * @Override public void run() { btnCapture1.setEnabled(true); } });
		 * btnMatchFinger.post(new Runnable() {
		 * 
		 * @Override public void run() { btnMatchFinger.setEnabled(true); } } );
		 */

		// btnMatchFinger.setEnabled(true);

	}
	
	private void CAPTURE(String isoFileName, final View v) {
		try {
			// CommonMethods.writeLog("1");
			bi_view = Bitmap.createBitmap(316, 354, Bitmap.Config.ALPHA_8);
			StopClick = 0;
			gblquality = 0;
			if (isoFileName == "EnrollFMR") {
				fileName = "EnrollFMR";
			} else {
				fileName = "VerifyFMR";
			}
			rawdata = new byte[316 * 354];
			
			
					int ret = 0;
					ret = mfs100api.MFS100StartXcan(gbldevice, 0, 0);
					if (ret != 0) {
						mfs100api.CheckError(ret);
						SetTextonuiThread(mfs100api.ErrorString);
						return;
					}
					int busy = 1;
					int Quality = 0;
					
					while (busy == 1) {
						ret = mfs100api.MFS100GetPreviewBitmapData(gbldevice,
								rawdata, bi_view);
						if (ret != 0) {
							mfs100api.CheckError(ret);
							SetTextonuiThread(mfs100api.ErrorString);
							return;
						}

						
						Quality = mfs100api.MFS100GetQuality(gbldevice);
						// CommonMethods.writeLog("3");
						if (Quality == mfs100api.MFS100_E_ALREADY_START_STOP) {
							SetTextonuiThread("MFS100 Stopped from Timeout");
							StopClick = 1;
							return;
						}
						if (Quality >= 50 || StopClick == 1) {
							gblquality = Quality;

							ret = mfs100api.MFS100StopXcan(gbldevice);
							busy = 0;
						
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							
							ret = mfs100api.MFS100GetFinalBitmapData(gbldevice,
									rawdata, bi_view);
							// CommonMethods.writeLog("5");
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							if (v.getId() == R.id.firstthumb) {
								
									firstThumb.setImageBitmap(bi_view);
									
									byte[] tmp_Template = new byte[2500];
									int ISO_Template_Size = mfs100api
											.MFS100ExtractISOTemplate(gbldevice,
													rawdata, tmp_Template);
									
									Enroll_Template = new byte[ISO_Template_Size];
									Enroll_Template = Arrays.copyOf(tmp_Template,
											ISO_Template_Size);
									for (int i = 0; i < Enroll_Template.length; i++) {
										Log.i("eeby", "enroll template size : " + i
												+ " " + Enroll_Template[i]);
									}
									
									
									sendingFP1 = Base64
											.encodeToString(
													Enroll_Template,
													Base64.DEFAULT);
									EncodedEnroll_Templateone = new String(
											Enroll_Template, "UTF-8");
									 new String(Enroll_Template);
									 
									//  showToastMessage(sendingFP1);
									 
								
							}
							else {
							
										SecondThumb.setImageBitmap(bi_view);
										
										
										
										byte[] tmp_Template = new byte[2500];
										int ISO_Template_Size = mfs100api
												.MFS100ExtractISOTemplate(gbldevice,
														rawdata, tmp_Template);
										
										Enroll_Template2 = new byte[ISO_Template_Size];
										Enroll_Template2 = Arrays.copyOf(tmp_Template,
												ISO_Template_Size);
										for (int i = 0; i < Enroll_Template2.length; i++) {
											Log.i("eeby", "enroll template2 size : " + i
													+ " " + Enroll_Template2[i]);
										}
										
										
										sendingFP2 = Base64
												.encodeToString(
														Enroll_Template2,
														Base64.DEFAULT);
										 EncodedEnroll_Templatetwo = new
										 String(Enroll_Template2);
										 
										 //showToastMessage(sendingFP2);
										 
										 
									
							}

							//byte[] tmp_Template = new byte[2500];
							//int ISO_Template_Size = mfs100api
								//	.MFS100ExtractISOTemplate(gbldevice,
											//rawdata, tmp_Template);

							//

								//Enroll_Template = new byte[ISO_Template_Size];
								// = Arrays.copyOf(tmp_Template,
									//	ISO_Template_Size);
								//for (int i = 0; i < Enroll_Template.length; i++) {
								//	Log.i("eeby", "enroll template size : " + i
								//			+ " " + Enroll_Template[i]);
								//}

							//try {
									//String root = Environment
											//.getExternalStorageDirectory()
										//	.toString()
										//	+ "/MFS100_Fingers/";

									//File fl = new File(root);
									//if (!fl.exists()) {
									//	fl.mkdirs();
								//	}
									//if (v.getId() == R.id.firstthumb) {
									//	sendingFP1 = Base64
											//	.encodeToString(
									//				//	Enroll_Template,
														//Base64.DEFAULT);
										//EncodedEnroll_Templateone = new String(
										//		Enroll_Template, "UTF-8");
										// new String(Enroll_Template);
										 
										// showToastMessage(sendingFP1);
									//}
									
								//	if (v.getId() == R.id.secondthumb) {
										//sendingFP2 = Base64
										//		.encodeToString(
														//Enroll_Template,
									//					Base64.DEFAULT);
										// EncodedEnroll_Templatetwo = new
										 //String(Enroll_Template);
										 
										 //showToastMessage(sendingFP2);
										//Enroll_Template2 = Enroll_Template2;
									//}

									//File myFile4 = new File(Environment
											//.getExternalStorageDirectory()
											//+ "/MFS100_Fingers/",
										//	"EnrolledISOTemplate.txt");
									//if (!myFile4.exists())
									//	myFile4.createNewFile();
									//FileOutputStream fos4;
									//fos4 = new FileOutputStream(myFile4);
								//	fos4.write(EncodedEnroll_Templateone
									//		.getBytes());
									//fos4.flush();
									//fos4.close();

									//Bitmap bi = null;
									//bi = Bitmap.createBitmap(
										//	bi_view.getWidth(),
											//bi_view.getHeight(),
											//Bitmap.Config.ARGB_8888);
									//Canvas c = new Canvas(bi);
									//Paint paint = new Paint();
									//ColorMatrix cm = new ColorMatrix();
									//cm.setSaturation(0);
									//paint.setFilterBitmap(true);
									//ColorMatrixColorFilter f = new ColorMatrixColorFilter(
									///		cm);
									//paint.setColorFilter(f);
									//c.drawBitmap(bi_view, 0, 0, paint);
									//ByteArrayOutputStream stream = new ByteArrayOutputStream();
									//if (bi != null) {
										//if (bi.compress(
											//	Bitmap.CompressFormat.PNG, 100,
											//	stream)) {
											//byte[] array = stream.toByteArray();

											//File flb1 = new File(root);
											//if (!flb1.exists()) {
											//	flb1.mkdirs();
											//}
											//File myFileb1 = new File(
													//Environment
														//	.getExternalStorageDirectory()
														//	+ "/MFS100_Fingers/",
												//	"OriginalBitmapFingerImage.png");
											//if (!myFileb1.exists())
												//myFileb1.createNewFile();
											//FileOutputStream fos;
											//fos = new FileOutputStream(myFileb1);
											//fos.write(array);
											//fos.flush();
											//fos.close();

										//	String EncodedImageString = Base64
													//.encodeToString(array,
															//Base64.DEFAULT);

										//	File myFile3 = new File(
												//	Environment
													//		.getExternalStorageDirectory()
													//		+ "/MFS100_Fingers/",
												//	"Base64EncodedFinger.txt");
											//if (!myFile3.exists())
												//myFile3.createNewFile();
											//FileOutputStream fos3;
											//fos3 = new FileOutputStream(myFile3);
											//fos3.write(EncodedImageString
												//	.getBytes());
											//fos3.flush();
											//fos3.close();
										//}
									//}

								//} catch (Exception ex) { // TODO
									// CommonMethods.writeLog("Exception : "
									// + ex.toString());
									//ex.printStackTrace();
								//}
								//SetTextonuiThread("Finger Captured Successfully");
							//} else {

								//Verify_Template = new byte[ISO_Template_Size];
								//Verify_Template = Arrays.copyOf(tmp_Template,
									//	ISO_Template_Size);
								//int retMatch = mfs100api.MFS100MatchISO(
									//	gbldevice, Verify_Template,
									//	Enroll_Template, 180);
								//SetTextonuiThread("FingerPrint Matching score: "
									//	+ String.valueOf(retMatch));
							//}
						}
					//}
				}
			//}).start();
		} catch (Exception ex) {
			// CommonMethods.writeLog("Exception in ContinuesScan(). Message:- "
			// + ex.getMessage());
		} /*
		 * finally { btnCapture1.post(new Runnable() {
		 * 
		 * @Override public void run() { btnCapture1.setEnabled(true); } });
		 * btnMatchFinger.post(new Runnable() {
		 * 
		 * @Override public void run() { btnMatchFinger.setEnabled(true); } } );
		 */

		// btnMatchFinger.setEnabled(true);

	}
	
	
	private void contineousfingerscan1(String isoFileName, final View v) {
		try {
			// CommonMethods.writeLog("1");
			bi_view = Bitmap.createBitmap(316, 354, Bitmap.Config.ALPHA_8);
			StopClick = 0;
			gblquality = 0;
			if (isoFileName == "EnrollFMR") {
				fileName = "EnrollFMR";
			} else {
				fileName = "VerifyFMR";
			}
			rawdata = new byte[316 * 354];
			// showToastMessage("Ram pal");
			// CommonMethods.writeLog("2");
			new Thread(new Runnable() {
				@Override
				public void run() {
					int ret = 0;
					ret = mfs100api.MFS100StartXcan(gbldevice, 0, 0);
					if (ret != 0) {
						mfs100api.CheckError(ret);
						SetTextonuiThread(mfs100api.ErrorString);
						return;
					}
					int busy = 1;
					int Quality = 0;

					while (busy == 1) {
						ret = mfs100api.MFS100GetPreviewBitmapData(gbldevice,
								rawdata, bi_view);
						if (ret != 0) {
							mfs100api.CheckError(ret);
							SetTextonuiThread(mfs100api.ErrorString);
							return;
						}

						
						Quality = mfs100api.MFS100GetQuality(gbldevice);
						// CommonMethods.writeLog("3");
						if (Quality == mfs100api.MFS100_E_ALREADY_START_STOP) {
							SetTextonuiThread("MFS100 Stopped from Timeout");
							StopClick = 1;
							return;
						}
						if (Quality >= 50 || StopClick == 1) {
							gblquality = Quality;

							ret = mfs100api.MFS100StopXcan(gbldevice);
							busy = 0;
						
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							
							ret = mfs100api.MFS100GetFinalBitmapData(gbldevice,
									rawdata, bi_view);
							// CommonMethods.writeLog("5");
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							if (v.getId() == R.id.firstthumb) {
								firstThumb.post(new Runnable() {

									@Override
									public void run() {
									firstThumb.setImageBitmap(bi_view);
									}
								});
							}
							else {
								SecondThumb.post(new Runnable() {

									@Override
									public void run() {
										SecondThumb.setImageBitmap(bi_view);
									}
								});
							}

							byte[] tmp_Template = new byte[2500];
							int ISO_Template_Size = mfs100api
									.MFS100ExtractISOTemplate(gbldevice,
											rawdata, tmp_Template);

							if (fileName == "EnrollFMR") {

								Enroll_Template = new byte[ISO_Template_Size];
								Enroll_Template = Arrays.copyOf(tmp_Template,
										ISO_Template_Size);
								for (int i = 0; i < Enroll_Template.length; i++) {
									Log.i("eeby", "enroll template size : " + i
											+ " " + Enroll_Template[i]);
								}

								try {
									String root = Environment
											.getExternalStorageDirectory()
											.toString()
											+ "/MFS100_Fingers/";

									File fl = new File(root);
									if (!fl.exists()) {
										fl.mkdirs();
									}
									if (v.getId() == R.id.firstthumb) {
										sendingFP1 = Base64
												.encodeToString(
														Enroll_Template,
														Base64.DEFAULT);
										EncodedEnroll_Templateone = new String(
												Enroll_Template, "UTF-8");
										 new String(Enroll_Template);
										 
										 showToastMessage(sendingFP1);
									}
									
									if (v.getId() == R.id.secondthumb) {
										sendingFP2 = Base64
												.encodeToString(
														Enroll_Template2,
														Base64.DEFAULT);
										 EncodedEnroll_Templatetwo = new
										 String(Enroll_Template);
										 
										 showToastMessage(sendingFP1);
										//Enroll_Template2 = Enroll_Template2;
									}

									File myFile4 = new File(Environment
											.getExternalStorageDirectory()
											+ "/MFS100_Fingers/",
											"EnrolledISOTemplate.txt");
									if (!myFile4.exists())
										myFile4.createNewFile();
									FileOutputStream fos4;
									fos4 = new FileOutputStream(myFile4);
									fos4.write(EncodedEnroll_Templateone
											.getBytes());
									fos4.flush();
									fos4.close();

									Bitmap bi = null;
									bi = Bitmap.createBitmap(
											bi_view.getWidth(),
											bi_view.getHeight(),
											Bitmap.Config.ARGB_8888);
									Canvas c = new Canvas(bi);
									Paint paint = new Paint();
									ColorMatrix cm = new ColorMatrix();
									cm.setSaturation(0);
									paint.setFilterBitmap(true);
									ColorMatrixColorFilter f = new ColorMatrixColorFilter(
											cm);
									paint.setColorFilter(f);
									c.drawBitmap(bi_view, 0, 0, paint);
									ByteArrayOutputStream stream = new ByteArrayOutputStream();
									if (bi != null) {
										if (bi.compress(
												Bitmap.CompressFormat.PNG, 100,
												stream)) {
											byte[] array = stream.toByteArray();

											File flb1 = new File(root);
											if (!flb1.exists()) {
												flb1.mkdirs();
											}
											File myFileb1 = new File(
													Environment
															.getExternalStorageDirectory()
															+ "/MFS100_Fingers/",
													"OriginalBitmapFingerImage.png");
											if (!myFileb1.exists())
												myFileb1.createNewFile();
											FileOutputStream fos;
											fos = new FileOutputStream(myFileb1);
											fos.write(array);
											fos.flush();
											fos.close();

											String EncodedImageString = Base64
													.encodeToString(array,
															Base64.DEFAULT);

											File myFile3 = new File(
													Environment
															.getExternalStorageDirectory()
															+ "/MFS100_Fingers/",
													"Base64EncodedFinger.txt");
											if (!myFile3.exists())
												myFile3.createNewFile();
											FileOutputStream fos3;
											fos3 = new FileOutputStream(myFile3);
											fos3.write(EncodedImageString
													.getBytes());
											fos3.flush();
											fos3.close();
										}
									}

								} catch (Exception ex) { // TODO
									// CommonMethods.writeLog("Exception : "
									// + ex.toString());
									ex.printStackTrace();
								}
								SetTextonuiThread("Finger Captured Successfully");
							} else {

								Verify_Template = new byte[ISO_Template_Size];
								Verify_Template = Arrays.copyOf(tmp_Template,
										ISO_Template_Size);
								int retMatch = mfs100api.MFS100MatchISO(
										gbldevice, Verify_Template,
										Enroll_Template, 180);
								SetTextonuiThread("FingerPrint Matching score: "
										+ String.valueOf(retMatch));
							}
						}
					}
				}
			}).start();
		} catch (Exception ex) {
			// CommonMethods.writeLog("Exception in ContinuesScan(). Message:- "
			// + ex.getMessage());
		} /*
		 * finally { btnCapture1.post(new Runnable() {
		 * 
		 * @Override public void run() { btnCapture1.setEnabled(true); } });
		 * btnMatchFinger.post(new Runnable() {
		 * 
		 * @Override public void run() { btnMatchFinger.setEnabled(true); } } );
		 */

		// btnMatchFinger.setEnabled(true);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (include.getVisibility() == View.VISIBLE) {

			// startActivity(new Intent(MainActivity.this,Use));
			include.setVisibility(View.INVISIBLE);
			mainthird.setVisibility(View.VISIBLE);
			if (getIntent().getStringExtra("batch") == null)
				startActivity(new Intent(MainActivity.this,
						UserDetailActivity.class).putExtra("enroll", "enroll"));
			finish();

		} else {
			if (init == true) {
				int ret = mfs100api.MFS100Uninit(gbldevice);
				if (ret != 0) {
					mfs100api.CheckError(ret);
					SetTextonuiThread(mfs100api.ErrorString);
				} else {
					SetTextonuiThread("Uninit Success");
					gbldevice = 0;
					Log.i("Thumb", "uninit success");
				}

			}
			startActivity(new Intent(MainActivity.this,
					InsertImeiActivity.class));
			
			finish();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// int ret = mfs100api.MFS100Uninit(gbldevice);
		// if (ret != 0) {
		// mfs100api.CheckError(ret);
		// SetTextonuiThread(mfs100api.ErrorString);
		// } else {
		// SetTextonuiThread("Uninit Success");
		// gbldevice = 0;
		//
		// }

		super.onPause();
	}

	@Override
	public void onStop() {
		int ret = mfs100api.MFS100Uninit(gbldevice);
		if (ret != 0) {
			mfs100api.CheckError(ret);
			SetTextonuiThread(mfs100api.ErrorString);
		} else {
			SetTextonuiThread("Uninit Success");
			gbldevice = 0;

		}

		super.onStop();
		// finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dismissDialog();
		// int ret = 0;
		// ret = mfs100api.MFS100Uninit(gbldevice);
		// if (ret != 0) {
		// mfs100api.CheckError(ret);
		// SetTextonuiThread(mfs100api.ErrorString);
		// } else {
		// SetTextonuiThread("Uninit Success");
		// gbldevice = 0;
		//
		// }
		// unregisterReceiver(mUsbReceiver);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
			photo = (Bitmap) data.getExtras().get("data");
			stringPhoto = BitMapToString(photo);
			picview.setImageBitmap(photo);
		}
	}

	private void enrollStudent1()
	{
		Log.i("enroll", "VTP :" + VTP + " BatchId :" + BatchId
				+ " stringphoto :" + stringPhoto + " p1 :"
				+ EncodedEnroll_Templateone);
		
		//rollNo, VTP, BatchId,
		//stringPhoto, sendingFP1, sendingFP2
		
		 String NAMESPACE1 = "http://tempuri.org/";
		  String URL1= "http://www.sdi.gov.in/sdipaservice/dgetservice.asmx?op=UpdateStudentDetails";
		   String SOAP_ACTION1 = "http://tempuri.org/UpdateStudentDetails";
		  String USER_LOGIN_METHOD_NAME1 = "UpdateStudentDetails";
		   SoapObject response1;
		  
		  
		  try {
				
				
				
			

				Log.e("StudentID", rollNo);
				Log.e("VTPRegNo", VTP);
				Log.e("BatchID", BatchId);
				Log.e("StudentPhoto", stringPhoto);
				Log.e("StudentFingerSampleOne", sendingFP1);
				Log.e("StudentFingerSampleSecond", sendingFP2);
				
				
				
				SoapObject request = new SoapObject(NAMESPACE1,USER_LOGIN_METHOD_NAME1);

				PropertyInfo fromProp = new PropertyInfo();
				fromProp.setName("StudentID");
				fromProp.setValue(rollNo);
				fromProp.setType(String.class);
				request.addProperty(fromProp);

				PropertyInfo toProp = new PropertyInfo();
				toProp.setName("VTPRegNo");
				toProp.setValue(VTP);
				toProp.setType(String.class);
				request.addProperty(toProp);
				
				PropertyInfo Mobile = new PropertyInfo();
				Mobile.setName("BatchID");
				Mobile.setValue(BatchId);
				Mobile.setType(String.class);
				request.addProperty(Mobile);
				
				PropertyInfo Mobile1 = new PropertyInfo();
				Mobile1.setName("StudentPhoto");
				Mobile1.setValue(stringPhoto);
				Mobile1.setType(String.class);
				request.addProperty(Mobile1);
				
				PropertyInfo Mobile2 = new PropertyInfo();
				Mobile2.setName("StudentFingerSampleOne");
				Mobile2.setValue(sendingFP1);
				Mobile2.setType(String.class);
				request.addProperty(Mobile2);
				
				PropertyInfo Mobile3 = new PropertyInfo();
				Mobile3.setName("StudentFingerSampleSecond");
				Mobile3.setValue(sendingFP2);
				Mobile3.setType(String.class);
				request.addProperty(Mobile3);
				
				
				
			
				Toast.makeText(getApplicationContext(), "Sharad", Toast.LENGTH_LONG).show();
				
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
			
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1,12000);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION1, envelope);

				SoapObject result = (SoapObject)envelope.bodyIn;

				response1 = (SoapObject) envelope.getResponse();
				
				
				Toast.makeText(getApplicationContext(), "Sharad1", Toast.LENGTH_LONG).show();
			
				System.out.println("string"+response1);
			
				
			

				
				

			} catch (Exception e) {
			e.printStackTrace();
				
			}
		  
	}
	
	
	String ShowEror(String errorcode)
	{
		String val2="";
		if(errorcode.equalsIgnoreCase("1"))
		{
			val2="Record Successfully Updated";
		}
		
		if(errorcode.equalsIgnoreCase("0"))
		{
			val2="Record Not Found";
		}
		
		else if(errorcode.equalsIgnoreCase("-1"))
		{
			val2="Student Id not found or incorrect";
		}
		else if(errorcode.equalsIgnoreCase("-2"))
		{
			val2="VTPID not found or incorrect";
		}
		else if(errorcode.equalsIgnoreCase("-3"))
		{
			val2="Batch Id not found or Incorrect";
		}
		else if(errorcode.equalsIgnoreCase("-4"))
		{
			val2="Student photo was not in correct format";
		}
		else if(errorcode.equalsIgnoreCase("-5"))
		{
			val2="Student finger print 1 image is not in correct format";
		}
		
		else if(errorcode.equalsIgnoreCase("-6"))
		{
			val2="Student finger print 2 image is not in correct format";
		}
		
		else if(errorcode.equalsIgnoreCase("-7"))
		{
			val2="Student Not Exist For Given VTP And Batch";
		}
		
		else if(errorcode.equalsIgnoreCase("-8"))
		{
			val2="Given First Finger Already Enrolled";
		}
		
		else if(errorcode.equalsIgnoreCase("-9"))
		{
			val2="Given Second Finger Already Enrolled";
		}
		
		
		else if(errorcode.equalsIgnoreCase("-10"))
		{
			val2="Student ID Is Mandatory";
		}
		
		else if(errorcode.equalsIgnoreCase("-11"))
		{
			val2="VTP Registration Number Is Mandatory";
		}
		
		else if(errorcode.equalsIgnoreCase("-12"))
		{
			val2="BatchId Is Mandatory";
		}
		
		else if(errorcode.equalsIgnoreCase("-13"))
		{
			val2="Student First Finger Is Mandatory";
		}
		
		else if(errorcode.equalsIgnoreCase("-14"))
		{
			val2="Student Seconrd Finger Is Mandatory";
		}
		
		else if(errorcode.equalsIgnoreCase("-15"))
		{
			val2="Student First And Second Finger Are Same";
		}
		
		else if(errorcode.equalsIgnoreCase("-16"))
		{
			val2="Error Occurred";
		}
		
		
		
		return val2;
	}
	private void enrollStudent() {

		Log.i("enroll", "VTP :" + VTP + " BatchId :" + BatchId
				+ " stringphoto :" + stringPhoto + " p1 :"
				+ EncodedEnroll_Templateone);
		EnrollStudentTask task = new EnrollStudentTask(rollNo, VTP, BatchId,
				stringPhoto, sendingFP1, sendingFP2);
		taskManager.executeTask(task, task.createRequest(), "Enroll Student  ",
				new OnAsyncTaskCompleteListener<String>() {

					@Override
					public void onTaskCompleteSuccess(String result) {
					

						
						//onEnrollStudentResult(result);
	                      showToastMessage("Enrolled Successfully");
	                      
	                      //showToastMessage(result);
							try
							{
	                   	String ramp=com.mantra.mfs100.Constants.EnrollStatus;
	                   	//showToastMessage(ramp);
	        			//ShowEror(ramp);
							
	        			com.mantra.mfs100.Constants.EnrollStatus="0";
							}
							
							catch(Exception ddf)
							{
								
							}
						Log.i("ebytes", " inserting in database print : "
								+ EncodedEnroll_Templateone);
						
						new MyDataBase(MainActivity.this).insertFingerPrints(
								BatchId, VTP, Enroll_Template,
								Enroll_Template2, rollNo);
						new MyDataBase(MainActivity.this).insertPhoto(rollNo,
								stringPhoto);

						
						
						 Log.i("nik", "Insert IMEI Result Code:" + result);
						// Log.i("nik","id :"+result.getState());
					}

					@Override
					public void onTaskFailed(Exception cause) {
						// Log.e(TAG, cause.getMessage(), cause);
						//showToastMessage("An error occured while attempting to invoke the enroll web service");
					}
				});
		// }
	}

	private void onEnrollStudentResult(String resultCode) {
		
		//showToastMessage(showerror(resultCode));
		if (resultCode != null) {
			// Log.i("nik", "Insert IMEI Result Code:" + resultCode.toString());
			if ((resultCode.equals("1"))) {
			
				Log.i("nik", "Insert IMEI Result Code: in call" + resultCode);
				// mdb = new MyDataBase(this);
				// mdb.insertImeiDetails("myimei", "10707", "desc");
				// mdb.insertImeiDetails("myimei", "20707", "desc");
				//
			}
		}
	}
	
	String showerror(String Result)
	{
		String Res="";
		
		if(Result.equalsIgnoreCase("-1"))
		{
			Res="Student Id not found or incorrect";
		}
		
		else if(Result.equalsIgnoreCase("-2"))
		{
			Res="VTPID not found or incorrect";
		}
		
		else if(Result.equalsIgnoreCase("-3"))
		{
			Res="Batch Id not found or Incorrect";
		}
		
		else if(Result.equalsIgnoreCase("-4"))
		{
			Res="Student photo was not in correct format";
		}
		
		else if(Result.equalsIgnoreCase("-5"))
		{
			Res="Student finger print 1 image is not in correct format";
		}
		
		
		
		else if(Result.equalsIgnoreCase("-6"))
		{
			Res="Student finger print 2 image is not in correct format";
		}
		
		else if(Result.equalsIgnoreCase("-7"))
		{
			Res="Student Not Exist For Given VTP And Batch";
		}
		
		
		
		else if(Result.equalsIgnoreCase("-8"))
		{
			Res="Given First Finger Already Enrolled";
		}
		
		else if(Result.equalsIgnoreCase("-9"))
		{
			Res="Given Second Finger Already Enrolled";
		}
		
		else if(Result.equalsIgnoreCase("-10"))
		{
			Res="Student ID Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-11"))
		{
			Res="VTP Registration Number Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-12"))
		{
			Res="BatchId Is Mandatory";
		}
		

		else if(Result.equalsIgnoreCase("-13"))
		{
			Res="Student First Finger Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-14"))
		{
			Res="Student Seconrd Finger Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-15"))
		{
			Res="Student First And Second Finger Are Same";
		}
		
		else if(Result.equalsIgnoreCase("-16"))
		{
			Res="Error Occurred";
		}
		
		else if(Result.equalsIgnoreCase("0"))
		{
			Res="Record Not Found";
		}
		
		else if(Result.equalsIgnoreCase("1"))
		{
			Res="Record Successfully Updated";
		}
		
		return Res;
	}

	public static String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			batterylevel.setText(String.valueOf(level) + "%");
		}
	};

}
