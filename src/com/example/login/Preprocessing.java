package com.example.login;



import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

public class Preprocessing {
	String word;
	int countfing=0;
	 //SQLiteDatabase gspeak;
	public Bitmap Normalize(Bitmap bmpOriginal) {
		int width, height, redavg = 0, greenavg = 0, blueavg = 0, rmin, rmax, gmin, gmax, bmin, bmax;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpNormalized = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixelColor = bmpOriginal.getPixel(i, j);
				redavg += Color.red(pixelColor);
				greenavg += Color.green(pixelColor);
				blueavg += Color.blue(pixelColor);
			}
		}
//	gspeak.openOrCreateDatabase("gspeak", null);
		redavg = redavg / (height * width);
		greenavg = greenavg / (height * width);
		blueavg = blueavg / (height * width);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixelColor = bmpOriginal.getPixel(i, j);
				int rnew, gnew, bnew, pixelAlpha;
				pixelAlpha = Color.alpha(pixelColor);
				// rnew = Color.red(pixelColor);
				// gnew = Color.green(pixelColor);
				// bnew = Color.blue(pixelColor);
				// rnew=((Color.red(pixelColor)-rmin)/rmax-rmin)*255;
				// gnew=((Color.green(pixelColor)-gmin)/gmax-gmin)*255;
				// bnew=((Color.blue(pixelColor)-bmin)/bmax-bmin)*255;
				rnew = ((Color.red(pixelColor) - redavg + 1) / 2) + redavg;
				gnew = ((Color.green(pixelColor) - greenavg + 1) / 2)
						+ greenavg;
				bnew = ((Color.blue(pixelColor) - blueavg + 1) / 2) + blueavg;
				;
				int newPixel = Color.argb(pixelAlpha, rnew, gnew, bnew);
				bmpNormalized.setPixel(i, j, newPixel);
			}
		}
		return bmpNormalized;
	
	}

	/*-------------------------------------*/

	public Bitmap eliminateBackground(Bitmap bmpNormalized) {
		Bind fing[]=new Bind[5];
		int width, height;
		height = bmpNormalized.getHeight();
		width = bmpNormalized.getWidth();
		Bitmap HSV = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Bitmap HSV2 = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				/*int pixelColor = bmpNormalized.getPixel(i, j);
				float hsv[] = new float[3];
				pixelColor = bmpNormalized.getPixel(i, j);
				Color.RGBToHSV(Color.red(pixelColor), Color.green(pixelColor),
						Color.blue(pixelColor), hsv);
				float hsv2[] = { 25, hsv[1], 0.40f };
				HSV2.setPixel(i, j, Color.HSVToColor(hsv2));
				*/
				int r = Color.red(bmpNormalized.getPixel(i, j));
				int g = Color.green(bmpNormalized.getPixel(i, j));
				int b = Color.blue(bmpNormalized.getPixel(i, j));
				if ((r > 90)
						&& (g > 35)
						&& (b > 15)
						&& ((Math.max(Math.max(r, g), b) - Math.min(
								Math.min(r, g), b)) > 15) && ((r - g) > 15)
						&& (r > g) && (r > b)) 
				{
					HSV2.setPixel(i, j, Color.WHITE);
				} else {
					HSV2.setPixel(i, j, Color.BLACK);
				}

			}
		}
		int distanceMatrix[][] = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (HSV2.getPixel(j, i) == Color.WHITE) {
					distanceMatrix[i][j] = 1;
				} else {
					distanceMatrix[i][j] = 0;
				}
			}
		}
		int start = 0, end = 0, countt = 0, countb = 0, countl = 0, countr = 0, i, j;
		boolean cont = false, l = false, r = false, t = false, b = false;
		for (j = 0; j < width; j++) {
			if (distanceMatrix[0][j] == 1 && cont == false) {
				start = j;
				cont = true;
				countt++;
			}
			if (distanceMatrix[0][j] == 1 && cont == true) {
				countt++;
			}
			if (distanceMatrix[0][j] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}

			if (distanceMatrix[height - 1][j] == 1 && cont == false) {
				start = j;
				cont = true;
				countb++;
			}
			if (distanceMatrix[height - 1][j] == 1 && cont == true) {
				countb++;
			}
			if (distanceMatrix[height - 1][j] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}
		}

		for (i = 0; i < height; i++) {
			if (distanceMatrix[i][0] == 1 && cont == false) {
				start = j;
				cont = true;
				countl++;
			}
			if (distanceMatrix[i][0] == 1 && cont == true) {
				countl++;
			}
			if (distanceMatrix[i][0] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}

			if (distanceMatrix[i][width - 1] == 1 && cont == false) {
				start = j;
				cont = true;
				countr++;
			}
			if (distanceMatrix[i][width - 1] == 1 && cont == true) {
				countr++;
			}
			if (distanceMatrix[i][width - 1] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}

		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countr) {
			for (j = width - 1; j > width - 15; j--) {
				r = true;
				HSV2.setPixel(j, 20, Color.YELLOW);
			}
		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countl) {
			for (j = 0; j < 15; j++) {
				l = true;
				HSV2.setPixel(j, 20, Color.YELLOW);
			}
		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countt) {
			for (i = 0; i < 15; i++) {
				t = true;
				HSV2.setPixel(20, i, Color.YELLOW);
			}
		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countb) {
			for (i = height - 1; i > height - 15; i--) {
				b = true;
				HSV2.setPixel(20, i, Color.YELLOW);
			}
		}
		
		
		int sum=0,k=0;
		int fi=0,fj=0,si=0,sj=0,ti=0,tj=0,ffi=0,ffj=0,thi=0,thj=0;
		boolean  thumb=false,first=false,second=false,third=false,fourth=false;
		if(b==true){
			b:for(i=height-1;i>=0;i--){
				sum=0;
				for(j=0;j<width-3;j++){
					sum+=distanceMatrix[i][j];
					if(distanceMatrix[i][j]==0 && distanceMatrix[i][j+1]==1 && distanceMatrix[i][j+2]==1 && distanceMatrix[i][j+3]==1){
						//bmpNormalized.setPixel(j+1, i, Color.BLUE);
						HSV2.setPixel(j, i, Color.BLUE);
					}
					if(distanceMatrix[i][j]==1 && distanceMatrix[i][j+1]==1 && distanceMatrix[i][j+2]==1 && distanceMatrix[i][j+3]==0){
						//bmpNormalized.setPixel(j, i, Color.BLUE);
						HSV2.setPixel(j, i, Color.BLUE);
					}
				}
				if(sum==0){
					k=i;
					break b;
				}
			}
			
			/*for(i=0;i<5;i++){
				fing[i].fingeri=0;
				fing[i].fingerj=0;
				fing[i].avail=false;
				
			}*/
			Log.i("Fing","First");
			Log.i("k_found",""+k);
			b:for(i=k;i<height;i++){
				for(j=0;j<width;j++){
					if(distanceMatrix[i][j]==1){
						thi=i;thj=j;
						thumb=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+100;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						fi=thi;fj=thj;
						thi=i;thj=j;
						first=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+100;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						si=fi;sj=fj;
						fi=thi;fj=thj;
						thi=i;thj=j;
						second=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+100;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						ti=si;tj=sj;
						si=fi;sj=fj;
						fi=thi;fj=thj;
						thi=i;thj=j;
						third=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+100;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						ffi=ti;ffj=tj;
						ti=si;tj=sj;
						si=fi;sj=fj;
						fi=thi;fj=thj;
						thi=i;thj=j;
						fourth=true;
						break b;
					}
				}
			}
		int fingeri[]=new int[5];
		int fingerj[]=new int[5];
		boolean avail[]=new boolean[5];
		for(i=0;i<5;i++){
			fingeri[i]=0;
			fingerj[i]=0;
			avail[i]=false;
		}
		Log.i("Fing","Second");
		if(thumb==true){
			avail[0] = true;
			fingeri[0]=thi;
			fingerj[0]=thj;
		}
		Log.i("Fing","third");
		if(first==true){
			avail[1]=true;
			fingeri[1]=fi;
			fingerj[1]=fj;
		}
		if(second==true){
			avail[2]=true;
			fingeri[2]=si;
			fingerj[2]=sj;
		}
		if(third==true)
		{
			avail[3]=true;
			fingeri[3]=ti;
			fingerj[3]=tj;
		}
		if(fourth==true){
			avail[4]=true;
			fingeri[4]=ffi;
			fingerj[4]=ffj;
		}
		Log.i("Fing","Completed");
		for(i=0;i<5;i++){
			Log.i("Fingeri-"+i,""+fingeri[i]);
			Log.i("Fingerj-"+i,""+fingerj[i]);
			Log.i("Avail-"+i,""+avail[i]);
		}
		int min=0;
		for(i=1;i<5;i++){
			if(fingeri[i]<fingeri[min] && avail[i]==true){
				min=i;
			}
		}
		Log.i("min",""+min);
		w:while(min<4){
			Log.i("In while",""+min);
			lb:for(i=fingeri[min]+5;i<fingeri[min]+60;i++){
				for(j=fingerj[min]+20;j<width;j++){
					if(distanceMatrix[i][j]==1){
						avail[min+1]=true;
						fingeri[min+1]=i;
						fingerj[min+1]=j;
						min=min+1;
						break lb;
					}
					if(i==fingeri[min]+59 && j==width-1){
						break w;
					}
				}
			}
		}
		Log.i("Finger","Again");
		for(i=0;i<5;i++){
			Log.i("Fingeri-"+i,""+fingeri[i]);
			Log.i("Fingerj-"+i,""+fingerj[i]);
			Log.i("Avail-"+i,""+avail[i]);
		}
		
		Canvas canvas = new Canvas(HSV2); 
		Paint paint = new Paint(); paint.setColor(Color.RED);
		for(i=0;i<5;i++){
			if(avail[i]==true){
				canvas.drawCircle(fingerj[i], fingeri[i], 2, paint);
			}
		}

		for(i=0;i<5;i++){
			if(avail[i]==true){
				countfing++;
			}
		}

	//	final Cursor resultset = gspeak.rawQuery("select text from train where numfing ="+countfing+";",null);
	/*	try{		
			if(resultset.moveToFirst()){
			String text = resultset.getString(0);
			t1.setText(""+text);
			greedy.execSQL("update train set text ='"+t1.getText().toString()+"' where numfing="+ps.countfing+";");	
			Toast.makeText(getApplicationContext(), "Updated", 3000).show();
				}else{
					
					greedy.execSQL("insert into train values("+ps.countfing+",'"+t1.getText().toString()+"');");
					Toast.makeText(getApplicationContext(), "Inserted", 3000).show();
				}
}catch(Exception e){
	Log.i("exception",""+e);
}	*/
		if(countfing==1){
			word="ek";
		}
		else if(countfing==2){
			word="do";
		}
		else if(countfing==3){
			word="tin";
		}
		else if(countfing==4){
			word="char";
		}
		else{
			word="ruko";
			
		}
	
		
		//paint.setStyle(Paint.Style.STROKE);
		/*if(thumb==true)
			canvas.drawCircle(thj, thi, 2,paint);
		if(first==true)
			canvas.drawCircle(fj, fi, 2,paint);
		if(second==true)
			canvas.drawCircle(sj, si, 2,paint);
		if(third==true)
			canvas.drawCircle(tj, ti, 2,paint);
		if(fourth==true)
			canvas.drawCircle(ffj, ffi, 2,paint);*/

		
		
		}
		return HSV2;
	}

	/*-------------------------------------*/

	public Bitmap hsvConversionDistanceTransform(Bitmap bmpNormalized) {
		int width, height;
		height = bmpNormalized.getHeight();
		width = bmpNormalized.getWidth();
		Bitmap HSV = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Bitmap HSV2 = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				/*int pixelColor = bmpNormalized.getPixel(i, j);
				float hsv[] = new float[3];
				pixelColor = bmpNormalized.getPixel(i, j);
				Color.RGBToHSV(Color.red(pixelColor), Color.green(pixelColor),
						Color.blue(pixelColor), hsv);
				float hsv2[] = { 25, hsv[1], 0.40f };
				HSV2.setPixel(i, j, Color.HSVToColor(hsv2));
				*/
				int r = Color.red(bmpNormalized.getPixel(i, j));
				int g = Color.green(bmpNormalized.getPixel(i, j));
				int b = Color.blue(bmpNormalized.getPixel(i, j));
				if ((r > 90)
						&& (g > 35)
						&& (b > 15)
						&& ((Math.max(Math.max(r, g), b) - Math.min(
								Math.min(r, g), b)) > 15) && ((r - g) > 15)
						&& (r > g) && (r > b)) 
				{
					HSV2.setPixel(i, j, Color.WHITE);
				} else {
					HSV2.setPixel(i, j, Color.BLACK);
				}

			}
		}
		//HSV2 = medianFilter(HSV2);
		int distanceMatrix[][] = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (HSV2.getPixel(j, i) == Color.WHITE) {
					distanceMatrix[i][j] = 1;
				} else {
					distanceMatrix[i][j] = 0;
				}
			}
		}
		/*int w = width;
		for (int i = 1; i <= height / 2; i++) {
			for (int j = i; j < w; j++) {
				// if(distanceMatrix[i][j]!=0){
				distanceMatrix[i][j] += distanceMatrix[i - 1][j];
				// }
			}
			w--;
		}
		w = width;
		for (int i = height - 2; i >= height / 2; i--) {
			for (int j = height - 1 - i; j < w; j++) {
				// if(distanceMatrix[i][j]!=0){
				distanceMatrix[i][j] += distanceMatrix[i + 1][j];
				// }
			}
			w--;
		}

		int h = height;
		for (int j = 1; j <= width / 2; j++) {
			for (int i = j; i < h; i++) {
				// if(distanceMatrix[i][j]!=0){
				distanceMatrix[i][j] += distanceMatrix[i][j - 1];
				// }
			}
			h--;
		}
		h = height;
		for (int j = width - 2; j >= width / 2; j--) {
			for (int i = width - 1 - j; i < h; i++) {
				// if(distanceMatrix[i][j]!=0){
				distanceMatrix[i][j] += distanceMatrix[i][j + 1];
				// }
			}
			h--;
		}

		int x = 0, y = 0, max = distanceMatrix[0][0];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (distanceMatrix[i][j] > max) {
					x = i;
					y = j;
					max = distanceMatrix[i][j];
					Log.i("x*y=", "" + x + " " + y);
				}
			}
		}*/
		/*----------------------------new-------------
		 int fi=0,fj=0,si=0,sj=0,ti=0,tj=0,ffi=0,ffj=0,thi=0,thj=0;
		
		 b:for(int i=0;i<height;i++){
		 boolean flag=false;
		 b3:for(int j=0;j<width;j++){
		 if(distanceMatrix[i][j]==1){
		 flag=true;
		 }
		 if(flag==true){
		 int sum=0;
		 if(i+50<height){
		 int p=i+50;
		 }
		 else{
		 int p=height;
		 }
		 b2:for(int k=i;k<i+50;k++){
		 sum=0;
		 for(int l=0;l<width;l++){
		 sum+=distanceMatrix[k][l];
		 }
		 if(sum==0){
		 flag=false;
		 break b2;
		 }
		 }
		 }
		 if(flag){
		 fi=i;fj=j;
		 HSV2.setPixel(j+2, i+5, Color.GREEN);
		 HSV2.setPixel(j+1, i+5, Color.GREEN);
		 HSV2.setPixel(j+3, i+5, Color.GREEN);
		 HSV2.setPixel(j+1, i+4, Color.GREEN);
		 HSV2.setPixel(j+1, i+6, Color.GREEN);
		 HSV2.setPixel(j+2, i+4, Color.GREEN);
		 HSV2.setPixel(j+2, i+6, Color.GREEN);
		 HSV2.setPixel(j+3, i+4, Color.GREEN);
		 HSV2.setPixel(j+3, i+6, Color.GREEN);
		
		 bmpNormalized.setPixel(j+2, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+6, Color.BLUE);
		
		 break b3;
		 }
		 }
		 if(flag==true){
		 flag=false;
		 break b;
		 }
		 }
		 b:for(int i=fi;i<fi+30;i++){
		 for(int j=0;j<fj-10;j++){
		 if(distanceMatrix[i][j]==1){
		 si=i;sj=j;
		 break b;
		 }
		 }
		 }
		
		
		
		 int i=si,j=sj;
		
		
		
		
		 bmpNormalized.setPixel(j+2, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+6, Color.BLUE);
		
		
		
		 b:for(i=si;i<si+80;i++){
		 for(j=0;j<sj-10;j++){
		 if(distanceMatrix[i][j]==1){
		 thi=i;thj=j;
		 break b;
		 }
		 }
		 }
		
		
		
		 i=thi;j=thj;
		
		
		
		
		 bmpNormalized.setPixel(j+2, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+6, Color.BLUE);
		
		
		 b:for(i=fi;i<fi+30;i++){
		 for(j=fj+10;j<width;j++){
		 if(distanceMatrix[i][j]==1){
		 ti=i;tj=j;
		 break b;
		 }
		 }
		 }
		
		
		
		 i=ti;j=tj;
		
		
		
		
		 bmpNormalized.setPixel(j+2, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+6, Color.BLUE);
		
		
		 b:for(i=ti;i<ti+30;i++){
		 for(j=tj+10;j<width;j++){
		 if(distanceMatrix[i][j]==1){
		 ffi=i;ffj=j;
		 break b;
		 }
		 }
		 }
		
		
		
		 i=ffi;j=ffj;
		
		
		
		
		 bmpNormalized.setPixel(j+2, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+5, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+1, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+2, i+6, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+4, Color.BLUE);
		 bmpNormalized.setPixel(j+3, i+6, Color.BLUE);
		
		
		
		 ------------------------------------*/

		int start = 0, end = 0, countt = 0, countb = 0, countl = 0, countr = 0, i, j;
		boolean cont = false, l = false, r = false, t = false, b = false;
		for (j = 0; j < width; j++) {
			if (distanceMatrix[0][j] == 1 && cont == false) {
				start = j;
				cont = true;
				countt++;
			}
			if (distanceMatrix[0][j] == 1 && cont == true) {
				countt++;
			}
			if (distanceMatrix[0][j] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}

			if (distanceMatrix[height - 1][j] == 1 && cont == false) {
				start = j;
				cont = true;
				countb++;
			}
			if (distanceMatrix[height - 1][j] == 1 && cont == true) {
				countb++;
			}
			if (distanceMatrix[height - 1][j] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}
		}

		for (i = 0; i < height; i++) {
			if (distanceMatrix[i][0] == 1 && cont == false) {
				start = j;
				cont = true;
				countl++;
			}
			if (distanceMatrix[i][0] == 1 && cont == true) {
				countl++;
			}
			if (distanceMatrix[i][0] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}

			if (distanceMatrix[i][width - 1] == 1 && cont == false) {
				start = j;
				cont = true;
				countr++;
			}
			if (distanceMatrix[i][width - 1] == 1 && cont == true) {
				countr++;
			}
			if (distanceMatrix[i][width - 1] == 0 && cont == true) {
				end = j - 1;
				cont = false;
			}

		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countr) {
			for (j = width - 1; j > width - 15; j--) {
				r = true;
				bmpNormalized.setPixel(j, 20, Color.YELLOW);
			}
		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countl) {
			for (j = 0; j < 15; j++) {
				l = true;
				bmpNormalized.setPixel(j, 20, Color.YELLOW);
			}
		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countt) {
			for (i = 0; i < 15; i++) {
				t = true;
				bmpNormalized.setPixel(20, i, Color.YELLOW);
			}
		}

		if (Math.max(Math.max(countr, countl), Math.max(countt, countb)) == countb) {
			for (i = height - 1; i > height - 15; i--) {
				b = true;
				bmpNormalized.setPixel(20, i, Color.YELLOW);
			}
		}
		int sum=0,k=0;
		int fi=0,fj=0,si=0,sj=0,ti=0,tj=0,ffi=0,ffj=0,thi=0,thj=0;
		boolean  thumb=false,first=false,second=false,third=false,fourth=false;
		if(b==true){
			b:for(i=height-1;i>=0;i--){
				sum=0;
				for(j=0;j<width-3;j++){
					sum+=distanceMatrix[i][j];
					if(distanceMatrix[i][j]==0 && distanceMatrix[i][j+1]==1 && distanceMatrix[i][j+2]==1 && distanceMatrix[i][j+3]==1){
						bmpNormalized.setPixel(j+1, i, Color.BLUE);
						HSV2.setPixel(j, i, Color.BLUE);
					}
					if(distanceMatrix[i][j]==1 && distanceMatrix[i][j+1]==1 && distanceMatrix[i][j+2]==1 && distanceMatrix[i][j+3]==0){
						bmpNormalized.setPixel(j, i, Color.BLUE);
						HSV2.setPixel(j, i, Color.BLUE);
					}
				}
				if(sum==0){
					k=i;
					break b;
				}
			}
			Log.i("k_found",""+k);
			b:for(i=k;i<height;i++){
				for(j=0;j<width;j++){
					if(distanceMatrix[i][j]==1){
						thi=i;thj=j;
						thumb=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+80;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						fi=thi;fj=thj;
						thi=i;thj=j;
						first=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+80;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						si=fi;sj=fj;
						fi=thi;fj=thj;
						thi=i;thj=j;
						second=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+80;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						ti=si;tj=sj;
						si=fi;sj=fj;
						fi=thi;fj=thj;
						thi=i;thj=j;
						third=true;
						break b;
					}
				}
			}
			b:for(i=thi;i<thi+80;i++){
				for(j=0;j<thj-30;j++){
					if(distanceMatrix[i][j]==1){
						ffi=ti;ffj=tj;
						ti=si;tj=sj;
						si=fi;sj=fj;
						fi=thi;fj=thj;
						thi=i;thj=j;
						fourth=true;
						break b;
					}
				}
			}
		}
		
		
		
		Canvas canvas = new Canvas(bmpNormalized); 
		Paint paint = new Paint(); paint.setColor(Color.RED);
		//paint.setStyle(Paint.Style.STROKE);
		if(thumb==true)
			canvas.drawCircle(thj, thi, 2,paint);
		if(first==true)
			canvas.drawCircle(fj, fi, 2,paint);
		if(second==true)
			canvas.drawCircle(sj, si, 2,paint);
		if(third==true)
			canvas.drawCircle(tj, ti, 2,paint);
		if(fourth==true)
			canvas.drawCircle(ffj, ffi, 2,paint);
		int count[],st[],ed[];
		Log.i("g_height",""+height);
		Log.i("height-k", ""+(height-k));
		count=new int[height-k];
		st=new int[height-k];
		ed=new int[height-k];
		int ts=0,te=0;
		boolean s=true,tf=true;
		for(i=k;i<height-1;i++){
			b:for(j=0;j<width-1;j++){
				if(j<width-2 && HSV2.getPixel(j, i)==Color.BLUE && HSV2.getPixel(j+1, i)!=Color.BLUE && s==true){
					ts=j;
					s=false;
					continue;
				}
				else if(j>=1 && HSV2.getPixel(j, i)==Color.BLUE && HSV2.getPixel(j-1, i)!=Color.BLUE && s==false){
					te=j;
					s=true;
					break b;
				}
				
			}
			st[i-k]=ts;
			ed[i-k]=te;
			count[i-k]=ed[i-k]-st[i-k]+1;
		}
			
		
		for(i=0;i<count.length-1;i++){
			Log.i("palmpoint["+i+"]",count[i]+" "+st[i]+" "+ed[i]);
		}
		paint.setColor(Color.GREEN);
		/*label:for(i=1;i<count.length-1;i++){
			if(count[i]-count[i-1]>=10){
				canvas.drawCircle(st[i], i, 2,paint);
				canvas.drawCircle(ed[i], i, 2,paint);
				Log.i("upper",""+i);
				break label;
			}
			
		}*/
		int lower;
		label2:for(i=count.length-2;i>=0;i--){
			if(count[i]-count[i+1]>=5){
				canvas.drawCircle(st[i], i, 2,paint);
				canvas.drawCircle(ed[i], i, 2,paint);
				Log.i("lower",""+i);
				i=count[i-count[i]];
				canvas.drawCircle(st[i], i, 2,paint);
				canvas.drawCircle(ed[i], i, 2,paint);
				
				break label2;
			}
			
		}
		
		/*-------------------------------------*/

		/*HSV2.setPixel(y, x, Color.BLUE);

		bmpNormalized.setPixel(y, x, Color.BLUE);
		bmpNormalized.setPixel(y - 1, x, Color.BLUE);
		bmpNormalized.setPixel(y + 1, x, Color.BLUE);
		bmpNormalized.setPixel(y, x - 1, Color.BLUE);
		bmpNormalized.setPixel(y, x + 1, Color.BLUE);
		bmpNormalized.setPixel(y - 1, x - 1, Color.BLUE);
		bmpNormalized.setPixel(y - 1, x + 1, Color.BLUE);
		bmpNormalized.setPixel(y + 1, x - 1, Color.BLUE);
		bmpNormalized.setPixel(y + 1, x + 1, Color.BLUE);

		Log.i("done_zinu", "begin of radius logic");
		int p = x, q = y;
		int east = 0, west = 0, north = 0, south = 0, nw = 0, ne = 0, sw = 0, se = 0;
		while (q > 0 && HSV2.getPixel(--q, p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			west++;
		}
		p = x;
		q = y;
		while (q < width - 1 && HSV2.getPixel(++q, p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			east++;
		}
		p = x;
		q = y;
		while (p > 0 && HSV2.getPixel(q, --p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			north++;
		}
		p = x;
		q = y;
		while (p < height - 1 && HSV2.getPixel(q, ++p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			south++;
		}
		p = x;
		q = y;
		while (p > 0 && q > 0 && HSV2.getPixel(--q, --p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			nw++;
		}
		p = x;
		q = y;
		while (p < height - 1 && q < width - 1
				&& HSV2.getPixel(++q, ++p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			se++;
		}
		p = x;
		q = y;
		while (p > 0 && q < width - 1 && HSV2.getPixel(++q, --p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			ne++;
		}
		p = x;
		q = y;
		while (p < height - 1 && q > 0
				&& HSV2.getPixel(--q, ++p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			sw++;
		}
		Log.i("west", "" + west);
		Log.i("east", "" + east);
		Log.i("north", "" + north);
		Log.i("south", "" + south);
		Log.i("ne", "" + ne);
		Log.i("nw", "" + nw);
		Log.i("se", "" + se);
		Log.i("sw", "" + sw);*/
		// int r = Math.min(Math.min(Math.min(east, west), Math.min(north,
		// south)),Math.min(Math.min(ne, nw), Math.min(se, sw)));
		/*
		 * int r=Math.min(Math.min(north, south), Math.min(east, west));
		 * Log.i("zaineya=", "" + r); Canvas canvas = new Canvas(HSV2); Paint
		 * paint = new Paint(); paint.setColor(Color.RED);
		 * paint.setStyle(Paint.Style.STROKE); canvas.drawCircle(y, x, r,
		 * paint); int outerradius = (int) (r * 1.2); canvas.drawCircle(y, x,
		 * outerradius, paint);
		 */

		return bmpNormalized;
	}

	public Bitmap hsvConversion(Bitmap bmpNormalized) {
		int width, height;
		height = bmpNormalized.getHeight();
		width = bmpNormalized.getWidth();
		Bitmap HSV = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Bitmap HSV2 = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixelColor = bmpNormalized.getPixel(i, j);
				float hsv[] = new float[3];
				pixelColor = bmpNormalized.getPixel(i, j);
				Color.RGBToHSV(Color.red(pixelColor), Color.green(pixelColor),
						Color.blue(pixelColor), hsv);
				float hsv2[] = { 25, hsv[1], 0.40f };
				HSV2.setPixel(i, j, Color.HSVToColor(hsv2));
				if ( /* (hsv2[0] >= 0 && hsv2[0] <= 50) && */(hsv[1] >= 0.10 && hsv[1] <= 0.75)) {
					HSV2.setPixel(i, j, Color.WHITE);
				} else {
					HSV2.setPixel(i, j, Color.BLACK);
				}

			}
		}
		HSV2 = medianFilter(HSV2);
		int startpixel[], endpixel[], stemp = 0, etemp = 0;
		boolean first = true, cont = false;
		startpixel = new int[height];
		endpixel = new int[height];
		for (int i = 0; i < height; i++) {
			startpixel[i] = endpixel[i] = 0;
			for (int j = 0; j < width; j++) {
				if (HSV2.getPixel(j, i) == Color.WHITE && cont == false) {
					stemp = j;
					cont = true;
				}
				if (HSV2.getPixel(j, i) == Color.WHITE && cont == true) {
					continue;
				}
				if (HSV2.getPixel(j, i) == Color.BLACK && cont == true) {
					etemp = j - 1;
					cont = false;
					if (first == true) {
						startpixel[i] = stemp;
						endpixel[i] = etemp;
						first = false;
					} else {
						if (endpixel[i] - startpixel[i] < etemp - stemp) {
							startpixel[i] = stemp;
							endpixel[i] = etemp;
						}
					}
				}

			}
			Log.i("range of " + i, "" + startpixel[i] + " " + endpixel[i]);
		}
		int x = 0, y = 0;
		int length = width / 3;
		Log.i("length", "" + length);
		loop: for (int i = 0; i < height; i++) {
			if (endpixel[i] - startpixel[i] > length) {
				for (int j = startpixel[i]; j <= endpixel[i]; j++) {
					HSV2.setPixel(j, i, Color.RED);
				}
				for (int k = i; k <= i + (endpixel[i] - startpixel[i]); k++) {
					HSV2.setPixel(startpixel[i], k, Color.RED);
				}
				for (int k = i; k <= i + (endpixel[i] - startpixel[i]); k++) {
					HSV2.setPixel(endpixel[i], k, Color.RED);
				}
				for (int k = startpixel[i]; k < endpixel[i]; k++) {
					HSV2.setPixel(k, i + (endpixel[i] - startpixel[i]),
							Color.RED);
				}
				x = (i + ((endpixel[i] - startpixel[i]) / 2));
				y = (startpixel[i] + endpixel[i]) / 2;
				HSV2.setPixel(y, x, Color.BLUE);
				/*
				 * HSV2.setPixel(y+1, x, Color.BLUE); HSV2.setPixel(y, x+1,
				 * Color.BLUE); HSV2.setPixel(y+1, x+1, Color.BLUE);
				 * HSV2.setPixel(y-1, x, Color.BLUE); HSV2.setPixel(y, x-1,
				 * Color.BLUE); HSV2.setPixel(y-1, x-1, Color.BLUE);
				 */
				break loop;
			}
		}

		int p = x, q = y;
		int east = 0, west = 0, north = 0, south = 0, nw = 0, ne = 0, sw = 0, se = 0;
		while (q > 0 && HSV2.getPixel(--q, p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			west++;
		}
		p = x;
		q = y;
		while (q < width && HSV2.getPixel(++q, p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			east++;
		}
		HSV2.setPixel(y, x, Color.WHITE);
		y = y + ((east - west) / 2);
		HSV2.setPixel(y, x, Color.BLUE);
		p = x;
		q = y;
		while (p > 0 && q > 0 && HSV2.getPixel(--q, --p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			nw++;
		}
		p = x;
		q = y;
		while (p < height && q < width
				&& HSV2.getPixel(++q, ++p) != Color.BLACK) {
			HSV2.setPixel(q, p, Color.YELLOW);
			se++;
		}
		HSV2.setPixel(y, x, Color.WHITE);
		x = x + ((se - nw) / 2);
		y = y + ((se - nw) / 2);
		HSV2.setPixel(y, x, Color.BLUE);
		/*
		 * p=x;q=y; while(p>0 && q<width && HSV2.getPixel(++q,
		 * --p)!=Color.BLACK){ HSV2.setPixel(q, p, Color.YELLOW); ne++; }
		 * p=x;q=y; while(p<height && q>0 && HSV2.getPixel(--q,
		 * ++p)!=Color.BLACK){ HSV2.setPixel(q, p, Color.YELLOW); sw++; }
		 * HSV2.setPixel(y, x, Color.WHITE); x=x+((sw-ne)/2); y=y+((sw-ne)/2);
		 * HSV2.setPixel(y, x, Color.BLUE);
		 */

		int r = Math.min(
				Math.min(Math.min(east, west), Math.min(north, south)),
				Math.min(Math.min(ne, nw), Math.min(se, sw)));
		Canvas canvas = new Canvas(HSV2);
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		canvas.drawCircle(x, y, r, paint);
		return HSV2;
	}

	public Bitmap edgeFilter(Bitmap bmp) {
		int window[] = new int[9], kernel[] = { 0, 1, 0, 1, -4, 1, 0, 1, 0 };
		for (int x = 1; x < bmp.getWidth() - 1; x++) {
			for (int y = 1; y < bmp.getHeight() - 1; y++) {
				int sumr = 0;
				window[0] = Color.red(bmp.getPixel(x - 1, y - 1));
				window[1] = Color.red(bmp.getPixel(x - 1, y));
				window[2] = Color.red(bmp.getPixel(x - 1, y + 1));
				window[3] = Color.red(bmp.getPixel(x, y - 1));
				window[4] = Color.red(bmp.getPixel(x, y));
				window[5] = Color.red(bmp.getPixel(x, y + 1));
				window[6] = Color.red(bmp.getPixel(x + 1, y - 1));
				window[7] = Color.red(bmp.getPixel(x + 1, y));
				window[8] = Color.red(bmp.getPixel(x + 1, y + 1));
				for (int i = 0; i < 9; i++) {
					sumr += kernel[i] * window[i];
				}
				int sumg = 0;
				window[0] = Color.green(bmp.getPixel(x - 1, y - 1));
				window[1] = Color.green(bmp.getPixel(x - 1, y));
				window[2] = Color.green(bmp.getPixel(x - 1, y + 1));
				window[3] = Color.green(bmp.getPixel(x, y - 1));
				window[4] = Color.green(bmp.getPixel(x, y));
				window[5] = Color.green(bmp.getPixel(x, y + 1));
				window[6] = Color.green(bmp.getPixel(x + 1, y - 1));
				window[7] = Color.green(bmp.getPixel(x + 1, y));
				window[8] = Color.green(bmp.getPixel(x + 1, y + 1));
				for (int i = 0; i < 9; i++) {
					sumg += kernel[i] * window[i];
				}
				int sumb = 0;
				window[0] = Color.blue(bmp.getPixel(x - 1, y - 1));
				window[1] = Color.blue(bmp.getPixel(x - 1, y));
				window[2] = Color.blue(bmp.getPixel(x - 1, y + 1));
				window[3] = Color.blue(bmp.getPixel(x, y - 1));
				window[4] = Color.blue(bmp.getPixel(x, y));
				window[5] = Color.blue(bmp.getPixel(x, y + 1));
				window[6] = Color.blue(bmp.getPixel(x + 1, y - 1));
				window[7] = Color.blue(bmp.getPixel(x + 1, y));
				window[8] = Color.blue(bmp.getPixel(x + 1, y + 1));
				for (int i = 0; i < 9; i++) {
					sumb += kernel[i] * window[i];
				}
				bmp.setPixel(x, y, Color.rgb(sumr, sumg, sumb));

			}
		}
		return bmp;
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
}

