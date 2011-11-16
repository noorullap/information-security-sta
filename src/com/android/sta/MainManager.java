package com.android.sta;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainManager {
	private static final String TAG = "STA.MainManager";
	private static final int DEFAULT_MODE = 0;
	private static final int SIGNIN_MODE = 1;
	private static final int REGISTER_MODE = 2;
	private static final String LOGIN_DEF = "root";
	private static final String PIN_DEF = "1234";
	private static final String INIT_PASSW_DEF = "default";
	private static final String FILENAME = "aaas.txt";
	private boolean isOnline, res;
	private String login, pin, init_passw;
	private int mode = DEFAULT_MODE;
	private Context context;
	private HTTPConnection connection = null;
	
	public void setForSigningIn( Context context, String login, 
								 String pin, boolean isOnline){
		this.login = login;
		this.pin = pin;
		this.isOnline = isOnline;
		this.context = context;
		mode = SIGNIN_MODE;
	}
	
	public void setForRegister( Context context, String login, 
								String init_passw, boolean isOnline){
		this.login = login;
		this.init_passw = init_passw;
		this.isOnline = isOnline;
		this.context = context;
		mode = REGISTER_MODE;
	}
	
	public boolean start(){
		res = true;
		switch ( mode ){
		case SIGNIN_MODE:
			connectToServer();
			signIn();
			receiveNewSetPassw();
			receiveAccountData();
			startMainScreen();
			break;
		case REGISTER_MODE:
			connectToServer();
			register();
			receiveNewSetPassw();
			receiveAccountData();
			startMainScreen();
			break;
		case DEFAULT_MODE:
			Log.e( TAG, "MainManager started without settings options");
			return false;
		default:
			Log.e( TAG, "assert detection in MainManager::start ");
			res = false;
			
		}
		
		return res;
		
	}
	
	private void startMainScreen() {
		if (res)
			context.startActivity(new Intent(context, MainScreen.class));
	}

	private void receiveAccountData() {
		/* TODO: implement receiving account information */
		if ( isOnline){
			
		} else {
			
		}
	}

	private void connectToServer(){
		if ( isOnline){
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
			String ip_server = settings.getString( context.getString(R.string.server_host), "127.0.0.1");
			int port_server = settings.getInt(context.getString(R.string.server_port), 45000);
			Log.d( TAG, "connecting to "+ip_server+":"+port_server);
			connection = HTTPConnection.getInstance( ip_server);
		} else{
			Log.d( TAG, "offline mode: connection");
		}
	}
	
	private void signIn(){
		/* TODO: implement exceptions, in case of incorrect login */
		if ( isOnline ){
			try {
				Log.d(TAG, "sending login");
				connection.sendMessage(login);
				/* TODO: implement receiving confirmation from server */
			} catch (Exception e) {
				e.printStackTrace();
				res = false;
			}
		} else {
			if ( login.equals( LOGIN_DEF)){
				Log.d( TAG, "offline mode: login is correct");
			} else {
				Log.d( TAG, "offline mode: incorrect login");
				res = false;
			}
				
		}
	}
	
	private void register(){
		/* TODO: implement exceptions, in case of incorrect initial password */
		signIn();
		if ( isOnline ){
			try {
				Log.d( TAG, "sending initial password");
				connection.sendMessage(init_passw);
				/* TODO: implement receiving confirmation from server */
			} catch (Exception e) {
				e.printStackTrace();
				res = false;
			}
		} else {
			if ( init_passw.equals( INIT_PASSW_DEF) ){
				Log.d( TAG, "offline mode: initial password is correct");
			} else {
				Log.d( TAG, "offline mode: incorrect inital password");
				res = false;
			}
		}
	}
	
	private void receiveNewSetPassw(){
		if ( isOnline ){
			try {
				Log.d(TAG, "receiving new set of passwords");
				String sPassw = connection.receiveMessage();
				Log.d(TAG, "received new set of passwords");
				createKeyFile(sPassw);
			} catch ( Exception e) {
				e.printStackTrace();
				res = false;
			}
		} else {
			Date time = new Date();
			String sPassw = time.toString();
			Log.d( TAG, "offline mode: received new set of passwords");
			createKeyFile( sPassw);
		}
	}
	
	private void createKeyFile( String text){
		FileOutputStream fos;
		Log.d( TAG, "writing key file");
		try {
			fos = context.openFileOutput( FILENAME, Context.MODE_PRIVATE);
			fos.write( text.getBytes());
			fos.close();
			Log.d(TAG, "write successful");
		} catch (FileNotFoundException e) {
			Log.e(TAG, "createKeyFile: File could not create");
			e.printStackTrace();
			res = false;
		} catch (IOException e){
			Log.e(TAG, "createKeyFile: could not write SetPasswords to file");
			e.printStackTrace();
			res = false;
		}
	}
}
