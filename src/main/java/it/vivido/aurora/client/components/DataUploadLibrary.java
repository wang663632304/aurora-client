package it.vivido.aurora.client.components;

import it.vivido.aurora.client.data.SensorsData;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DataUploadLibrary {

	private static String API_KEY = "uKX2w7zzJYTYdzaVymJYPIZ0XoOSAKxFY2RYT2pST1p1WT0g";

	private static DataUploadLibrary _singleton;
	public static DataUploadLibrary getInstance(Context context) { if (_singleton == null ) _singleton = new DataUploadLibrary(context); return _singleton; }

	private HttpClient _ht;	
	private Context _context;
	private Boolean th_execution = true;
	private Thread th_dataupload;

	private LinkedBlockingQueue<SensorsData> _lbq = new LinkedBlockingQueue<SensorsData>();

	public DataUploadLibrary(Context context) {

		this._context = context;
		_ht = new DefaultHttpClient();		
		startUpload();
	}
	/**
	 * Use this function for send to host Sensors data
	 * @param Sensor key
	 * @param Sensor data
	 */
	public void updateSensor(final String key, final String value)
	{
		_lbq.add(new SensorsData(key, value));

	}

	private void startUpload()
	{
		th_dataupload = new Thread(new Runnable() {

			@Override
			public void run() {
				try
				{
					while (th_execution)
					{	
						SensorsData data = _lbq.poll();
						if ( data != null)
						{
							if (canUpload())
							{
								sendUploadData(data);
							}
						}
					}
				}
				catch(Exception ex)
				{

				}
			}
		});
		th_dataupload.start();
	}

	/**
	 * When activity closing use this function for release upload thread
	 */
	public void Stop()
	{
		Log.d("pachube_upload", "Closing upload thread..");
		th_execution  = false;

		try 
		{
			th_dataupload.join();
		} catch (InterruptedException e) {

		}
	}

	private boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private HttpPut getPut(String feed, String api_key)
	{
		HttpPut put = new HttpPut(String.format("http://api.pachube.com/v2/feeds/%s", feed));
		put.addHeader("X-ApiKey", api_key);

		return put;
	}

	private Boolean canUpload()
	{
		Boolean prefs =  (Boolean) APreferences.getInstance(_context).getValue("dataupload", Boolean.class);
		Boolean internet_conn = isOnline();

		if ((prefs) && (internet_conn))
			return true;

		return false;									
	}

	private void sendUploadData(SensorsData data)
	{
		HttpPut put = getPut("72413", API_KEY);

		try
		{
			Log.d("pachube_upload", String.format("Upload data key:%s data:%s", data.getKey(), data.getValue()));


			String TextData = String.format("%s,%s", data.getKey(), data.getValue());
			StringEntity entity = new StringEntity(TextData, "UTF-8");
			entity.setContentType("text/csv");

			put.setEntity(entity);
			_ht.execute(put);

			Log.d("pachube_upload", "Upload success!");
		}
		catch(Exception ex)
		{
			Log.e("dataupload", "Error during upload dataupload: " + ex.getMessage());
		}
		finally
		{
			put = null;
		}

	}
}
