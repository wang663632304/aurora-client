package it.vivido.aurora.client.auroraclient;

import java.util.Random;

import it.vivido.aurora.client.base.AuroraInfo;
import it.vivido.aurora.client.components.CosmLibrary;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;

public class DebugActivity extends Activity {

	
	private AQuery aq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug_activity);
		aq = new AQuery(this);

		registerReceiver(btr, new IntentFilter(AuroraInfo.DATA_IN));
		
		aq.id(R.id.bntTestPachube).clicked(this, "bntPachube");

	}


	
	private BroadcastReceiver btr = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			  String action = intent.getAction();
			  if (action.equals(AuroraInfo.DATA_IN))
			  {
				  String datain = intent.getStringExtra("datain");
				  aq.find(R.id.edtDebug).getEditText().append(datain);
				  
			  }
		
			  
		}
	}; 
	
	public void bntPachube(View v)
	{
		CosmLibrary.getInstance(this).updateSensor("rpm", Integer.toString(new Random().nextInt(6000)));
	}


};


