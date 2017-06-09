package com.dan.criminalint;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = "CrimeCameraFragment";
	public static final String EXTRA_PHOTO_FILENAME = "CrimeCameraFragment.photo_filename";
	public static final String EXTRA_PHOTO_ORITATION = "CrimeCameraFragment.photo_oritation";
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private View mProgressContainer;
	private SensorManager mSensorManager;
	private Sensor mAcceleSensor;
	private Sensor mMagneticSensor;
	private float[] acceleValues = new float[3];
	private float[] magneticValues = new float[3];
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			String filename = UUID.randomUUID().toString() + ".jpg";
			int oritation = 0;
			FileOutputStream os = null;
			boolean success = true;

			try {
				os = getActivity().openFileOutput(filename,
						Context.MODE_PRIVATE);
				os.write(data);
				// oritation = getResources().getConfiguration().orientation;
				oritation = calcuOritation();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "Error writing to file" + filename, e);
				success = false;

			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, "Error closing file" + filename, e);
						success = false;
					}
				}
			}
			if (success) {
				// Log.i(TAG, "JPEG saved at "+filename);
				Log.i(TAG, "JPEG oritation " + oritation);
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				i.putExtra(EXTRA_PHOTO_ORITATION, oritation);
				getActivity().setResult(Activity.RESULT_OK, i);

			} else {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();

		}
	};

	@SuppressWarnings("deprecation")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_crime_camera, container,
				false);
		Button takePictureButton = (Button) v
				.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// getActivity().finish();
				if (mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, mJpegCallback);
				}
			}
		});
		mSurfaceView = (SurfaceView) v
				.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if (mCamera != null)
					mCamera.stopPreview();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try {
					if (mCamera != null) {
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
				if (mCamera == null)
					return;
				Camera.Parameters parameters = mCamera.getParameters();
				Size s = getBestSupportedSize(
						parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(),
						width, height);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try {
					mCamera.startPreview();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e(TAG, "Could not start preview " + e);
					mCamera.release();
					mCamera = null;
				}
			}
		});
		mProgressContainer = v
				.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		mSensorManager = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		mAcceleSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagneticSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
		} else {
			mCamera = Camera.open();
		}
		mSensorManager.registerListener(new CameraSenserEventListener(), mAcceleSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(new CameraSenserEventListener(),
				mMagneticSensor, Sensor.TYPE_MAGNETIC_FIELD);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
		mSensorManager.unregisterListener(new CameraSenserEventListener());
	}

	private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				bestSize = s;
				largestArea = area;
			}
		}
		return bestSize;
	}

	private class CameraSenserEventListener implements SensorEventListener {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				acceleValues = event.values;
			} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticValues = event.values;
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

	}

	private int calcuOritation() {
		// float[] values = new float[3];
		// float[] R = new float[9];
		// SensorManager.getRotationMatrix(R, null, acceleValues,
		// magneticValues);
		// SensorManager.getOrientation(R, values);
		// values[0]=(float) Math.toDegrees(values[0]);
		// return (int) values[0];
		float ax = acceleValues[0];
		float ay = acceleValues[1];

		double g = Math.sqrt(ax * ax + ay * ay);
		double cos = ay / g;
		if (cos > 1) {
			cos = 1;
		} else if (cos < -1) {
			cos = -1;
		}
		double rad = Math.acos(cos);
		if (ax < 0) {
			rad = 2 * Math.PI - rad;
		}
		rad = Math.toDegrees(rad);
		return reCalcu(rad);
	}

	private int reCalcu(double rad) {
		int degree = 0;
		if (rad >= 0 && rad <= 30 || (rad >= 330 && rad <= 360)) {
			degree = 90;
		} else if ((rad >= 150 && rad <= 210)) {
			degree = 270;
		} else if (rad >= 240 && rad <= 300) {
			degree = 180;
		} else {
			degree = 0;
		}
		return degree;
	}
}
