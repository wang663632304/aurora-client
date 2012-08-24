package it.vivido.aurora.client.base;

import it.vivido.aurora.client.auroraclient.R;
import it.vivido.aurora.client.components.Thermometer;

import com.androidquery.AQuery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class AuroraSensorActivity extends Activity{


	private String sensor_key = "";
	private String sensor_label = "";
	private String sensor_unit = "";
	
	private int sensor_min = 0;
	private int sensor_max = 0;
	

	private String sensor_value = "";

	private Thermometer termo;
	private AQuery aq_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAQuery();

		sensor_key =  getIntent().getExtras().getString("sensor_key");	
		sensor_label = getIntent().getExtras().getString("sensor_label");
		sensor_unit = getIntent().getExtras().getString("sensor_unit");
		sensor_max = getIntent().getExtras().getInt("sensor_max");
		sensor_max = getIntent().getExtras().getInt("sensor_max");

	
		
		setContentView(R.layout.sensor_layout);

		//termo = (Thermometer) findViewById(R.id.sens_therm);
		//termo.setMinMax(sensor_min, sensor_max);
		//termo.Progress(sensor_min);
		
		aq_.find(R.id.lblSensorLabel).getTextView().setText(sensor_label);
		aq_.find(R.id.lblSensorData).getTextView().setText("N/A");

		registerReceiver(btr, new IntentFilter(AuroraInfo.DATA_IN));

	}

	private void initAQuery()
	{
		aq_ = new AQuery(this);
	}

	private BroadcastReceiver btr = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(AuroraInfo.DATA_IN))
			{
				String datain = intent.getStringExtra("datain");
				if (datain.startsWith(sensor_key))
				{
					if (datain.split("\r\n").length == 1)
					{
						sensor_value =datain.replace(sensor_key, "").replace("\r\n", "");
						executeSensorUpdate();
					}}
			}
		}
	};

	private void executeSensorUpdate()
	{
		this.runOnUiThread(SensorUpdate);
	}

	private Runnable SensorUpdate = new Runnable() {
		public void run() {

			aq_.find(R.id.lblSensorData).getTextView().setText(String.format("%s%s", sensor_value, sensor_unit));
			
			try
			{
				//termo.Progress(Integer.parseInt(sensor_value));
			}
			catch(Exception ex)
			{
				
			}
		}
	};

}
