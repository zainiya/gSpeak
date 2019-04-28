package com.example.login;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PwdActivity extends Activity {
TextView sequ;
EditText ans;
SQLiteDatabase gspeak;
Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd);
		sequ = (TextView)findViewById(R.id.textView1);
		String ques = getIntent().getExtras().getString("que");
		final String email = getIntent().getExtras().getString("email");
		ans = (EditText)findViewById(R.id.editText1);
		sequ.setText(ques);
		submit = (Button)findViewById(R.id.button1);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gspeak = openOrCreateDatabase("gspeak", MODE_PRIVATE, null);
				Cursor resultset = gspeak.rawQuery("select seqans, pswd from user where squ='"+sequ.getText().toString()+"' and emailid='"+email+"';",null);
				resultset.moveToFirst();
				String rans = resultset.getString(0).toString();
				String pswd = resultset.getString(1).toString();
				if(ans.getText().toString().equals(rans)){
					Toast.makeText(getApplicationContext(), pswd , 3000).show();
				}else 
				{
					Toast.makeText(getApplicationContext(), "You have given an incorrect answer!!! "+ ans.getText().toString() , 3000).show();
					
				}
						gspeak.close();
						finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pwd, menu);
		return true;
	}

}
