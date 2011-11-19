package com.android.sta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class StartScreen extends Activity implements OnClickListener{
	private static final String TAG = "STA.StartScreen";
	private static String FileName = "aaas.txt";
	public static final int CONNECT_OPT_ID = Menu.FIRST;
	public static final int CLEAR_D_ID = Menu.FIRST+1;
	
	private EditText mLoginText, mPINText, mIPasswText;
	private CheckBox mOfflineCheckBox; 
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_win);
		setTitle(R.string.app_name);
				
		mLoginText = (EditText) findViewById(R.id.login);
		mPINText = (EditText) findViewById(R.id.pin);
		mIPasswText = (EditText) findViewById(R.id.init_passw);
		mOfflineCheckBox = (CheckBox) findViewById(R.id.offline_mode);
		
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
			startSignIn();
			break;

		case R.id.init_reg:
			Log.d(TAG, "onClick: initial registration");
			startInitReg();
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

	private void startSignIn() {
		String login = mLoginText.getText().toString();
		String pin = mPINText.getText().toString();

		MainManager mMainManager = MainManager.getInstance();
		
		mMainManager.setForSigningIn( this, login, pin, 
									  !mOfflineCheckBox.isChecked());
		
		if ( mMainManager.start())
		{
			
		}
		else {
			Toast.makeText(this, "Incorrect login or PIN", Toast.LENGTH_LONG).show();
		}

	}

	private void startInitReg() {
		String login = mLoginText.getText().toString();
		String init_passw = mIPasswText.getText().toString();

		MainManager mMainManager = MainManager.getInstance();
		
		mMainManager.setForSigningIn( this, login, init_passw, 
									  !mOfflineCheckBox.isChecked());
		
		if ( mMainManager.start())
		{
			
		}
		else {
			Toast.makeText(this, "Incorrect login or initial password", Toast.LENGTH_LONG).show();
		}
		

	}
	
/**
 * This functionality added for delete all keys in phone memory for simulating
 * new client. 
 */
	private void clearKey() {
		
		if ( deleteFile(FileName)) {
			Toast.makeText(this, "Clear is done", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Clear is not done", Toast.LENGTH_LONG).show();
		}
	}
	
/**
 * String setting screen 
 */
	public void startPreferences() {
        Intent i = new Intent(this, SettingScreen.class);
        startActivity(i);
	}

	
}