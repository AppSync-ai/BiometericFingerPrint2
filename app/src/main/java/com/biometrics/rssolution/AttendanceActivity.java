package com.biometrics.rssolution;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.biometrics.rssolution.Helper.PrefenrenceHelper;
import com.biometrics.rssolution.soapreq.AsyncTaskManager;
import com.biometrics.rssolution.soapreq.OnAsyncTaskCompleteListener;
import com.biometrics.rssolution.soapreq.UpdateAttendenceTask;
import com.biometrics.rssolutions.Gps.GPSTracker;
import com.example.Mydatabse.MyDataBase;
import com.mantra.mfs100.MainActivity;
import com.mantra.mfs100.mfs100api;

public class AttendanceActivity extends Activity implements OnClickListener {
	int StopClick = 0;
	int busy = 1;
	byte[] rawdata;
	byte[] Iso_19794_2_Template;
	byte[] Enroll_Template;
	byte[] Verify_Template;
	byte[] Verify_Template2;

	public static final String PREF_ONE_TIME = "add";

	SharedPreferences statusPref;

	public AsyncTaskManager taskManager;// = new AsyncTaskManager(this);
	String name;
	ArrayList<String> entry;
	TextView txtStatus;
	ImageView user_pic;
	private String EncodedEnroll_Templateone;
	private String EncodedEnroll_Templatetwo;
	TextView regNumEdit, rollnoView, NameView;
	ImageView Finger1;
	Bitmap bi_view = Bitmap.createBitmap(316, 354, Bitmap.Config.ALPHA_8);
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
	Button nextbt; // here
	final String ACTION_USB_PERMISSION = "com.mantra.mfs100.USB_PERMISSION"; // Change
	ImageView one, two, three, four, five, six, seven, eight, nine, zero,
			delete;
	MyDataBase mdb;
	String rollno;
	String textrollno, VTP, BatchId;
	Button init;
	byte[] fp, fp2;
	double latitude, longitude;
	String Date, Time;
	Set<String> pending;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.seventh_layout);
		taskManager = new AsyncTaskManager(this);
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();
		Date = dateFormat.format(date);
		// System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
		pending = new HashSet<String>();
		DateFormat timeformat = new SimpleDateFormat("hh:mm");
		Date time = new Date();
		Time = timeformat.format(time);
		Log.i("Datet", "date " + Date + " time: " + Time);
		
		statusPref = getSharedPreferences(MainActivity.PREF_ONE_TIME, 0);

		txtStatus = (TextView) findViewById(R.id.status);
		user_pic = (ImageView) findViewById(R.id.studentpicview);
		user_pic.setScaleType(ScaleType.FIT_XY);
		mdb = new MyDataBase(this);
		VTP = mdb.getLatestVtp();
		BatchId = SelectBatchActivity.BatchId;
		if (BatchId != null) {
			((TextView) findViewById(R.id.vtptext)).setText(BatchId);
		}
		Log.i("selectb", "batchId :" + BatchId);
		entry = new ArrayList<String>();
		textrollno = "USP/";
		regNumEdit = (TextView) findViewById(R.id.regnumedit);
		rollnoView = (TextView) findViewById(R.id.rollnoview);
		NameView = (TextView) findViewById(R.id.namevaluetext);
		one = (ImageView) findViewById(R.id.numone);
		one.setOnClickListener(this);
		two = (ImageView) findViewById(R.id.numtwo);
		two.setOnClickListener(this);
		three = (ImageView) findViewById(R.id.numthree);
		three.setOnClickListener(this);
		four = (ImageView) findViewById(R.id.numfour);
		four.setOnClickListener(this);
		five = (ImageView) findViewById(R.id.numfive);
		five.setOnClickListener(this);
		six = (ImageView) findViewById(R.id.numsix);
		six.setOnClickListener(this);
		seven = (ImageView) findViewById(R.id.numseven);
		seven.setOnClickListener(this);
		eight = (ImageView) findViewById(R.id.numeight);
		eight.setOnClickListener(this);
		nine = (ImageView) findViewById(R.id.numnine);
		nine.setOnClickListener(this);
		zero = (ImageView) findViewById(R.id.numzero);
		zero.setOnClickListener(this);
		delete = (ImageView) findViewById(R.id.numdelete);
		delete.setOnClickListener(this);
		init = (Button) findViewById(R.id.init);
		GPSTracker gps = new GPSTracker(this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		}

		if (pending.size() > 0) {
			new Timer().scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Iterator<String> itr = pending.iterator();
					while (itr.hasNext()) {
						String id = itr.next();
						ReenrollStudent(id);
					}
				}
			}, 0, 600000);
		}

		Context context = AttendanceActivity.this.getApplicationContext();
		File outputDir = context.getCacheDir();
		Finger1 = (ImageView) findViewById(R.id.finger1);
		createDirIfNotExists(Storagepath);
		Storagepath = outputDir.getPath() + "/";
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		// SetTextonuiThread("Selected MFS Version :- " +
		// settings.getString("MFSVer", String.valueOf(mfsVer)));
		Toast.makeText(this, "Selected MFS Version" + mfsVer, Toast.LENGTH_LONG)
				.show();
		mfsVer = Integer.parseInt(settings.getString("MFSVer",
				String.valueOf(mfsVer)));
		Toast.makeText(this, "Selected MFS Version" + mfsVer, Toast.LENGTH_LONG)
				.show();
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
		init.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mfsVer = Integer.parseInt(settings.getString("MFSVer",
						String.valueOf(mfsVer)));
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
	}

	public void onSave(View v) {
		Toast.makeText(this, "wait ..", Toast.LENGTH_SHORT).show();
	}

	private String readFromFile() {

		// CommonMethods.writeLog("in readFromFile Function");
		String ret = "";
		try {

			File myFile = new File(Environment.getExternalStorageDirectory()
					+ "/MFS100_Fingers/", "Base64EncodedFinger.txt");
			BufferedReader br = null;
			String response = null;
			try {
				StringBuffer output = new StringBuffer();
				String fpath = myFile.toString();
				br = new BufferedReader(new FileReader(fpath));
				String line = "";
				while ((line = br.readLine()) != null) {
					output.append(line + "\n");
				}
				response = output.toString();

			} catch (IOException e) {
				// CommonMethods.writeLog(" readFromFile IOException :-"
				// + e.getMessage());
				return null;
			}
			return response;

		} catch (Exception exw) {
			// CommonMethods.writeLog("in readFromFile Exception:-"
			// + exw.getMessage());

		}
		return ret;
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

	public void ExtractISO(String isoName) {
		// TODO Auto-generated method stub
		byte[] tmp_Template = new byte[2500];
		int ISO_Template_Size = mfs100api.MFS100ExtractISOTemplate(gbldevice,
				rawdata, tmp_Template);
		Iso_19794_2_Template = Arrays.copyOf(tmp_Template, ISO_Template_Size);
		SetTextonuiThread("FMR Template Extracted, Size = " + ISO_Template_Size);
		try {
			FileOutputStream output = new FileOutputStream("/sdcard/MFS100/"
					+ isoName + ".iso");
			output.write(Iso_19794_2_Template);
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
			}
		} catch (Exception ex) {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (entry.size() < 12) {
			switch (v.getId()) {

			case R.id.numone: {
				entry.add("1");
				setEntry(entry);
			}
				break;
			case R.id.numtwo: {
				entry.add("2");
				setEntry(entry);
			}
				break;
			case R.id.numthree: {
				entry.add("3");
				setEntry(entry);
			}
				break;
			case R.id.numfour: {
				entry.add("4");
				setEntry(entry);
			}
				break;
			case R.id.numfive: {
				entry.add("5");
				setEntry(entry);
			}
				break;
			case R.id.numsix: {
				entry.add("6");
				setEntry(entry);
			}
				break;
			case R.id.numseven: {
				entry.add("7");
				setEntry(entry);
			}
				break;
			case R.id.numeight: {
				entry.add("8");
				setEntry(entry);
			}
				break;
			case R.id.numnine: {
				entry.add("9");
				setEntry(entry);
			}
				break;
			case R.id.numzero: {
				entry.add("0");
				setEntry(entry);
			}
				break;
			case R.id.numdelete: {
				// int item = Integer.entry.get(entry.size()-1);
				if (entry.size() != 0) {
					entry.remove(entry.size() - 1);
					setEntry(entry);
				}
			}
				break;
			}
		}
		if (v.getId() == R.id.numdelete && entry.size() >= 12) {
			if (entry.size() != 0) {
				entry.remove(entry.size() - 1);
				setEntry(entry);
			}
		}
	}

	public void setEntry(ArrayList<String> list) {
		String text = "";
		for (int i = 0; i < list.size(); i++) {
			text = text + list.get(i);
		}
		regNumEdit.setText("USP/" + text);
	}


	
	public void onOk(View v) {
		
		try
		{
		rollno = (String) regNumEdit.getText();
		name = mdb.getStudentNameByrollno(rollno);
		String chkBatch = mdb.getStudentBatchIdByrollno(rollno);
		if(chkBatch.equals(BatchId)){
		if (name != null) {
			rollnoView.setText(rollno);
			NameView.setText(name);
			try
			{
			
			String pic = mdb.getStudentPhotoByrollno(rollno);
			
			Log.i("Photo", "photo : " + pic);
                if (pic.length() != 0) {
				
				
				Bitmap photo = StringToBitMap(pic);

				user_pic.setImageBitmap(photo);
				
			}
			}
			catch(Exception dd1){}
			
			
		try
			{
			fp = mdb.getStudentFP1Byrollno(rollno);
			fp2 = mdb.getStudentFP2Byrollno(rollno);
			
			//String MESSAGE = new String(fp2);
			
			//Toast.makeText(this, MESSAGE, Toast.LENGTH_LONG)
			//.show();
			}
		
		catch(Exception fd)
		{
		
		}
			
			 //Log.i("fp", "fp size: "+fp.length+"fp2 size: "+fp2.length  );
			
			 /*if (fp2.length != 0) {
				for (int i = 0; i < fp2.length; i++) {
					Log.i("fp", "fp2" + i + " = " + fp2[i]);
				}*/
			//}

		} else

			Toast.makeText(this, "StudentID does not exist", Toast.LENGTH_LONG)
					.show();

		Log.i("attendance", "rollno: " + rollno);
		}
		else showToastMessage("Student not in this Batch");
		}
		
		catch(Exception ddf)
		{
			showToastMessage("Student not in this Batch");
		}

	}


	private void contineousfingerscan(String isoFileName, final View v) {
		try {
			
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
						
							
							
							
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							
										Finger1.setImageBitmap(bi_view);
										
										

								
							} 


						byte[] tmp_Template = new byte[2500];
						int ISO_Template_Size = mfs100api
								.MFS100ExtractISOTemplate(gbldevice,
										rawdata, tmp_Template);
						
						Enroll_Template = new byte[ISO_Template_Size];
						Enroll_Template = Arrays.copyOf(tmp_Template,
								ISO_Template_Size);
						for (int i = 0; i < Enroll_Template.length; i++) {
							Log.i("fp", "Enroll" + i + " = "
									+ Enroll_Template[i]);
						}
						int retMatc1=0;
						int retMatc = matchPrint(fp, Enroll_Template);
						try
						{
						 retMatc1 = matchPrint(fp2, Enroll_Template);
						}
						catch(Exception dh)
						{}
						if(retMatc>0)
						{
							
							onFingerPrintMatch();
						//showToastMessage(String.valueOf(retMatc).toString());
						
						
						}
						else if(retMatc1>0)
						{
							onFingerPrintMatch();
						}
						else
						{
							showToastMessage("Finger Print not Matched Try Another!");
						}
					
						

						}
					//}
				//}
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

	int count = 0;

	public void onVerify(View v) {

		if (name != null) {
			try {
				contineousfingerscan("EnrollFMR", v);
				
				count++;
			} catch (Exception e) {
			} finally {
				// btnCapture1.setEnabled(true);
				// btnMatchFinger.setEnabled(true);
			}
		} else
			Toast.makeText(AttendanceActivity.this, "Enter a valid StudentID ",
					Toast.LENGTH_LONG).show();
	}

	public int matchPrint(byte[] one, byte[] two) {
		int score;
		Log.i("matchp", "in method matchprint");
		score = mfs100api.MFS100MatchISO(gbldevice, one, two, 180);
		return score;
	}

	public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		int ret = mfs100api.MFS100Uninit(gbldevice);
		if (ret != 0) {
			mfs100api.CheckError(ret);
			SetTextonuiThread(mfs100api.ErrorString);
		} else {
			SetTextonuiThread("Uninit Success");
			gbldevice = 0;

		}

		super.onStop();
	}

	protected void showInputDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Password");
		alert.setMessage("Enter Password");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {

				String value = input.getText().toString();
				String password = new PrefenrenceHelper(AttendanceActivity.this)
						.GetPreferences("password");
				Log.i("pass", "pass: " + password);
				if (value.equals(password)) {
					finish();
				} else {
					Toast.makeText(AttendanceActivity.this, " Wrong Password",
							Toast.LENGTH_SHORT).show();
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showInputDialog();
	}

	private void enrollStudent() {

	
		UpdateAttendenceTask task = new UpdateAttendenceTask(rollno, VTP,
				BatchId, Date, Time, String.valueOf(longitude),
				String.valueOf(latitude));
		taskManager.executeTask(task, task.createRequest(), "Enroll Student  ",
				new OnAsyncTaskCompleteListener<String>() {

					@Override
					public void onTaskCompleteSuccess(String result) {
						// updateForecastDetails(result);

						Log.i("ebytes", " inserting in database print : "
								+ EncodedEnroll_Templateone);
						
						((ImageView) findViewById(R.id.dotverifyview))
								.setBackgroundResource(R.drawable.verified);
						
						
						if(String.valueOf(latitude).equalsIgnoreCase("0"))
						{
							showToastMessage("Please ON GPS  ");
						}
						
						if(String.valueOf(longitude).equalsIgnoreCase("0"))
						{
							showToastMessage("Please ON GPS  ");
						}
						showToastMessage(ErrorMessage(result)+"Location Is :"+ String.valueOf(latitude) +"& "+String.valueOf(longitude));
						//showToastMessage(result);
						
						onEnrollStudentResult(result);
						regNumEdit.setText("USP/" );
						
					}

					@Override
					public void onTaskFailed(Exception cause) {
						// Log.e(TAG, cause.getMessage(), cause);
						showToastMessage("An error occured while attempting update attendance");
					}
				});
		// }
	}

	
	String  ErrorMessage(String Result)
	{
		String Res="";
		if(Result.equalsIgnoreCase("1"))
		{
			Res="Record Successfully Inserted";
		}
		
		else if(Result.equalsIgnoreCase("-1"))
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
			Res="Attendance date format is not valid";
		}
		
		else if(Result.equalsIgnoreCase("-5"))
		{
			Res="Attendance time format is not valid";
		}
		
		else if(Result.equalsIgnoreCase("-6"))
		{
			Res="Longititude value is not correct";
		}
		
		else if(Result.equalsIgnoreCase("-7"))
		{
			Res="Latitude value is not correct";
		}
		
		else if(Result.equalsIgnoreCase("-8"))
		{
			Res="Student Not Exist For Given VTP And Batch";
		}
		
		else if(Result.equalsIgnoreCase("-9"))
		{
			Res="Student ID Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-10"))
		{
			Res="VTP Registration Number Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-11"))
		{
			Res="BatchId Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-12"))
		{
			Res="Attendance Date Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-13"))
		{
			Res="Attendance Time Is Mandatory";
		}
		else if(Result.equalsIgnoreCase("-14"))
		{
			Res="Longititude Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-15"))
		{
			Res="Latitude Is Mandatory";
		}
		
		else if(Result.equalsIgnoreCase("-16"))
		{
			Res="Error Occurred";
		}
		
		else if(Result.equalsIgnoreCase("0"))
		{
			Res="No Record Inserted";
		}
		
		else if(Result.equalsIgnoreCase("-17"))
		{
			Res="Wrong Batch Time";
		}
		
		return Res;
	}
	
	private void ReenrollStudent(String id) {

		// Log.i("enroll", "VTP :" + VTP + " BatchId :" + BatchId
		// + " stringphoto :" + stringPhoto + " p1 :"
		// + EncodedEnroll_Templateone);
		UpdateAttendenceTask task = new UpdateAttendenceTask(id, VTP, BatchId,
				Date, Time, String.valueOf(longitude), String.valueOf(latitude));
		taskManager.executeTask(task, task.createRequest(),
				"Updating Attendance  ",
				new OnAsyncTaskCompleteListener<String>() {

					@Override
					public void onTaskCompleteSuccess(String result) {
						// updateForecastDetails(result);

						Log.i("ebytes", " inserting in database print : "
								+ EncodedEnroll_Templateone);
					

						showToastMessage("Enrolled Successfully");
						onEnrollStudentResult(result);
						// Log.i("nik", "Insert IMEI Result Code:" + result);
						// Log.i("nik","id :"+result.getState());
					}

					@Override
					public void onTaskFailed(Exception cause) {
						// Log.e(TAG, cause.getMessage(), cause);
						showToastMessage("An error occured while attempting to invoke the enroll web service");
					}
				});
		// }
	}

	private void onEnrollStudentResult(String resultCode) {
		if (resultCode != null) {
			// Log.i("nik", "Insert IMEI Result Code:" + resultCode.toString());
			if ((resultCode.equals("1"))) {
				// Log.i("nik", "Insert IMEI Result Code: in call" +
				// resultCode);
				// mdb = new MyDataBase(this);
				// mdb.insertImeiDetails("myimei", "10707", "desc");
				// mdb.insertImeiDetails("myimei", "20707", "desc");
				//
			} else if (resultCode.equals("0")) {
				pending.add(rollno);
			}
		}
	}

	private void showToastMessage(String messageId) {
		Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
	}

	public void onFingerPrintMatch() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showToastMessage("Finger print Matched");
				mdb.insertDateTime(rollno, Date, Time, BatchId, VTP, name);
				Log.i("selectb", "BatchId : " + BatchId);
				enrollStudent();
			}
		});
	}

}
