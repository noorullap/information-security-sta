package com.sta;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class TokenlessAuthenticationActivity extends Activity {
	public static final int CONNECT_ID = Menu.FIRST;
	public static final int WRITE_ID = Menu.FIRST + 1;
	public static final int READ_ID = Menu.FIRST + 2;

	private EditText mBodyText;
	private String FileName = "aaas.txt";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_win);
        setTitle( R.string.app_name);
        mBodyText = (EditText) findViewById( R.id.body);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, CONNECT_ID, 0, R.string.sign_in);
        menu.add(0, WRITE_ID, 0, R.string.write);
        menu.add(0, READ_ID, 0, R.string.read);
        
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	try {
    	switch (item.getItemId()) {
        case CONNECT_ID:
            connect_to_server();
            return true;
        case WRITE_ID:
        	writeFile(FileName);
        	return true;
        case READ_ID:
        	readFile(FileName);
        }
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return super.onOptionsItemSelected(item);
    }

    private void connect_to_server(){
    	return;
    }
    
    private void writeFile(String path) throws IOException{
        String body = mBodyText.getText().toString();
        FileOutputStream fos = openFileOutput( path, Context.MODE_PRIVATE);
        fos.write( body.getBytes());
        fos.close();
        mBodyText.setText("done!");
        return;
    }
    
    private void readFile( String path) throws IOException{
        FileInputStream fis = openFileInput( path);
        byte[] inputBuffer = new byte[1000];
        fis.read(inputBuffer);
        fis.close();
        String read = new String(inputBuffer);
     	mBodyText.setText( read);
    }
    
}