package com.example.login;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetActivity extends Activity {
	

	public Button ok ;
	public EditText email;
	
	SQLiteDatabase gspeak;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		email = (EditText)findViewById(R.id.editText1);
		ok = (Button)findViewById(R.id.button1);
		
		final Intent mIntent = new Intent(this, PwdActivity.class);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String eid = email.getText().toString();		
				gspeak = openOrCreateDatabase("gspeak", MODE_PRIVATE, null);
				if(RegistrationActivity.emailValidator(eid)){
			//		Toast.makeText(getApplicationContext(), "valid email address", 3000).show();
					Cursor resultset = gspeak.rawQuery("select emailid, squ , seqans from user where emailid='"+email.getText().toString()+"';",null);
					resultset.moveToFirst();
					String emailid = resultset.getString(0).toString();
					String sequ = resultset.getString(1).toString();
					String sequans = resultset.getString(2).toString();
					Bundle mBundle = new Bundle();
					mBundle.putString("que", sequ);
					mBundle.putString("email", eid);
					mIntent.putExtras(mBundle);
					gspeak.close();
					finish();
					startActivity(mIntent);
					
				}else{
					Toast.makeText(getApplicationContext(), "invalid email address", 3000).show();
				}
				gspeak.close();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forget, menu);
		return true;
	}

}
