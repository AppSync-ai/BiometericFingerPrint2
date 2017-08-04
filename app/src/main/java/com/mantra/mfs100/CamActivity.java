package com.mantra.mfs100;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.biometrics.rssolution.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CamActivity extends Activity implements OnClickListener {
	private int w_display, h_display;
	private DisplayMetrics metrics;
	private ResizableCameraPreview fPreview;
	private RelativeLayout flayout;
	private Button btn;
	public static Bitmap bitmap;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cam_layout);
		metrics = getResources().getDisplayMetrics();
		h_display = metrics.heightPixels;
		w_display = metrics.widthPixels;
		flayout = (RelativeLayout) findViewById(R.id.rlFront);
		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(this);
		createCameraPreviewFront(flayout, 1);
	}

	private void createCameraPreviewFront(RelativeLayout mLayout, int cameraId) {

		fPreview = new ResizableCameraPreview(this,
				CameraPreview.LayoutMode.FitToParent, false, w_display - 30,
				h_display / 2, getCameraInstance(cameraId));
		LayoutParams previewLayoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		previewLayoutParams.addRule(Gravity.CENTER);
		mLayout.addView(fPreview, 0, previewLayoutParams);

	}

	@SuppressWarnings("deprecation")
	public Camera getCameraInstance(int cameraId) {
		Camera c = null;

		try {
			c = Camera.open(cameraId); // attempt to get a Camera instance

		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		if (c == null)
			Log.e("aj", "camera returned is  null");
		else {
			Log.e("aj", "camera returned is  not null");

		}
		// parameters = c.getParameters();
		return c; // returns null if camera is unavailable
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		@Override
		public void onShutter() {
			Log.d("aj", "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("aj", "onPictureTaken - raw");
		}
	};
	public PictureCallback mpicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("aj", "this is onPictureTaken");
			try {
				ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
						data);
				Log.d("aj", "data is not null");
				Log.d("aj", "localByteInputStraem is not null");
                // bitmap=Bitmap.createBitmap(200, 150, Config.ARGB_8888);
				bitmap = BitmapFactory.decodeStream(localByteArrayInputStream);
				bitmap=Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				bitmap = rotate(bitmap, -90);
				MainActivity.stringPhoto = MainActivity.BitMapToString(bitmap);
				MainActivity.picview.setImageBitmap(bitmap);
				finish();
			
				// DeBug.ShowLogD("isFirsttime in onPicturetaken" +
				// isFirsttime);

				//url = writeBitmapToFileNew(bitmap, "temp1.png");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	@Override
	public void onClick(View v) {
		fPreview.mCamera.takePicture(shutterCallback, rawCallback, mpicture);
	}

	public static String writeBitmapToFileNew(Bitmap bitmap, String filename)
			throws Exception {

		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/AndroidTest");

		dir.mkdirs();
		File file = new File(dir, filename);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the
																// bitmap object
		Log.d("aj", "before FileOutputStream..");
		FileOutputStream f = new FileOutputStream(file);
		f.write(baos.toByteArray());
		Log.d("aj", "before flush()..");
		f.flush();
		f.close();
		String url = sdCard.getAbsolutePath() + "/AndroidTest/" + filename;
		return url;

	}
	public Bitmap rotate(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, false);
	}
}
