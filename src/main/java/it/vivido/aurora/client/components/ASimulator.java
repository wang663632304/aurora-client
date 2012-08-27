package it.vivido.aurora.client.components;

import it.vivido.aurora.client.base.AuroraInfo;

import java.util.Random;

import android.app.Activity;
import android.content.Context;

public class ASimulator {
	private static ASimulator _instance = null;
	public static ASimulator getInstance(Context context) { if (_instance == null) { _instance = new ASimulator(context);} return _instance;}



	private Thread  sim_thread;
	private Context _context;

	private Boolean th_loop_enabled = true;

	public ASimulator(Context context) {
		this._context = context;
	}

	public void startSimulator()
	{
		sim_thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try
				{
					while (th_loop_enabled)
					{
						String toupload = "";
						toupload +=  "RPM:" + new Random().nextInt(7000) + "#END\r\n";
						toupload +=  "COT:" + new Random().nextInt(100) + "#END\r\n";
						toupload +=  "THR:" + new Random().nextInt(100) + "#END\r\n";
						toupload +=  "SPD:" + new Random().nextInt(280) + "#END\r\n";						

						Thread.sleep(100);
						AuroraInfo.SendOBDBroadcast((Activity) _context, toupload);					
					}
				}
				catch(Exception ex)
				{

				}

			}
		});		

		sim_thread.start();
	}

	public void stopSimulator()
	{
		th_loop_enabled = false;
		try
		{
			sim_thread.join();
		}
		catch(Exception ex)
		{

		}
	}



}
