package it.vivido.aurora.client.services;
import it.vivido.aurora.client.auroraclient.R;
import it.vivido.aurora.client.base.AuroraInfo;
import it.vivido.aurora.client.components.AComponents;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MsgService extends  BroadcastReceiver {

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
					String msg = datain.substring(datain.indexOf("MSG:") + "MSG:".length(), datain.indexOf("#END")).replace("\r\n", "");
					AComponents.getInstance().getANotificationManager().showSimpleNotification((Activity) context, msg, context.getResources().getString(R.string.app_name) + context.getResources().getString(R.string.app_version), msg);
				}}
		}
	}

}





