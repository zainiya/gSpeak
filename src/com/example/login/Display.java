package com.example.login;

import java.util.ArrayList;
import java.util.Locale;

import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class Display extends Activity implements OnClickListener,
		OnInitListener {
	ImageView iv0;
	Bitmap b;
	Intent ttsintent;
	private TextToSpeech myTTS;
	Preprocessing ps = new Preprocessing();
	SQLiteDatabase gspeak;
	String text;
	SharedPreferences sharedPref;
	// status check code
	private int MY_DATA_CHECK_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		Intent checkTTSIntent = new Intent();

		sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), MODE_WORLD_READABLE);

		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
		Log.i("activity", "hello");
		iv0 = (ImageView) findViewById(R.id.imageView1);
		Button speakButton = (Button) findViewById(R.id.button1);
		speakButton.setOnClickListener(this);
		b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("b"),
				0, getIntent().getByteArrayExtra("b").length);
		iv0.setImageBitmap(ps.eliminateBackground(b));
		EditText t1 = (EditText) findViewById(R.id.editText1);
		Log.i("sooo", "here we Are..");
		gspeak = openOrCreateDatabase("gspeak", MODE_PRIVATE, null);

		final Cursor resultset = gspeak.rawQuery(
				"select text from train where numfing=" + ps.countfing
						+ " and user='" + sharedPref.getString("User", "")
						+ "';", null);
		String tmp = "select text from train where numfing =" + ps.countfing
				+ " and user='" + sharedPref.getString("User", "") + "';";
		Log.i("Jayesh", tmp);
		Toast.makeText(getApplicationContext(),
				"You " + sharedPref.getString("User", ""), 3000).show();
		//resultset.close();
		//gspeak.close();
		try {
			if (resultset.moveToFirst()) {
				text = resultset.getString(0);
				t1.setText("" + text);
			} else {
				Toast.makeText(
						getApplicationContext(),
						"You have inserted a new Gesture!! "
								+ sharedPref.getString("User", ""), 3000)
						.show();
			}
		} catch (Exception e) {
			Log.i("exception", "" + e);
		}
		finally{
			resultset.close();
			gspeak.close();
			
		}
	}

	public void onClick(View v) {

		// get the text entered
		// EditText enteredText = (EditText)findViewById(R.id.enter);
		// String words = enteredText.getText().toString();
		speakWords(text);
	}

	// speak the user text
	private void speakWords(String speech) {

		// speak straight away
		myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
	}

	// act on result of TTS data check
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// the user has the necessary data - create the TTS
				myTTS = new TextToSpeech(this, this);
			} else {
				// no data - install it now
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
	}

	// setup TTS
	public void onInit(int initStatus) {

		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			// if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
			myTTS.setLanguage(new Locale("HI"));
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

}
