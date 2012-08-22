package it.vivido.aurora.client.auroraclient;

import it.vivido.aurora.client.components.BluetoothCommandService;

import com.androidquery.AQuery;

import android.app.Activity;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;

public class FrontActivity extends TabActivity{

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
  
    
	private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		aq = new AQuery(this);
		mCommandService = new BluetoothCommandService(this, mHandler);
		setContentView(R.layout.front_layout);
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec = tabHost.newTabSpec("Test").setIndicator("Test", getResources().getDrawable(R.drawable.icon)).setContent(new Intent(this, TestGraphLayout.class));
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("Test").setIndicator("Test", getResources().getDrawable(R.drawable.icon)).setContent(new Intent(this, TestGraphLayout.class));
		tabHost.addTab(spec);
		
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
                
                Toast.makeText(this, address, Toast.LENGTH_LONG).show();
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mCommandService.connect(device);
            }
            break;
	    }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	// The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothCommandService.STATE_CONNECTED:
                	  Toast.makeText(getApplicationContext(), "Connected to "
                              + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothCommandService.STATE_CONNECTING:
                	  Toast.makeText(getApplicationContext(), "Connecting to "
                              + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothCommandService.STATE_LISTEN:
                case BluetoothCommandService.STATE_NONE:
                	  Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                    break;
                }
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
                
            case MESSAGE_READ:
            	msg.getData();
            	break;
            	
            case MESSAGE_DATA_IN:
            	String data_in = msg.getData().getString("datain");
            	 Toast.makeText(getApplicationContext(),data_in,
                         Toast.LENGTH_SHORT).show();
            	 break;
            }
            
            
        }
    };


}
