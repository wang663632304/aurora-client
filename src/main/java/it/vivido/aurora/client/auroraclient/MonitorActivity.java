package it.vivido.aurora.client.auroraclient;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;


public class MonitorActivity extends Activity {


	private ArrayList<String> devices = new ArrayList<String>();
	private ArrayAdapter<String> dataAdapter;
	private BroadcastReceiver bluetoothBReceiver;


	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	 // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";


	private AQuery aq;
	private BluetoothAdapter mBluetoothAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		aq = new AQuery(this);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


		setContentView(R.layout.monitor_layout);
	}


	@Override
	protected void onStart() {

		dataAdapter = new ArrayAdapter<String>(this, R.layout.bt_item, R.id.itemName);		
		aq.id(R.id.lvBTDevices).getListView().setAdapter(dataAdapter);
		aq.id(R.id.lvBTDevices).getListView().setOnItemClickListener(mDeviceClickListener);
		discoverDevices();
		super.onStart();
	}


	private void discoverDevices()
	{

		bluetoothBReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

					// If it's already paired, skip it, because it's been listed already
				//	if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
						if (!device.getName().isEmpty())
							dataAdapter.add(device.getName() + "\n" + device.getAddress());
						
						dataAdapter.notifyDataSetChanged();
						Log.d("MonitorActivity", "Bluetooth device added: " + device.getAddress().toString());	
					}//}

			}};

			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(bluetoothBReceiver, filter); 


			if (mBluetoothAdapter.isEnabled())
			{
				mBluetoothAdapter.startDiscovery();						
			}
	}
	

	// The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            LinearLayout layout = (LinearLayout)v;
            TextView tv = (TextView) v.findViewById(R.id.itemName);
            String info = tv.getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
            //Toast.makeText(v.getContext(), address, Toast.LENGTH_LONG).show();
        }
    };


	@Override
	protected void onDestroy() {
		unregisterReceiver(bluetoothBReceiver);
		if (mBluetoothAdapter != null)
		{
			mBluetoothAdapter.cancelDiscovery();
		}
		super.onDestroy();
	}
	@Override
	protected void onStop() {

		super.onStop();
	}
}
