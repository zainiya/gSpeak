package com.example.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	public EditText usname;
	public String usname1="";
	public EditText pswd;
	public String pswd1="";
	public EditText email;
	public String email1="";
	public Spinner sequ;
	public EditText sequans;
	public String sequans1="";
	public Button submit;
	public SQLiteDatabase gspeak;
	public static int count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		usname = (EditText)findViewById(R.id.uname);
		pswd = (EditText)findViewById(R.id.pswd);
		email =(EditText)findViewById(R.id.email);
		sequ = (Spinner)findViewById(R.id.spinner1);
		sequans = (EditText)findViewById(R.id.seqans);
		submit = (Button)findViewById(R.id.button1);
		final Intent login = new Intent(this, MainActivity.class);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				usname1=usname.getText().toString();
				pswd1=pswd.getText().toString();
				email1=email.getText().toString();
				sequans1=sequans.getText().toString();
				gspeak = openOrCreateDatabase("gspeak", MODE_PRIVATE, null);
				count++;
				gspeak.execSQL("create table if not exists user(uid int,uname varchar, pswd varchar, emailid varchar,squ varchar, seqans varchar);");
				Cursor resultset = gspeak.rawQuery("select max(uid) from user;",null);
				resultset.moveToFirst();
				count = resultset.getInt(0);
				count = count+1;
				if(validateFields()){
					doRegister();
					gspeak.close();
					finish();
				}
			}
		});
	}
	public boolean validateFields()
	{
		Log.i("gSpeak", usname1.toString()+" "+pswd1.toString()+" "+email1.toString()+" "+sequans1.toString());
		if(!(usname1.equals("") || pswd1.equals("") || email1.equals("") || sequans1.equals("")))
		{
			if(emailValidator(email.getText().toString()))
			{
				return true;
			}
			else 
			{
				Toast.makeText(getApplicationContext(), "invalid email address", 3000).show();
				return false;
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Fill all the fields", 3000).show();
			return false;
		}
	}
	public void doRegister()
	{
		gspeak.execSQL("insert into user values('"+count+"','"+usname1+"','"+pswd1+"','"+email+"','"+sequ.getSelectedItem().toString()+"','"+sequans.getText().toString()+"');");
		Toast.makeText(getApplicationContext(), "done="+count, 3000).show();
	}
	public static boolean emailValidator(final String mailAddress) {
         Pattern pattern;
         Matcher matcher;
         final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
         pattern = Pattern.compile(EMAIL_PATTERN);
         matcher = pattern.matcher(mailAddress);
         return matcher.matches();
     }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
}
