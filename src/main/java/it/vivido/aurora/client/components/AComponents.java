package it.vivido.aurora.client.components;

import android.content.Context;

public class AComponents {
	
	private static AComponents _instance = null;
	public static AComponents getInstance(Context context) { if (_instance == null) _instance = new AComponents(context); return _instance;}
	
	public static AComponents getInstance() { return _instance;}
	
	private Context _context;

	public AComponents(Context context) {
		this._context = context;
	}
		
	public ANotificationsManager getANotificationManager() {
		return ANotificationsManager.getInstance(_context);
	}
	
	public ALocationManager getALocationManager(){
		return ALocationManager.getInstance(_context);
	}
}
