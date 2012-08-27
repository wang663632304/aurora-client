package it.vivido.aurora.client.auroraclient;

import it.vivido.aurora.client.base.AuroraInfo;
import it.vivido.aurora.client.components.ASimulator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;

public class DebugModeActivity extends Activity {
	
	private AQuery _aq;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debugmode_activity);
		
		_aq = new AQuery(this);
		
		_aq.id(R.id.bntDebugTestMsg).clicked(this, "onBntDebugmodeTestMsg");
		_aq.id(R.id.bntDebugEnableSim).clicked(this, "onBntDebugmodeEnableSimulator");
		_aq.id(R.id.bntDebugDisableSim).clicked(this, "onBntDebugmodeDisableSimulator");		
				
	}
	
	public void onBntDebugmodeEnableSimulator(View v)
	{
		ASimulator.getInstance(this).startSimulator();
	}
	
	public void onBntDebugmodeDisableSimulator(View v)
	{
		ASimulator.getInstance(this).stopSimulator();
	}
	public void onBntDebugmodeTestMsg(View v)
	{
		AuroraInfo.SendOBDBroadcast(this, "MSG:This is test message!!#END\r\n");
	}

}
