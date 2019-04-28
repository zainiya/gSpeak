package com.example.login;

import android.R.color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public EditText usname;
	public EditText paswd;
	public Button login;
	public Intent int1;
	public TextView forpwd;
	public TextView reg;
	public SQLiteDatabase gspeak;
	@SuppressWarnings("deprecation")
	public SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), MODE_WORLD_READABLE);
		usname = (EditText) findViewById(R.id.uname);
		paswd = (EditText) findViewById(R.id.pswd);
		login = (Button) findViewById(R.id.button1);
		forpwd = (TextView) findViewById(R.id.textView3);
		forpwd.setText(Html.fromHtml("<u>" + "Forget Password?" + "</u>"));
		forpwd.setTextColor(Color.BLUE);
		reg = (TextView) findViewById(R.id.textView4);
		reg.setText(Html.fromHtml("<u>" + "Register Here" + "</u>"));
		reg.setTextColor(Color.BLUE);
		final Intent int1 = new Intent(this, GspeakActivity.class);
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sharedPref = getSharedPreferences(
						getString(R.string.preference_file_key), MODE_WORLD_READABLE);
				if (validateUser()) {
					Toast.makeText(getApplicationContext(),
							"Successfully loged in!!", 1000).show();

					SharedPreferences.Editor prefEditor = sharedPref.edit();
					prefEditor.putString("User", ""
							+ usname.getText().toString());
					prefEditor.commit();
					Toast.makeText(getApplicationContext(), "Pref. Created!!",
							1000).show();
					startActivity(int1);
				} else {
					Toast.makeText(getApplicationContext(),
							"Wrong ID or Password!!", 3000).show();
				}
			}
		});

		reg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				register();
				// TODO Auto-generated method stub
			}
		});

		forpwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				forgotPswd();
			}
		});
	}

	public void forgotPswd() {
		final Intent forget = new Intent(this, ForgetActivity.class);
		startActivity(forget);
	}

	public void register() {
		final Intent regint = new Intent(this, RegistrationActivity.class);
		startActivity(regint);
	}

	public boolean validateUser() {
		gspeak = openOrCreateDatabase("gspeak", MODE_PRIVATE, null);
		gspeak.execSQL("create table if not exists user(uid int,uname varchar, pswd varchar, emailid varchar,squ varchar, seqans varchar);");
		Cursor resultset = gspeak.rawQuery(
				"select pswd from user where uname='"
						+ usname.getText().toString() + "';", null);

		resultset.moveToFirst();
		String pswd = paswd.getText().toString();
		String pass;
		// test case 2 start
		try {
			pass = resultset.getString(resultset.getColumnIndex("pswd"));
		} catch (Exception e) {
			return false;
		}
		resultset.close();
		gspeak.close();
		if (pass.equals(pswd)) {
			return true;
		} else {
			return false;
		}
		// test case 2 end
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
