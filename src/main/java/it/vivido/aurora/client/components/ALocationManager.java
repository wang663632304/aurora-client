package it.vivido.aurora.client.components;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class ALocationManager  implements LocationListener {

	private static ALocationManager _instance = null;
	public static ALocationManager getInstance(Context context) { if (_instance == null) { _instance = new ALocationManager(context);} return _instance;}


	private Context _context;
	private LocationManager _locationmanager;

	private Location _location;
	public Location getCurrentLocation() { return _location;}

	public ALocationManager(Context context) {
		this._context = context;
		initLocationManager();
	}

	private void initLocationManager()
	{
		_locationmanager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
		_locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, this);

		Criteria criteria = new Criteria();
		String bestProvider = _locationmanager.getBestProvider(criteria, false);
		
		_location = _locationmanager.getLastKnownLocation(bestProvider);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d("alocationmanager", "New location");
		AComponents.getInstance().getANotificationManager().showToast(String.format("New location!\n LAT: %s LONG: %s", location.getLatitude(), location.getLongitude()));
		_location = location;		
	}

	@Override
	public void onProviderDisabled(String provider) {
		String _provider = Settings.Secure.getString(_context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if(!_provider.contains("gps")){ //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3")); 
			_context.sendBroadcast(poke);
		}

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {


	}
}
