package com.example.login;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GspeakActivity extends Activity {
	Intent i;
	Button communicate,train,shortcut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gspeak);
		communicate=(Button)findViewById(R.id.button1);
		train=(Button)findViewById(R.id.Button01);
		shortcut=(Button)findViewById(R.id.Button02);
		communicate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				i=new Intent(getApplicationContext(),HelloViewfinderActivity.class);
				Bind.training = false;
				
				startActivity(i);
				}
		})
		;
shortcut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				i=new Intent(getApplicationContext(),HelloViewfinderActivity.class);
				Bind.training = false;
				Bind.shortcut =true;
				startActivity(i);
				
			}
		})
		;
		
train.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		i=new Intent(getApplicationContext(),HelloViewfinderActivity.class);
		Bind.training = true;
		startActivity(i);
	}
});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gspeak, menu);
		return true;
	}

}
