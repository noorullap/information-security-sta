package com.android.sta;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
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
	private SSLConnection connection = null;
	
	private MainManager() {
		
	}
	
	public Context getContext() {
		return context;
	}
	
	public static MainManager getInstance() {
		return instance;
	}
	
	public String getBalanceFromServer(String accountNumber) { //in future
		String ret = null;
		
		if (connection != null) {
			
			try {
				String send = new String(XorEncryption.Encrypt("2".getBytes(), Long.valueOf(init_passw)));
				connection.sendMessage(send);

				Log.e( TAG, "Start receiving balance");
				String ans = connection.receiveMessage();
				Log.e( TAG, "End receiving balance");
				
				ans = new String( XorEncryption.Decrypt(ans.getBytes(), Long.valueOf(init_passw)));

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
				String send = "3 " + dest + " " + sum + " ";
				send = new String(XorEncryption.Encrypt(send.getBytes(), Long.valueOf(init_passw)));
				connection.sendMessage(send);

				String ans = connection.receiveMessage();
				ans = new String( XorEncryption.Decrypt(ans.getBytes(), Long.valueOf(init_passw)));
				
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
			connectToServer();
			registerInit();
//			receiveNewSetPassw();
//			receiveAccountData();
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
			connection = SSLConnection.getInstance();

		} else{
			Log.d( TAG, "offline mode: connection");
		}
	}
	
	private void signIn(){
		/* implement exceptions, in case of incorrect login */
		if ( isOnline ){
			try {
//				
//				Log.d(TAG, "sending login");
//
//				
//				connection.sendMessage("0 " + login + " " + pin + " ");
//				/*  implement receiving confirmation from server */
//				
//				
//				Log.d(TAG, "receiving answer from server(success or not)");
//				String ans = connection.receiveMessage();
//				Log.d(TAG, "received ans "+ans);
//				
//				
//				Pattern pat = Pattern.compile("(0)\\s(true|false)");
//				Matcher mat = pat.matcher(ans);
//
//				if (mat.matches()) {
//					String trueOrFalse = mat.group(2);
//					Log.d(TAG, "trueOrFalse  "+trueOrFalse);
//					if( trueOrFalse.equals("false") ) {
//						res = false;
//					} else {
//						
//						
//						
//						//SSL here
//						
//						
//						
//						//request for keyString
//						
//						connection.sendMessage("1 " + login + " ");
//						
//						Log.d(TAG, "receiving reyString from the server");
//						String ans2 = connection.receiveMessage();
//						Log.d(TAG, "received ans "+ans2);
//						
//						
//						Pattern pat2 = Pattern.compile("(1)\\s(.*)"); // \\w
//						Matcher mat2 = pat2.matcher(ans2);
//						
//						if (mat2.matches()) {
//							keyString = mat2.group(2);
//							Log.d(TAG, "keyString  "+keyString);
//						}
//						
//					}
//				} else {
//					res = false;
//					Log.d(TAG, "bad string "+ans);
//				}
//				
				
//				createKeyFile(sPassw);

				
				Log.d(TAG, "sending login");

				
				connection.sendMessage("0 " + login + " ");
				/*  implement receiving confirmation from server */
				
				
				Log.d(TAG, "receiving answer from server(success or not)");
				String ans = connection.receiveMessage();
				Log.d(TAG, "received ans "+ans);
				
				
				Pattern pat = Pattern.compile("(0)\\s(true\\(s[0-9]+)|false)");
				Matcher mat = pat.matcher(ans);

				if (mat.matches()) {
					String trueOrFalse = mat.group(2);
					Log.d(TAG, "trueOrFalse  "+trueOrFalse);
					if( trueOrFalse.equals("false") ) {
						res = false;
					} else {
						
						
		/* New init_pass here and reading the file
						mat.group(3)
						
						
		*/				
						int N = Integer.valueOf(mat.group(3));
						Log.d(TAG, "N = "+N);
						
						String str = readFile();
						init_passw = str.substring(N, N + 10);
						
						
						//request for a new keyString
						
						connection.sendMessage(
								new String(XorEncryption.Encrypt("1".getBytes(), Long.valueOf(init_passw)))
								);
						
						Log.d(TAG, "receiving keyString from the server");
						String ans2 = connection.receiveMessage();
						ans2 = new String( XorEncryption.Decrypt(ans2.getBytes(), Long.valueOf(init_passw)));
						
						Log.d(TAG, "received ans "+ans2);
						
						
						Pattern pat2 = Pattern.compile("(1)\\s(.*)"); // \\w
						Matcher mat2 = pat2.matcher(ans2);
						
						if (mat2.matches()) {
							keyString = mat2.group(2);
							createKeyFile(keyString);
							keyString = new String(
									XorEncryption.Decrypt(keyString.getBytes(), Long.valueOf(pin))
									);
							Log.d(TAG, "keyString  "+keyString);
						}
						
					}
				} else {
					res = false;
					Log.d(TAG, "bad string "+ans);
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
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
	
	private void registerInit(){
		if ( isOnline ){
			try {
				Log.d(TAG, "sending login");

				
				connection.sendMessage("0 " + login + " ");
				/*  implement receiving confirmation from server */
				
				
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
						
						connection.sendMessage(
								new String(XorEncryption.Encrypt("1".getBytes(), Long.valueOf(init_passw)))
								);
//						connection.sendMessage("1");
						
						Log.d(TAG, "receiving keyString from the server");
						String ans2 = connection.receiveMessage();
						ans2 = new String( XorEncryption.Decrypt(ans2.getBytes(), Long.valueOf(init_passw)));
						
						Log.d(TAG, "received ans "+ans2);
						
						
						Pattern pat2 = Pattern.compile("(1)\\s(.*)"); // \\w
						Matcher mat2 = pat2.matcher(ans2);
						
						if (mat2.matches()) {
							keyString = mat2.group(2);
							createKeyFile(keyString);
							keyString = new String(
									XorEncryption.Decrypt(keyString.getBytes(), Long.valueOf(pin))
									);
							Log.d(TAG, "keyString  "+keyString);
						}
						
					}
				} else {
					res = false;
					Log.d(TAG, "bad string "+ans);
				}
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
	
	private String readFile() {
		String read = null;

		FileInputStream fis;
		try {
			fis = context.openFileInput(FILENAME);

			byte[] inputBuffer = new byte[1000];
			fis.read(inputBuffer);
			fis.close();
			read = new String(inputBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return read;
	}

	public String getBalance() {
		return balance;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPin() {
		return pin;
	}
	
	public String getInit_passw() {
		return init_passw;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	
	
}
