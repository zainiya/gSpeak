package com.example.login;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TtsActivity extends Activity implements OnClickListener,OnInitListener {
	  private TextToSpeech myTTS;
	  public SQLiteDatabase gspeak;
	  Preprocessing ps;
    //status check code
private int MY_DATA_CHECK_CODE = 0;
String w;
public void onCreate(Bundle savedInstanceState) {
	     
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tts);
/*    gspeak = openOrCreateDatabase("gspeak", MODE_PRIVATE, null);
    final Cursor resultset = gspeak.rawQuery("select text from train where numfing ="+ps.countfing+";",null);
    try{		
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
}	  
  */  //Intent in  = getIntent();
    w = getIntent().getExtras().getString("word");
    Log.i("Word",""+w);
            //get a reference to the button element listed in the XML layout
                //listen for clicks
    Button speakButton = (Button)findViewById(R.id.button1);
    //listen for clicks
speakButton.setOnClickListener(this);

               //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        
}
 
    //respond to button clicks
public void onClick(View v) {

        //get the text entered
        //EditText enteredText = (EditText)findViewById(R.id.enter);
        //String words = enteredText.getText().toString();
        speakWords(w);
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
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
  }

	
	
  
}
