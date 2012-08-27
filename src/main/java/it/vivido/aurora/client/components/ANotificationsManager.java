package it.vivido.aurora.client.components;

import it.vivido.aurora.client.auroraclient.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ANotificationsManager {

	private static ANotificationsManager _instance;
	public static ANotificationsManager getInstance(Context context) { if (_instance == null) _instance = new ANotificationsManager(context); return _instance;} 


	private Context _context;
	private NotificationManager _notificationMan;

	public ANotificationsManager(Context context) {	
		this._context = context;		
		initNotificationManager();
	}

	private void initNotificationManager()
	{
		_notificationMan = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE); 		
	}

	public void showSimpleNotification(Activity activity, String ticker, String title, String text)
	{
		if (_notificationMan != null)
		{
			Notification notif = new Notification(R.drawable.icon, ticker, System.currentTimeMillis());
			notif.flags |= Notification.FLAG_AUTO_CANCEL;
			notif.number += 1;
			Intent intent = new Intent(_context, activity.getClass());
			PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, intent, 0);
			
			notif.setLatestEventInfo(_context, title, text, contentIntent);
			_notificationMan.notify(0, notif);
			
		}
	}	
	
	public void showToast(String text)
	{
		Toast.makeText(_context, text, Toast.LENGTH_SHORT).show();
	}
}
