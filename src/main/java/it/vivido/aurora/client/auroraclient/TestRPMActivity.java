package it.vivido.aurora.client.auroraclient;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.androidquery.AQuery;

import android.app.Activity;
import android.os.Bundle;

public class TestRPMActivity extends Activity {

	AQuery aq;
	Timer tmr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testrpm_layout);
aq = new AQuery(this);
		tmr = new Timer();

		tmr.schedule(new TimerTask() {

			@Override
			public void run() {
				executeRandom();

			}
		}, 0, 200);
	}

	private void executeRandom()
	{
		this.runOnUiThread(Carousel_Tick);
	}
	
	private Runnable Carousel_Tick = new Runnable() {
		public void run() {
			
			Random ran = new Random();
			;
			
			aq.find(R.id.lblRPM).getTextView().setText(String.format("%srpm", ran.nextInt(6000)));



		}
	};





}
