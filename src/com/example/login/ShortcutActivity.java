package com.example.login;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ShortcutActivity extends Activity implements OnClickListener,OnInitListener{
	ImageView iv0;
	Bitmap b;
	Intent ttsintent;
	private TextToSpeech myTTS;
	Preprocessing ps=new Preprocessing();
	SQLiteDatabase gspeak;
	String text;
	MainActivity ma;
	PackageManager p=null;
	Intent intent;
	
	//status check code
	private int MY_DATA_CHECK_CODE = 0;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			Bind.shortcut=false;
			p=getPackageManager();
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_shortcut);
			 Log.i("activity", "hello");
			 b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("b"),0,getIntent().getByteArrayExtra("b").length);
			 ps.eliminateBackground(b);
			Log.i("sooo","here we Are..");
			gspeak = openOrCreateDatabase("gspeak", MODE_PRIVATE, null);
//		String user = ma.usname.getText().toString();
			final Cursor resultset = gspeak.rawQuery("select appname from train where numfing = "+ps.countfing+" ;",null);
		try{
			if(resultset.moveToFirst()){
				text = resultset.getString(0);
				Log.i("package",text);
				intent = p.getLaunchIntentForPackage(text);
					
					startActivity(intent);
				}else{
						Toast.makeText(getApplicationContext(), "You have inserted a new Gesture!!", 3000).show();
			}}catch(Exception e){
				//Log.i("exception", ""+e);
			}
		gspeak.close();
		}

		public void onClick(View v) {

	        //get the text entered
	        //EditText enteredText = (EditText)findViewById(R.id.enter);
	        //String words = enteredText.getText().toString();
	        speakWords(text);
	}
	 
	    //speak the user text
	private void speakWords(String speech) {

	        //speak straight away
	        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
	}
	 
	    //act on result of TTS data check
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 
	    if (requestCode == MY_DATA_CHECK_CODE) {
	        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	            //the user has the necessary data - create the TTS
	        myTTS = new TextToSpeech(this, this);
	        }
	        else {
	                //no data - install it now
	            Intent installTTSIntent = new Intent();
	            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	            startActivity(installTTSIntent);
	        }
	    }
	}

	    //setup TTS
	public void onInit(int initStatus) {
	 
	        //check for successful instantiation
	    if (initStatus == TextToSpeech.SUCCESS) {
	     //   if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
	            myTTS.setLanguage(new Locale("HI"));
	    }
	    else if (initStatus == TextToSpeech.ERROR) {
	        Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
	    }
	}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.display, menu);
			return true;
		}

	}
