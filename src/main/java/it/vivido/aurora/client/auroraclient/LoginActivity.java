package it.vivido.aurora.client.auroraclient;

import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static String TAG = "aurora_loginactivity";

	private AQuery aq;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.main);
		aq = new AQuery(this);

		
	
		aq.id(R.id.btnLogin).text("Login?").clicked(this, "login");

		
		
	}

	public void login(View v)
	{
		Intent in = new Intent(this, MonitorActivity.class);
		startActivity(in);

	}

}

