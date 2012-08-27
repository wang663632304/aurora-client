package it.vivido.aurora.client.auroraclient;

import it.vivido.aurora.client.base.AuroraInfo;
import it.vivido.aurora.client.components.ASimulator;
import it.vivido.aurora.client.components.BluetoothCommandService;
import it.vivido.aurora.client.services.MsgService;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.androidquery.AQuery;

public class FrontActivity extends TabActivity implements OnGesturePerformedListener{


	// Key names received from the BluetoothCommandService Handler
	public static final String DEVICE_NAME = "device_name";

	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothCommandService mCommandService = null; 
	private String mConnectedDeviceName = null;

	private Timer Carouseltmr;
	private TabHost tabHost;

	private GestureLibrary gestureLib;

	private int current_index = 0;
	private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		aq = new AQuery(this);
		mCommandService = new BluetoothCommandService(this, mHandler);
		//setContentView(R.layout.front_layout);

		initGestures();

		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				current_index = getTabHost().getCurrentTab();
			}
		});

		tabHost.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Carouseltmr.cancel();
				Carouseltmr.purge();


				return false;
			}
		});

		tabHost.addTab(tabHost.newTabSpec("RPM").setIndicator("RPM",getResources().getDrawable(R.drawable.ic_sensor_rpm) ).setContent(AuroraInfo.createSensorActivity(this, "RPM:", "RPM", " rpm/minute", 0,7000, "rpm")));
		tabHost.addTab(tabHost.newTabSpec("Coolant").setIndicator("Coolant temp", getResources().getDrawable(R.drawable.ic_sensor_temp)).setContent(AuroraInfo.createSensorActivity(this, "COT:", "Coolant Temperature", " *", 0, 100, "coolant_temp")));
		tabHost.addTab(tabHost.newTabSpec("Speed").setIndicator("Speed", getResources().getDrawable(R.drawable.ic_sensor_speed)).setContent(AuroraInfo.createSensorActivity(this, "SPD:", "Car speed", " km/h", 0, 280, "speed")));
		tabHost.addTab(tabHost.newTabSpec("Throttle").setIndicator("Throttle", getResources().getDrawable(R.drawable.ic_sensor_speed)).setContent(AuroraInfo.createSensorActivity(this, "THR:", "Throttle", " %", 0, 100, "")));
		tabHost.addTab(tabHost.newTabSpec("Location").setIndicator("Location", getResources().getDrawable(R.drawable.ic_sensor_speed)).setContent(new Intent(this, MapPosActivity.class)));


		if (AuroraInfo.DEBUG_MODE)
		{
			tabHost.addTab(tabHost.newTabSpec("Debug").setIndicator("Debug", getResources().getDrawable(R.drawable.ic_sensor_debug)).setContent(new Intent(this, DebugActivity.class)));
			tabHost.addTab(tabHost.newTabSpec("Debug Cmd").setIndicator("Debug Cmd", getResources().getDrawable(R.drawable.ic_sensor_debug)).setContent(new Intent(this, DebugModeActivity.class)));

		}

		registerReceiver(new MsgService(), new IntentFilter(AuroraInfo.DATA_IN));		


		StartCarousel();
	}

	private void StartCarousel()
	{
		Carouseltmr = new Timer();
		Carouseltmr.schedule(new TimerTask() {

			@Override
			public void run() {
				Carousel_start();

			}
		}, 0, 2000);	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.m_front, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.itscan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, MonitorActivity.class);
			startActivityForResult(serverIntent, AuroraInfo.REQUEST_CONNECT_DEVICE);
			return true;

		case R.id.itexit:
			setResult(AuroraInfo.EXIT_APPLICATION);
			finish();
			return true;
		case R.id.itsettings:
			startActivity(new Intent(this, PrefsActivity.class));
			return true;
		case R.id.itabout:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		}
		return false;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case AuroraInfo.REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras()
						.getString(MonitorActivity.EXTRA_DEVICE_ADDRESS);


				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				// Attempt to connect to the device
				mCommandService.connect(device);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Runnable Carousel_Tick = new Runnable() {
		public void run() {

			int count = tabHost.getTabWidget().getTabCount();

			if (current_index == count)
				current_index = 0;

			tabHost.setCurrentTab(current_index);
			current_index++;

			//tabHost.setup();


		}
	};

	private void Carousel_start()
	{
		runOnUiThread(Carousel_Tick);
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AuroraInfo.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothCommandService.STATE_CONNECTED:
					aq.find(R.id.lblStatus).getTextView().setText(getResources().getString(R.string.btMsgConnected) + " " + mConnectedDeviceName);
					break;
				case BluetoothCommandService.STATE_CONNECTING:
					aq.find(R.id.lblStatus).getTextView().setText(R.string.btMsgConnecting);

					break;
				case BluetoothCommandService.STATE_LISTEN:
				case BluetoothCommandService.STATE_NONE:
					aq.find(R.id.lblStatus).getTextView().setText(R.string.btMsgNotConnected);
					break;
				}
				break;
			case AuroraInfo.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				aq.find(R.id.lblStatus).getTextView().setText(getResources().getString(R.string.btMsgConnected) + " " + mConnectedDeviceName);
				break;

			case AuroraInfo.MESSAGE_DATA_IN:
				AuroraInfo.SendOBDBroadcast(getCurrentActivity(), msg.getData().getString("datain"));

			}


		}
	};


	public void NextTab()
	{
		int count = tabHost.getTabWidget().getTabCount();

		if (current_index == count)
			current_index = 0;

		current_index++;
		getTabHost().setCurrentTab(current_index);
		StartCarousel();

	}

	public void PrevTab()
	{
		int count = tabHost.getTabWidget().getTabCount();

		if (current_index == 0)
			current_index = count;

		current_index--;
		getTabHost().setCurrentTab(current_index);
		StartCarousel();


	}

	private void initGestures()
	{
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.front_layout, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			finish();
		}
		setContentView(gestureOverlayView);
	}
	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 1.0) {

				if (prediction.name.equals("right"))
					NextTab();
				if (prediction.name.equals("left"))
					PrevTab();
			}
		}
	}
}




