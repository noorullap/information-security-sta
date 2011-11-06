package com.sta;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class STAStartActivity extends Activity {
	private static final String TAG = "STAStart";
	private static final String LOGIN_DEF = "root";
	private static final String PIN_DEF = "1234";
	private static final String IPASSW_DEF = "default";
	private static String FileName = "aaas.txt";
	public static final int CLEAR_D_ID = Menu.FIRST;

	private EditText mLoginText, mPINText, mIPasswText;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_win);
        setTitle( R.string.app_name);
        mLoginText = (EditText) findViewById( R.id.login);
        mPINText = (EditText) findViewById( R.id.pin);
        mIPasswText = (EditText) findViewById( R.id.init_passw);
        Button signinButton = (Button) findViewById(R.id.sign_in);
        Button initregButton = (Button) findViewById(R.id.init_reg);

        signinButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(TAG, "onClick: signing-in");
            	start_signin();
            }
        });
        
        initregButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(TAG, "onClick: initial registration");
            	start_initreg();
            }
        });
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, CLEAR_D_ID, 0, R.string.clear_debug);
        
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case CLEAR_D_ID:
    		clear_debug();
    		return true;
    	}
        return super.onOptionsItemSelected(item);
    }

    private void start_signin(){
    	String login = mLoginText.getText().toString();
    	String pin = mPINText.getText().toString();
    	
    	if ( login.intern() == LOGIN_DEF.intern() 
    		 && pin.intern() == PIN_DEF.intern() ){
            Log.d(TAG, "sign_in: authentication done");
    		startActivity( new Intent(this, STAMainActivity.class));
    	} else {
            Log.d(TAG, "sign_in: incorrect login or password");
            Log.d(TAG, "sign_in: login \""+login+"\"");
            Log.d(TAG, "sign_in: pin \""+pin+"\"");
            Toast.makeText(this, "Incorrect login or PIN", Toast.LENGTH_LONG).show();
    	}
    }
    
    private void start_initreg(){
    	String login = mLoginText.getText().toString();
    	String init_passw = mIPasswText.getText().toString();
    	
    	if ( login.intern() == LOGIN_DEF.intern() 
    		 && init_passw.intern() == IPASSW_DEF.intern() ){
            Log.d(TAG, "init_reg: login and initial password are correct");
            Date time = new Date();
            String text = "Auto-generated file at "+time.toString();
            FileOutputStream fos;
			try {
				fos = openFileOutput( FileName, Context.MODE_PRIVATE);
	            fos.write( text.getBytes());
	            fos.close();
	            Log.d(TAG, "init_reg: write successful");
	            Toast.makeText(this, "Initial registration is done", Toast.LENGTH_LONG).show();
	        } catch (FileNotFoundException e) {
		    	Log.d( TAG, "init_reg: Could not create "+FileName);
		    	e.printStackTrace();
			} catch (IOException e) {
		    	Log.d( TAG, "init_reg: Could not write information to file "+FileName);
		    	e.printStackTrace();
			}
    	} else {
            Log.d(TAG, "init_reg: incorrect login or password");
            Log.d(TAG, "init_reg: login \"" + login + "\"");
            Log.d(TAG, "init_reg: init_passw \"" + init_passw + "\"");
            Toast.makeText(this, "Incorrect login or initial password", Toast.LENGTH_LONG).show();
    	}

    }
    
    private void clear_debug(){
    	Toast.makeText(this, "Clear is not done", Toast.LENGTH_LONG).show();
    	return;
    }
}