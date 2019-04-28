package com.example.login;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

public class DrawOnTop extends View {
	Bitmap mBitmap, mBitmapBck;
	Paint mPaintBlack;
	Paint mPaintYellow;
	byte[] mYUVData;
	int[] mRGBData;
	int mImageWidth, mImageHeight;
	int[] mGrayHistogram;
	double[] mGrayCDF;
	int[] background;
	int mState;
	Bundle mBundle;
	byte images[][];
	ByteArrayOutputStream bs; 
	ArrayList<Bitmap> bitmaps;
	picturestore pc=new picturestore();
	boolean taken;
	
	static final int STATE_ORIGINAL = 0;
	static final int STATE_PROCESSED = 1;
	static final int STATE_CAPTURE = 2;
	static final int STATE_STOP = 3;
		Bitmap bmparray;
		Intent it,it2,it3;
		
	public DrawOnTop(Context context) {
		super(context);
		taken = false;
		mPaintBlack = new Paint();
		mPaintBlack.setStyle(Paint.Style.FILL);
		mPaintBlack.setColor(Color.BLACK);
		mPaintBlack.setTextSize(25);

		mPaintYellow = new Paint();
		mPaintYellow.setStyle(Paint.Style.FILL);
		mPaintYellow.setColor(Color.YELLOW);
		mPaintYellow.setTextSize(25);

		mBitmap = null;
		mYUVData = null;
		mRGBData = null;
		mGrayHistogram = new int[256];
		mGrayCDF = new double[256];
		mState = STATE_ORIGINAL;
       it = new Intent(getContext(), Display.class);
       it2 = new Intent(getContext(), Train.class);
       it3=new Intent(getContext(),ShortcutActivity.class);
       mBundle = new Bundle();
       bs=new ByteArrayOutputStream();
       bitmaps = new ArrayList<Bitmap>();
       images= new byte[5][];
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null) {
			int canvasWidth = canvas.getWidth();
			int canvasHeight = canvas.getHeight();
			int newImageWidth = 640;
			int newImageHeight = 480;
			int marginWidth = (canvasWidth - newImageWidth) / 2;
	
			// Convert from YUV to RGB
			if (mState == STATE_CAPTURE) {
					decodeYUV420RGB(mRGBData, mYUVData, mImageWidth,mImageHeight);
					mBitmap.setPixels(mRGBData, 0, mImageWidth, 0, 0,mImageWidth, mImageHeight);
					try{
						bmparray = mBitmap;
						
						Thread.sleep(100);
						
						Log.i("picturestore = ", "" + pc.count);
								bmparray.compress(Bitmap.CompressFormat.JPEG, 50, bs);						
					if(Bind.training==false && Bind.shortcut==false){
						it.putExtra("b", bs.toByteArray());
							getContext().startActivity(it);
					}else if(Bind.training==true){
						it2.putExtra("b", bs.toByteArray());
						getContext().startActivity(it2);
					
						
					}
					else if(Bind.training==false && Bind.shortcut==true){
						it3.putExtra("b", bs.toByteArray());
						getContext().startActivity(it3);
					}
					
							Log.i("hey", "gone in bundle");
							mState = STATE_STOP;
								}catch(Exception e){
				
					}
} else if (mState == STATE_ORIGINAL)
				decodeYUV420RGB(mRGBData, mYUVData, mImageWidth, mImageHeight);
			else if (mState == STATE_PROCESSED) {
				decodeYUV420RGBbinary(mRGBData, mYUVData, mImageWidth,
						mImageHeight);
			}
			if (mState != STATE_STOP) {
		//	if(pc.count==0)
				
				mBitmap.setPixels(mRGBData, 0, mImageWidth, 0, 0, mImageWidth,mImageHeight);
			
				Rect src = new Rect(0, 0, mImageWidth, mImageHeight);
				Rect dst = new Rect(marginWidth, 0, canvasWidth - marginWidth,
						canvasHeight);
				canvas.drawBitmap(mBitmap, src, dst, mPaintBlack);

				// Draw black borders
				canvas.drawRect(0, 0, marginWidth, canvasHeight, mPaintBlack);
				canvas.drawRect(canvasWidth - marginWidth, 0, canvasWidth,
						canvasHeight, mPaintBlack);
				String imageStateStr = "";
				if (mState == STATE_ORIGINAL)
					imageStateStr = "Original Image";
				else if (mState == STATE_PROCESSED)
					imageStateStr = "Processed Image";

				canvas.drawText(imageStateStr, marginWidth + 10 - 1, 30 - 1,
						mPaintBlack);
				canvas.drawText(imageStateStr, marginWidth + 10 + 1, 30 - 1,
						mPaintBlack);
				canvas.drawText(imageStateStr, marginWidth + 10 + 1, 30 + 1,
						mPaintBlack);
				canvas.drawText(imageStateStr, marginWidth + 10 - 1, 30 + 1,
						mPaintBlack);
				canvas.drawText(imageStateStr, marginWidth + 10, 30,
						mPaintYellow);
				super.onDraw(canvas);

			}
		}

	} // end onDraw method

	private void decodeYUV420RGBbackground(int[] rgb, byte[] yuv420sp,
			int width, int height) {
		final int frameSize = width * height;
		background = new int[rgb.length];
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
				background[yp] = rgb[yp];
			}
		}

	}

	private void decodeYUV420RGB(int[] rgb, byte[] yuv420sp, int width,
			int height) {
		// Convert YUV to RGB
		final int frameSize = width * height;
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}

	private void decodeYUV420RGBbinary(int[] rgb, byte[] yuv420sp, int width,
			int height) {
		final int frameSize = width * height;
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);

				r = Color.red(rgb[yp]);
				g = Color.green(rgb[yp]);
				b = Color.blue(rgb[yp]);
				// r=r-Color.red(mBitmapBck.getPixel(j, i));
				// g=g-Color.green(mBitmapBck.getPixel(j, i));
				// b=b-Color.blue(mBitmapBck.getPixel(j, i));
				if ((r > 90)
						&& (g > 35)
						&& (b > 15)
						&& ((Math.max(Math.max(r, g), b) - Math.min(
								Math.min(r, g), b)) > 15) && ((r - g) > 15)
						&& (r > g) && (r > b)) {
					rgb[yp] = Color.WHITE;
				} else {
					rgb[yp] = Color.BLACK;
				}

				/*
				 * if(r==0 && g==0 && b==0){ rgb[yp]=Color.BLACK;
				 * 
				 * }else{ rgb[yp]=Color.WHITE; }
				 */
			}
		}

	}

	public int[] selectionSort(int[] window) {
		int min = 0;
		for (int i = 0; i < window.length; i++) {
			for (int j = i + 1; j < window.length; j++) {
				if (window[j] < window[min]) {
					min = j;
				}
			}
			int temp = window[i];
			window[i] = window[min];
			window[min] = temp;
		}
		return window;
	}

	public Bitmap medianFilter(Bitmap bmp) {
		int window[] = new int[9];
		int img[][] = new int[bmp.getWidth()][bmp.getHeight()];
		for (int x = 1; x < bmp.getWidth() - 1; x++) {
			for (int y = 1; y < bmp.getHeight() - 1; y++) {
				if (bmp.getPixel(x, y) == Color.WHITE)
					img[x][y] = 1;
				else
					img[x][y] = 0;
			}

		}
		for (int x = 1; x < bmp.getWidth() - 1; x++) {
			for (int y = 1; y < bmp.getHeight() - 1; y++) {
				window[0] = img[x - 1][y - 1];
				window[1] = img[x - 1][y];
				window[2] = img[x - 1][y + 1];
				window[3] = img[x][y - 1];
				window[4] = img[x][y];
				window[5] = img[x][y + 1];
				window[6] = img[x + 1][y - 1];
				window[7] = img[x + 1][y];
				window[8] = img[x + 1][y + 1];
				window = selectionSort(window);
				if (window[4] == 1)
					bmp.setPixel(x, y, Color.WHITE);
				else
					bmp.setPixel(x, y, Color.BLACK);
			}
		}
		return bmp;
	}

	private void decodeYUV420RGBContrastEnhance(int[] rgb, byte[] yuv420sp,
			int width, int height) {
		// Compute histogram for Y
		final int frameSize = width * height;
		int clipLimit = frameSize / 10;
		for (int bin = 0; bin < 256; bin++)
			mGrayHistogram[bin] = 0;
		for (int j = 0, yp = 0; j < height; j++) {
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if (mGrayHistogram[y] < clipLimit)
					mGrayHistogram[y]++;
			}
		}
		double sumCDF = 0;
		for (int bin = 0; bin < 256; bin++) {
			sumCDF += (double) mGrayHistogram[bin] / (double) frameSize;
			mGrayCDF[bin] = sumCDF;
		}

		// Convert YUV to RGB
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}
				y = (int) (mGrayCDF[y] * 255 + 0.5);

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				/*
				 * float hsv[] = new float[3]; Color.RGBToHSV(r, g, b, hsv); if
				 * ((hsv[1] >= 0.10 && hsv[1] <= 0.75)) { rgb[yp]= Color.WHITE;
				 * } else { rgb[yp]=Color.BLACK; }
				 */
				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);

			}
		}
	}
}
