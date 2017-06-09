package com.dan.criminalint;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity a, String path) {
//		Display display = a.getWindowManager().getDefaultDisplay();
//		float destWidth = display.getWidth();
//		float destHeight = display.getHeight();
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(path, options);
//		float srcWidth = options.outWidth;
//		float srcHeight = options.outHeight;
//		int inSampleSize = 1;
//		if (srcWidth > destWidth || srcHeight > destHeight) {
//			if (srcWidth > srcHeight) {
//				inSampleSize = Math.round(srcHeight / destHeight);
//			} else {
//				inSampleSize = Math.round(srcWidth / destWidth);
//			}
//		}
//		options = new BitmapFactory.Options();
//		options.inSampleSize = inSampleSize;
		Bitmap bitmap = getScaledBitmap(a, path);
		return new BitmapDrawable(a.getResources(), bitmap);

	}

	public static Bitmap getScaledBitmap(Activity a, String path) {
		Display display = a.getWindowManager().getDefaultDisplay();
		float destWidth = display.getWidth();
		float destHeight = display.getHeight();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		int inSampleSize = 1;
		if (srcWidth > destWidth || srcHeight > destHeight) {
			if (srcWidth > srcHeight) {
				inSampleSize = Math.round(srcHeight / destHeight);
			} else {
				inSampleSize = Math.round(srcWidth / destWidth);
			}
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		return  BitmapFactory.decodeFile(path, options);
	}

	public static BitmapDrawable getScaledDrawable(Activity a, String path,
			int oritation) {
		Bitmap bitmap=rotateBitmap(getScaledBitmap(a, path), oritation);
		return new BitmapDrawable(a.getResources(), bitmap);
	}

	public static void cleanImageView(ImageView imageview) {
		if (!(imageview.getDrawable() instanceof BitmapDrawable))
			return;
		BitmapDrawable b = (BitmapDrawable) imageview.getDrawable();
		b.getBitmap().recycle();
		imageview.setImageDrawable(null);
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int oritation) {
		int degree = oritation;
//		switch (oritation) {
//		// case ExifInterface.ORIENTATION_ROTATE_180:
//		// degree = 180;
//		// break;
//		// case ExifInterface.ORIENTATION_ROTATE_270:
//		// degree = 270;
//		// break;
//		// case ExifInterface.ORIENTATION_ROTATE_90:
//		// degree = 90;
//		// break;
//		case Configuration.ORIENTATION_PORTRAIT:
//			degree = 90;
//
//		default:
//			degree = 0;
//			break;
//		}
		if (degree != 0) {
			Matrix m = new Matrix();
			m.postRotate(degree);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
		}
		return bitmap;

	}
}
