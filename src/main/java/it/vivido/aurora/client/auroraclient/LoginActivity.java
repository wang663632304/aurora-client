package it.vivido.aurora.client.auroraclient;

import it.vivido.aurora.client.base.AuroraActivity;
import it.vivido.aurora.client.base.AuroraInfo;
import it.vivido.aurora.client.components.AComponents;
import it.vivido.aurora.client.components.APreferences;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class LoginActivity extends AuroraActivity {

	private static String TAG = "aurora_loginactivity";

	private Activity _activity;


	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_activity = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(TAG, "onCreate");
		
		
	
		
		initAComponents();
		checkBluetooth();
	

		setContentView(R.layout.main);


		getAQuery().id(R.id.btnLogin).clicked(this, "login");

		checkConfig();


	}

	private void initAComponents()
	{
		AComponents.getInstance(getApplicationContext());
		AComponents.getInstance().getALocationManager();
	}
	/**
	 * Check if bluetooth is available, else close application :(
	 */
	private void initBluetooth()
	{	
		// If the adapter is null, then Bluetooth is not supported
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			Toast.makeText(this, R.string.msgBluetoothNotAvailable, Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	/**
	 *  Check if bluetooth is enabled, else try to enable connection
	 */
	private void checkBluetooth()
	{
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, 2);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode)
		{
		case AuroraInfo.REQUEST_ENABLE_BT:
			if (resultCode != Activity.RESULT_OK)
			{				  
				Toast.makeText(this, R.string.msgBluetoothNotAvailable, Toast.LENGTH_LONG).show();
				finish();
			}
			else
			{
				Toast.makeText(this, R.string.msgBluetoothEnabled, Toast.LENGTH_LONG).show();
			}			
		
		case AuroraInfo.EXIT_APPLICATION:
			finish();
			System.exit(0);
			
		}
	}

	public void login(View v)
	{
		//Check if username and password edits are empty
		if ((getAQuery().id(R.id.edtEmail).getEditText().getText().toString().isEmpty()) || (getAQuery().id(R.id.edtPassword).getEditText().getText().toString().isEmpty()))
		{		
			Toast.makeText(getApplication(), R.string.msgEmptyUserPass, Toast.LENGTH_LONG).show();			
			getAQuery().id(R.id.edtEmail).getEditText().requestFocus();
		}
		else
		{
			APreferences.getInstance(getApplicationContext()).setValue("email", getAQuery().id(R.id.edtEmail).getEditText().getText().toString());
			APreferences.getInstance(getApplicationContext()).setValue("password", getAQuery().id(R.id.edtPassword).getEditText().getText().toString());

			progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getText(R.string.msgLogin), true);			


			makeAjaxLogin();

		}


	}

	private void checkConfig()
	{
		if (APreferences.getInstance(getApplicationContext()).getValue("email", String.class) != "")
		{
			String email = (String) APreferences.getInstance(getApplicationContext()).getValue("email", String.class);
			String password = (String) APreferences.getInstance(getApplicationContext()).getValue("password", String.class);
			getAQuery().find(R.id.edtEmail).getTextView().setText(email);
			getAQuery().find(R.id.edtPassword).getTextView().setText(password);

			login(getCurrentFocus());
		}
	}
	


	private void makeAjaxLogin()
	{
		String login_ = "http://127.0.0.1";
		getAQuery().ajax(login_, JSONObject.class, new AjaxCallback<JSONObject>()
				{

			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {

				try
				{
					Thread.sleep(2000);
					progressDialog.hide();
					Intent in = new Intent(getBaseContext(), FrontActivity.class);
					startActivityForResult(in, AuroraInfo.EXIT_APPLICATION);
				}
				catch(Exception ex)
				{

				}
			}

				});

	}

}

