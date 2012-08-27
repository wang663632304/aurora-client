package it.vivido.aurora.client.auroraclient;
import it.vivido.aurora.client.base.AuroraInfo;
import it.vivido.aurora.client.components.AComponents;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;


public class MsgActivity extends Activity {
	
	private Activity _activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_activity = this;
		registerReceiver(btr, new IntentFilter(AuroraInfo.DATA_IN));
	}
	
	
	
	private BroadcastReceiver btr = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(AuroraInfo.DATA_IN))
			{
				String datain = intent.getStringExtra("datain");
				if (datain.startsWith("MSG:"))
				{
					if (datain.split("\r\n").length == 1)
					{
						String message = datain.substring(datain.indexOf("MSG:") + "MSG:".length(), datain.indexOf("#END")).replace("\r\n", "");						
						AComponents.getInstance().getANotificationManager().showSimpleNotification(_activity, message, getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.app_version), message);						
					}}
			}
		}
	};

}
