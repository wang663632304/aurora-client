package it.vivido.aurora.client.auroraclient;

import org.json.JSONObject;

import it.vivido.aurora.client.components.APreferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class LoginActivity extends Activity {

	private static String TAG = "aurora_loginactivity";


	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	private ProgressDialog progressDialog;

	private AQuery aq;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(TAG, "onCreate");

		initBluetooth();
		checkBluetooth();

		setContentView(R.layout.main);
		aq = new AQuery(this);

		aq.id(R.id.btnLogin).clicked(this, "login");

		checkConfig();


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
		case REQUEST_ENABLE_BT:
			if (resultCode != Activity.RESULT_OK)
			{				  
				Toast.makeText(this, R.string.msgBluetoothNotAvailable, Toast.LENGTH_LONG).show();
				finish();
			}
			else
			{
				Toast.makeText(this, R.string.msgBluetoothEnabled, Toast.LENGTH_LONG).show();
			}			
		}
	}

	public void login(View v)
	{
		//Check if username and password edits are empty
		if ((aq.id(R.id.edtEmail).getEditText().getText().toString().isEmpty()) || (aq.id(R.id.edtPassword).getEditText().getText().toString().isEmpty()))
		{		
			Toast.makeText(getApplication(), R.string.msgEmptyUserPass, Toast.LENGTH_LONG).show();			
			aq.id(R.id.edtEmail).getEditText().requestFocus();
		}
		else
		{
			APreferences.getInstance(getApplicationContext()).setValue("email", aq.id(R.id.edtEmail).getEditText().getText().toString());
			APreferences.getInstance(getApplicationContext()).setValue("password", aq.id(R.id.edtPassword).getEditText().getText().toString());

			progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getText(R.string.msgLogin), true);			


			makeAjaxLogin();

		}


	}

	private void checkConfig()
	{
		if (APreferences.getInstance(getApplicationContext()).getValue("email", String.class) != "")
		{
			Toast.makeText(getApplication(), R.string.msgLogin, Toast.LENGTH_LONG).show();
		}
	}

	private void makeAjaxLogin()
	{
		String login_ = "http://127.0.0.1";
		aq.ajax(login_, JSONObject.class, new AjaxCallback<JSONObject>()
				{

			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {

				try
				{
					Thread.sleep(2000);
					progressDialog.hide();
					//Toast.makeText(getApplication(), R.string.msgLogin, Toast.LENGTH_LONG).show();
					Intent in = new Intent(getBaseContext(), FrontActivity.class);
					startActivity(in);
				}
				catch(Exception ex)
				{

				}
			}

				});

	}

}

