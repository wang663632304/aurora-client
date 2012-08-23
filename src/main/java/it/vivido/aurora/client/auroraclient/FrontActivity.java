package it.vivido.aurora.client.auroraclient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import it.vivido.aurora.client.components.BluetoothCommandService;

import com.androidquery.AQuery;

import android.app.Activity;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
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
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class FrontActivity extends TabActivity implements OnGesturePerformedListener{

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_DATA_IN = 6;


	// Key names received from the BluetoothCommandService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

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
		
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				current_index = getTabHost().getCurrentTab();
			}
		});
		tabHost = getTabHost();
		TabHost.TabSpec spec = tabHost.newTabSpec("Test").setIndicator("Test graph").setContent(new Intent(this, TestGraphLayout.class));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("Test2").setIndicator("Test graph 2").setContent(new Intent(this, TestRPMActivity.class));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("Debug").setIndicator("Debug").setContent(new Intent(this, DebugActivity.class));
		tabHost.addTab(spec);


		Carouseltmr = new Timer();
		
		Carouseltmr.cancel();
		
		StartCarousel();
	}

	private void StartCarousel()
	{
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
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;

		case R.id.itexit:
			System.exit(0);
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
		case REQUEST_CONNECT_DEVICE:
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
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothCommandService.STATE_CONNECTED:
					aq.find(R.id.lblStatus).getTextView().setText("Connected to " + mConnectedDeviceName);
					break;
				case BluetoothCommandService.STATE_CONNECTING:
					aq.find(R.id.lblStatus).getTextView().setText("Connecting..");

					break;
				case BluetoothCommandService.STATE_LISTEN:
				case BluetoothCommandService.STATE_NONE:
					aq.find(R.id.lblStatus).getTextView().setText("Not connected");
					break;
				}
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				aq.find(R.id.lblStatus).getTextView().setText("Connected to " + mConnectedDeviceName);
				break;



			case MESSAGE_DATA_IN:
				String data_in = msg.getData().getString("datain");
				//Toast.makeText(getApplicationContext(),data_in,
				//        Toast.LENGTH_SHORT).show();
				SendBroadcast(data_in);
				break;
			}


		}
	};

	private void SendBroadcast(String Msg){
		Intent i = new Intent();
		i.setAction(DebugActivity.DATA_IN);
		i.putExtra("datain", Msg);
		this.sendBroadcast(i);
	}

	public void NextTab()
	{
		int count = tabHost.getTabWidget().getTabCount();

		if (current_index == count)
			current_index = 0;

		current_index++;
		tabHost.setCurrentTab(current_index);

	}
	
	public void PrevTab()
	{
		int count = tabHost.getTabWidget().getTabCount();

		if (current_index == 0)
			current_index = count;

		current_index--;
		tabHost.setCurrentTab(current_index);

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




