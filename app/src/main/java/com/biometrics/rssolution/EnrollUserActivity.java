package com.biometrics.rssolution;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.util.EncodingUtils;

import com.example.Mydatabse.MyDataBase;
import com.mantra.mfs100.mfs100api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

public class EnrollUserActivity extends Activity {
	ImageView firstThumb,SecondThumb;
	int StopClick = 0;
	int busy = 1;
	byte[] rawdata;
	byte[] Iso_19794_2_Template;
	byte[] Enroll_Template;
	byte[] Verify_Template;

	TextView txtStatus;
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
	MyDataBase mdb;										// here
	final String ACTION_USB_PERMISSION = "com.mantra.mfs100.USB_PERMISSION"; // Change

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.fifth_layout);
		
		firstThumb = (ImageView)findViewById(R.id.firstthumb);
		SecondThumb = (ImageView)findViewById(R.id.userdetails);
		
firstThumb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
//					btnCapture1.setEnabled(false);
//					btnMatchFinger.setEnabled(false);
					contineousfingerscan("EnrollFMR");
				} catch (Exception e) {
				} finally {
//					btnCapture1.setEnabled(true);
//					btnMatchFinger.setEnabled(true);
				}
			}
		});
	
		//scanner initiation code starts
		
		txtStatus = (TextView)findViewById(R.id.statusn);
		txtStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				
				
				{
				mfsVer = Integer.parseInt(settings.getString("MFSVer",
						String.valueOf(mfsVer)));
				Log.i("init","init clicked");
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
				
				Context context = this.getApplicationContext();
				File outputDir = context.getCacheDir();

				
				
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
				//Finger1.refreshDrawableState();
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
				//scanner initiation code ends
				
				//init code starts
				
					// TODO Auto-generated method stub
				
				
					{
					mfsVer = Integer.parseInt(settings.getString("MFSVer",
							String.valueOf(mfsVer)));
					Log.i("init","init clicked");
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

			// init code ends
				
					Init_Sensor();
		
		
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
//					btnCapture1.setEnabled(false);
//					btnStopCapture.setEnabled(false);
//					btnExtract.setEnabled(false);
//					btnInit.setEnabled(true);
//					btnUninit.setEnabled(false);
//					btnMatchFinger.setEnabled(false);
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
//				btnCapture1.setEnabled(true);
//				btnStopCapture.setEnabled(true);
//				btnExtract.setEnabled(true);
//				btnInit.setEnabled(false);
//				btnUninit.setEnabled(true);
//				btnMatchFinger.setEnabled(true);
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
		//Finger1.setImageBitmap(bi);
	}
	
	private void contineousfingerscan(String isoFileName) {
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

						firstThumb.post(new Runnable() {
							@Override
							public void run() {
								firstThumb.setImageBitmap(bi_view);
								// WriteBmp316_354_Gray(rawdata, 316, 354);
							}
						});

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
							// CommonMethods.writeLog("4");
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							// Get FinalFrame Data
							ret = mfs100api.MFS100GetFinalBitmapData(gbldevice,
									rawdata, bi_view);
							// CommonMethods.writeLog("5");
							if (ret != 0) {
								mfs100api.CheckError(ret);
								SetTextonuiThread(mfs100api.ErrorString);
								return;
							}
							firstThumb.post(new Runnable() {
								@Override
								public void run() {
									firstThumb.setImageBitmap(bi_view);
								}
							});
							byte[] tmp_Template = new byte[2500];
							int ISO_Template_Size = mfs100api
									.MFS100ExtractISOTemplate(gbldevice,
											rawdata, tmp_Template);

							if (fileName == "EnrollFMR") {

								Enroll_Template = new byte[ISO_Template_Size];
								Enroll_Template = Arrays.copyOf(tmp_Template,
										ISO_Template_Size);

								try {
									String root = Environment
											.getExternalStorageDirectory()
											.toString()
											+ "/MFS100_Fingers/";

									File fl = new File(root);
									if (!fl.exists()) {
										fl.mkdirs();
									}

									String EncodedEnroll_Template = Base64
											.encodeToString(Enroll_Template,
													Base64.DEFAULT);

									File myFile4 = new File(Environment
											.getExternalStorageDirectory()
											+ "/MFS100_Fingers/",
											"EnrolledISOTemplate.txt");
									if (!myFile4.exists())
										myFile4.createNewFile();
									FileOutputStream fos4;
									fos4 = new FileOutputStream(myFile4);
									fos4.write(EncodedEnroll_Template
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
//									CommonMethods.writeLog("Exception : "
//											+ ex.toString());
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
//			CommonMethods.writeLog("Exception in ContinuesScan(). Message:- "
//					+ ex.getMessage());
		} /*finally {
			btnCapture1.post(new Runnable() {

				@Override
				public void run() {
					btnCapture1.setEnabled(true);
				}
			});
			btnMatchFinger.post(new Runnable() {

				@Override
				public void run() {
					btnMatchFinger.setEnabled(true);
				}
			}
			);*/

			// btnMatchFinger.setEnabled(true);
		
	}


}
