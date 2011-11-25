package com.android.sta;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class MainManager extends Object{
	private static final String TAG = "STA.MainManager";
	private static final int DEFAULT_MODE = 0;
	private static final int SIGNIN_MODE = 1;
	private static final int REGISTER_MODE = 2;
	private static final String LOGIN_DEF = "root";
	private static final String PIN_DEF = "1234";
	private static final String INIT_PASSW_DEF = "default";
	private static final String FILENAME = "aaas.txt";
	private List<String> accountNumbers = null; // in future
	private String accountNumber = null;
	private String balance = null;
	private String keyString = null;
	private boolean isOnline, res;
	private String login, pin, init_passw;
	private int mode = DEFAULT_MODE;
	private static MainManager instance = new MainManager();
	
	private Context context;
	private HTTPConnection connection = null;
	
	private MainManager() {
		
	}
	
	public static MainManager getInstance() {
		return instance;
	}
	
	public String getBalanceFromServer(String accountNumber) { //in future
		String ret = null;
		
		if (connection != null) {
			
			try {
				connection.sendMessage("2 " + accountNumber + " ");

				String ans = connection.receiveMessage();

				Pattern pat = Pattern.compile("(2)\\s([0-9]*)");
				Matcher mat = pat.matcher(ans);

				if (mat.matches()) {
					ret = mat.group(2);
				}

			} catch (IOException e) {
				Log.e( TAG, "server isn't connected::getBalanceFromServer");
				e.printStackTrace();
			}

		}
		
		return ret;
	}
	
	public boolean transferMoney(String source, String dest, String sum) {
		boolean ret = false;
		
		if (connection != null) {

			try {
				connection.sendMessage("3 " + source + " " + dest + " " + sum + " ");

				String ans = connection.receiveMessage();
				
				Log.d(TAG, "connection.receiveMessage():"+ ans);

				Pattern pat = Pattern.compile("(3)\\s(true|false)");
				Matcher mat = pat.matcher(ans);

				if (mat.matches()) {
					String trueOrFalse = mat.group(2);
					Log.d(TAG, "trueOrFalse  "+trueOrFalse);
					if( trueOrFalse.equals("true") ) {
						ret = true;
					}
				}

			} catch (IOException e) {
				Log.d(TAG, "server isn't connected::getBalanceFromServer");
				e.printStackTrace();
			}

		}

		return ret;
	}
	
	public String getAccountNumberFromServer() {
		
		if (connection != null) {

		}
		return accountNumber;
	}
	
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
			//receiveNewSetPassw();
			//receiveAccountData();
			startMainScreen();
			break;
		case REGISTER_MODE:
//			connectToServer();
//			register();
//			receiveNewSetPassw();
//			receiveAccountData();
//			startMainScreen();
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
		{
			Intent intent = new Intent();
			intent.setClass( context, MainScreen.class);
			
			intent.putExtra( MainScreen.EXT_ACCOUNT, accountNumber);
			context.startActivity( intent);
		} else{
			Toast.makeText(context, "Incorrect login or password", Toast.LENGTH_LONG).show();
		}
			
	}

	private void receiveAccountData() {
		/* TODO: implement receiving account information */
		if ( isOnline){
			
		} else {
			
		}
	}

	private void connectToServer(){
		if ( isOnline){
//			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//			String ip_server = settings.getString( context.getString(R.string.server_host), "127.0.0.1");
//			int port_server = settings.getInt(context.getString(R.string.server_port), 45000);
//			Log.d( TAG, "connecting to "+ip_server+":"+port_server);
//			connection = HTTPConnection.getInstance( ip_server);
			connection = HTTPConnection.getInstance( "123");
		} else{
			Log.d( TAG, "offline mode: connection");
		}
	}
	
	private void signIn(){
		/* TODO: implement exceptions, in case of incorrect login */
		if ( isOnline ){
			try {
				
				Log.d(TAG, "sending login");

				
				connection.sendMessage("0 " + login + " " + pin + " ");
				/* TODO: implement receiving confirmation from server */
				
				
				Log.d(TAG, "receiving answer from server(success or not)");
				String ans = connection.receiveMessage();
				Log.d(TAG, "received ans "+ans);
				
				
				Pattern pat = Pattern.compile("(0)\\s(true|false)");
				Matcher mat = pat.matcher(ans);

				if (mat.matches()) {
					String trueOrFalse = mat.group(2);
					Log.d(TAG, "trueOrFalse  "+trueOrFalse);
					if( trueOrFalse.equals("false") ) {
						res = false;
					} else {
						
						
						
						//SSL here
						
						
						
						//request for keyString
						
						connection.sendMessage("1 " + login + " ");
						
						Log.d(TAG, "receiving reyString from the server");
						String ans2 = connection.receiveMessage();
						Log.d(TAG, "received ans "+ans2);
						
						
						Pattern pat2 = Pattern.compile("(1)\\s(.*)"); // \\w
						Matcher mat2 = pat2.matcher(ans2);
						
						if (mat2.matches()) {
							keyString = mat2.group(2);
							Log.d(TAG, "keyString  "+keyString);
						}
						
					}
				}
				
				
//				createKeyFile(sPassw);

				
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
	
	public String getBalance() {
		return balance;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	
}
