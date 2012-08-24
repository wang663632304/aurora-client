package it.vivido.aurora.client.auroraclient;

import it.vivido.aurora.client.base.AuroraInfo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.androidquery.AQuery;

public class DebugActivity extends Activity {

	
	private AQuery aq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug_activity);
		aq = new AQuery(this);

		registerReceiver(btr, new IntentFilter(AuroraInfo.DATA_IN));

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


};


