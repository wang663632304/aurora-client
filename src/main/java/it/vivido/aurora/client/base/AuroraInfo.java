package it.vivido.aurora.client.base;

import it.vivido.aurora.client.auroraclient.TestRPMActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AuroraInfo {

	public static final String DATA_IN = "it.vivido.aurora.client.auroraclient.DATA_IN";

	// Intent request codes
	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_DATA_IN = 6;
	
	public static final String TOAST = "toast";

	/**
	 * Broadcast Aurora sensor
	 * @param sender
	 * @param msg
	 */
	public static void SendOBDBroadcast(Activity sender, String msg){
		Intent i = new Intent();
		i.setAction(DATA_IN);
		i.putExtra("datain", msg);
		sender.sendBroadcast(i);
	}
	
	/**
	 * Create TabWidget passing sensor to view
	 * @param context
	 * @param sensor
	 * @return
	 */
	public static Intent createSensorActivity(Context context, String sensor_key, String sensor_label, String sensor_unit, int sensor_min, int sensor_max, String sensor_pachube_key)
	{
		Intent i = new Intent(context, AuroraSensorActivity.class);
		Bundle b = new Bundle();
		
		b.putString("sensor_key", sensor_key);
		b.putString("sensor_unit", sensor_unit);
		b.putString("sensor_label", sensor_label);		
		b.putInt("sensor_min", sensor_min);
		b.putInt("sensor_max", sensor_max);
		b.putString("sensor_pachube_key", sensor_pachube_key);
		
		i.putExtras(b);
		
		return i;
		
	}
}
