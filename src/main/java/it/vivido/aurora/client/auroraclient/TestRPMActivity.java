package it.vivido.aurora.client.auroraclient;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.androidquery.AQuery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class TestRPMActivity extends Activity {

	private static String rpm = "0";
	AQuery aq;
	Timer tmr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testrpm_layout);
		aq = new AQuery(this);
		registerReceiver(btr, new IntentFilter(DebugActivity.DATA_IN));
	}
		
	private BroadcastReceiver btr = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(DebugActivity.DATA_IN))
			{
				String datain = intent.getStringExtra("datain");
				//String[] str = datain.split("\r\n");
				if (datain.startsWith("RPM"))
				{
					if (datain.split("\r\n").length == 1)
					{
					rpm =datain.replace("RPM:", "").replace("\r\n", "");
					executeRandom();
				}}
			}


		}
	};

		private void executeRandom()
		{
			this.runOnUiThread(Carousel_Tick);
		}

		private Runnable Carousel_Tick = new Runnable() {
			public void run() {

			

				aq.find(R.id.lblRPM).getTextView().setText(String.format("%srpm", rpm));



			}
		};





	}
