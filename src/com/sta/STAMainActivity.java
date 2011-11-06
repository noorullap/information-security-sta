package com.sta;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;	

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class STAMainActivity extends Activity {
	private static final String TAG = "STAStart";
	private static String FileName = "aaas.txt";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main_win);
        setTitle(R.string.app_name);
        byte[] inputBuffer = new byte[1000];      
        TextView mText = (TextView) findViewById( R.id.maintext); 
        FileInputStream fis;
		
        try {
			fis = openFileInput( FileName);
	        fis.read(inputBuffer);
	        fis.close();
	        Log.d(TAG, "read successful");
	    } catch (FileNotFoundException e) {
	    	Log.d( TAG, "File "+FileName+" not found");
	    	e.printStackTrace();
		} catch (IOException e) {
	    	Log.d( TAG, "Could not read information from file "+FileName);
	    	e.printStackTrace();
		}


        String read = new String(inputBuffer);
     	mText.setText( read);
	}

}
