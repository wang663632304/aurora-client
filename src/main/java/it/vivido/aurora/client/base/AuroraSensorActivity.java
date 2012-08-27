package it.vivido.aurora.client.base;

import java.util.Timer;
import java.util.TimerTask;

import it.vivido.aurora.client.auroraclient.R;
import it.vivido.aurora.client.components.APreferences;
import it.vivido.aurora.client.components.DataUploadLibrary;
import it.vivido.aurora.client.components.Thermometer;

import com.androidquery.AQuery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class AuroraSensorActivity extends Activity{


	private String sensor_key = "";
	private String sensor_label = "";
	private String sensor_unit = "";
	private String sensor_pachube_key = "";

	private int sensor_min = 0;
	private int sensor_max = 0;


	private String sensor_value = "";
	Double  sensor_perc;

	private Thermometer termo;
	private AQuery aq_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAQuery();

		sensor_key =  getIntent().getExtras().getString("sensor_key");	
		sensor_label = getIntent().getExtras().getString("sensor_label");
		sensor_unit = getIntent().getExtras().getString("sensor_unit");
		sensor_min = getIntent().getExtras().getInt("sensor_min");
		sensor_max = getIntent().getExtras().getInt("sensor_max");
		sensor_pachube_key = getIntent().getExtras().getString("sensor_pachube_key");


		setContentView(R.layout.sensor_layout);

		//termo = (Thermometer) findViewById(R.id.sens_therm);
		//termo.setMinMax(sensor_min, sensor_max);
		//termo.Progress(sensor_min);

		aq_.find(R.id.lblSensorLabel).getTextView().setText(sensor_label);
		aq_.find(R.id.lblSensorData).getTextView().setText("N/A");



		registerReceiver(btr, new IntentFilter(AuroraInfo.DATA_IN));

		initPachubeUpdate();

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
				
				String a_datain[] = datain.split("\r\n");
				
				for (int i=0;i<a_datain.length;i++)
				{
					if (a_datain[i].startsWith(sensor_key))
					{
						String data = a_datain[i];
						//if (datain.split("\r\n").length == 1)
						//{
							sensor_value = datain.substring(data.indexOf(sensor_key) + sensor_key.length(), data.indexOf("#END")).replace("\r\n", "");
							executeSensorUpdate();
						//}
						
					}
				}				
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
				sensor_perc =  (double)((Integer.parseInt(sensor_value) * 100.0f) / sensor_max);
				aq_.find(R.id.pbSensor).getProgressBar().setProgress(sensor_perc.intValue());
				//termo.Progress(Integer.parseInt(sensor_value));
			}
			catch(Exception ex)
			{

			}
		}
	};

	private Runnable PachubeUpdate = new Runnable() {

		@Override
		public void run() {
			if (!sensor_value.isEmpty())
			{   Log.i(String.format("sensor_%s", sensor_key), "Upload to cosm.com");
				DataUploadLibrary.getInstance(getApplicationContext()).updateSensor(sensor_pachube_key, sensor_value);
			}
		}
	};

	private void initPachubeUpdate()
	{
		int refresh_rate = 0;
		
		String tmp = (String) APreferences.getInstance(this).getValue("dataupload_interval", String.class);
		if (tmp.isEmpty())
		{
			APreferences.getInstance(this).setValue("dataupload_interval", 30000);
		}
		
		refresh_rate =  Integer.parseInt(tmp);
	
		 Log.i(String.format("sensor_%s", sensor_key), "Starting upload thread every " + refresh_rate + " milliseconds");
		if (!sensor_pachube_key.isEmpty())
		{
			Timer tmrPachube = new Timer();
			tmrPachube.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					runOnUiThread(PachubeUpdate);					
				}
			}, 0, refresh_rate);


		}
	}

}
