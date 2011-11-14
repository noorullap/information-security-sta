package com.sta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class STAStartActivity extends Activity implements OnClickListener{
	private static final String TAG = "STAStart";
	private static final String LOGIN_DEF = "root";
	private static final String PIN_DEF = "1234";
	private static final String IPASSW_DEF = "default";
	private static String FileName = "aaas.txt";
	public static final int CONNECT_OPT_ID = Menu.FIRST;
	public static final int CLEAR_D_ID = Menu.FIRST+1;
	
	private HTTPConnection connection = null;
	

	private EditText mLoginText, mPINText, mIPasswText;

    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_win);
		setTitle(R.string.app_name);
				
		mLoginText = (EditText) findViewById(R.id.login);
		mPINText = (EditText) findViewById(R.id.pin);
		mIPasswText = (EditText) findViewById(R.id.init_passw);
		
		/** Button "Sign-in" */
		Button signinButton = (Button) findViewById(R.id.sign_in);
		signinButton.setOnClickListener( this);
		
		/** Button "Initial registration" */
		Button initregButton = (Button) findViewById(R.id.init_reg);
		initregButton.setOnClickListener( this);

	}

	/** Handling pressing buttons */
	public void onClick( View view){
		switch ( view.getId() ){
		
		case R.id.sign_in:
			Log.d(TAG, "onClick: signing-in");
			start_signin();
			break;

		case R.id.init_reg:
			Log.d(TAG, "onClick: initial registration");
			start_initreg();
			break;
		
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, CONNECT_OPT_ID, 0, R.string.prefs);
		menu.add(0, CLEAR_D_ID, 0, R.string.clear_debug);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CLEAR_D_ID:
			clearKey();
			return true;
		case CONNECT_OPT_ID:
			startPreferences();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void start_signin() {
		Log.d(TAG, "getting instance ");

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String ip_server = settings.getString( getString(R.string.server_host), "127.0.0.1");
		connection = HTTPConnection.getInstance( ip_server);
		Log.d(TAG, "instance has been got" + ip_server);

		String login = mLoginText.getText().toString();
		String pin = mPINText.getText().toString();

		/* Here you can talk with server */
		try {
			
			connection.sendMessage(login);
			Log.d(TAG, "sign_in: message has been sent: ");

		
			Log.d(TAG, "sign_in: Starting receive message: ");
			String str = connection.receiveMessage();
			Log.d(TAG, "sign_in: Received message: " + str);
			
			mLoginText.setText(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

		if (LOGIN_DEF.equals(login) && PIN_DEF.equals(pin)) {
			Log.d(TAG, "sign_in: authentication done");
			startActivity(new Intent(this, STAMainActivity.class));
		} else {
			Log.d(TAG, "sign_in: incorrect login or password");
			Log.d(TAG, "sign_in: login \"" + login + "\"");
			Log.d(TAG, "sign_in: pin \"" + pin + "\"");
			Toast.makeText(this, "Incorrect login or PIN", Toast.LENGTH_LONG).show();
		}
	}

	private void start_initreg() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String ip_server = settings.getString( getString(R.string.server_host), "127.0.0.1");
		
		connection = HTTPConnection.getInstance( ip_server);

		String login = mLoginText.getText().toString();
		String init_passw = mIPasswText.getText().toString();

		/* Here you can talk with server */
		
		try {
			
			connection.sendMessage(login);
			Log.d(TAG, "sign_in: message has been sent: ");

		
			Log.d(TAG, "sign_in: Starting receive message: ");
			String str = connection.receiveMessage();
			Log.d(TAG, "sign_in: Received message: " + str);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (LOGIN_DEF.equals(login) && IPASSW_DEF.equals(init_passw)) {
			Log.d(TAG, "init_reg: login and initial password are correct");
			Date time = new Date();
			String text = "Auto-generated file at " + time.toString();
			FileOutputStream fos;
			try {
				fos = openFileOutput(FileName, Context.MODE_PRIVATE);
				fos.write(text.getBytes());
				fos.close();
				Log.d(TAG, "init_reg: write successful");
				Toast.makeText(this, "Initial registration is done", Toast.LENGTH_LONG).show();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "init_reg: Could not create " + FileName);
				e.printStackTrace();
			} catch (IOException e) {
				Log.d(TAG, "init_reg: Could not write information to file " + FileName);
				e.printStackTrace();
			}
		} else {
			Log.d(TAG, "init_reg: incorrect login or password");
			Log.d(TAG, "init_reg: login \"" + login + "\"");
			Log.d(TAG, "init_reg: init_passw \"" + init_passw + "\"");
			Toast.makeText(this, "Incorrect login or initial password", Toast.LENGTH_LONG).show();
		}

	}
	

/*
 * This functionality added for delete all keys in phone memory for simulating
 * new client. 
 */
	private void clearKey() {
		File key_file = new File( FileName);
		
		if ( key_file.exists()){
			Toast.makeText(this, "File is existed", Toast.LENGTH_LONG);
		}
			
		if ( deleteFile(FileName)) {
			Toast.makeText(this, "Clear is done", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Clear is not done", Toast.LENGTH_LONG).show();
		}
	}
	
	public void startPreferences() {
        Intent i = new Intent(this, SettingScreen.class);
        startActivity(i);
	}

	
}