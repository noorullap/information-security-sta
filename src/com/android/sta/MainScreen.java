package com.android.sta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainScreen extends Activity implements OnClickListener{
	private static final String TAG = "STAStart";
	private String account;
	private EditText mAccountText, mBalanceText, mSourceText, mDestinationText, mAmountText;
	private MainManager mMainManager;
	
	public static final String EXT_ACCOUNT = "account";
	public static final String EXT_MANAGER = "manager";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main_win);
        setTitle(R.string.app_name);
        Bundle extras = getIntent().getExtras();
        account = extras.getString(EXT_ACCOUNT);
        mMainManager = MainManager.getInstance();
        
        mAccountText = (EditText) findViewById(R.id.account);
        mBalanceText = (EditText) findViewById(R.id.balance);
        mSourceText = (EditText) findViewById(R.id.source);
        mDestinationText = (EditText) findViewById(R.id.destination);
        mAmountText = (EditText) findViewById(R.id.amount);
		
        mAccountText.setText(account);
        mSourceText.setText(account);

		/** Button "Refresh" */
		Button getBalanceButton = (Button) findViewById(R.id.getBalance);
		getBalanceButton.setOnClickListener( this);

		/** Button "Transfer" */
		Button tranferButton = (Button) findViewById(R.id.transfer);
		tranferButton.setOnClickListener( this);

		/** Button "Exit" */
		Button exitButton = (Button) findViewById(R.id.exit);
		exitButton.setOnClickListener( this);
	}

	/** Handling pressing buttons */
	public void onClick( View view){
		switch ( view.getId() ){
		
		case R.id.getBalance:
			Log.d(TAG, "onClick: get Balance");
			String balance = mMainManager.getBalanceFromServer("1");
			mBalanceText.setText(balance);
			break;

		case R.id.transfer:
			Log.d(TAG, "onClick: transfer");
			String source = mSourceText.getText().toString();
			String destination = mDestinationText.getText().toString();
			String amount = mAmountText.getText().toString();
			if ( mMainManager.transferMoney(source, destination, amount) )
			{
				Log.d(TAG, "onClick: tranfer done");
				Toast.makeText(this, "Tranfer done!", Toast.LENGTH_LONG).show();
				
			} else{
				Log.d(TAG, "onClick: transfer error");
				Toast.makeText(this, "Tranfer error!", Toast.LENGTH_LONG).show();
			}
		
		case R.id.exit:
			Log.d(TAG,"onClick: exit");
			/** TODO: add additional code before log out */
			finish();
			break;
		
		}
	}

}
