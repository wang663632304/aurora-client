package it.vivido.aurora.client.components;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

import com.pachube.jpachube.Feed;
import com.pachube.jpachube.Pachube;


public class CosmLibrary {

	public class PachubeData {
		public String _key;
		public String _value;

		public PachubeData(String key, String value) {
			this._key = key;
			this._value = value;

		}
	}

	private static String API_KEY = "uKX2w7zzJYTYdzaVymJYPIZ0XoOSAKxFY2RYT2pST1p1WT0g";

	private static CosmLibrary _singleton;
	public static CosmLibrary getInstance(Context context) { if (_singleton == null ) _singleton = new CosmLibrary(context); return _singleton; }

	private Pachube _pachube;

	private Feed _feed;
	private Context _context;

	private LinkedBlockingQueue<PachubeData> _lbq = new LinkedBlockingQueue<CosmLibrary.PachubeData>();

	public CosmLibrary(Context context) {

		this._context = context;
		startUpload();
	}


	public void updateSensor(final String key, final String value)
	{
		_lbq.add(new PachubeData(key, value));

	}

	private void startUpload()
	{
		Thread thu = new Thread(new Runnable() {

			@Override
			public void run() {
				try
				{
					while (true)
					{
						PachubeData data = _lbq.poll();
						if ( data != null)
						{



							if ((Boolean) APreferences.getInstance(_context).getValue("dataupload", Boolean.class))
							{
								Log.d("pachube_upload", String.format("Upload data key:%s data:%s", data._key, data._value));

								HttpClient ht = new DefaultHttpClient();
								HttpPut put = new HttpPut("http://api.pachube.com/v2/feeds/72413");
								put.addHeader("X-ApiKey", API_KEY);
								String TextData = String.format("%s,%s",data._key, data._value);
								StringEntity entity = new StringEntity(TextData, "UTF-8");
								entity.setContentType("text/csv");
								put.setEntity(entity);
								ht.execute(put);

								Log.d("pachube_upload", "Upload success!");

							}


						}
					}
				}
				catch(Exception ex)
				{

				}

			}
		});
		thu.start();


	}


}
