package it.vivido.aurora.client.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class APreferences {

	private static APreferences _instance;
	public static APreferences getInstance(Context context) {if (_instance == null) _instance = new APreferences(context); return _instance;}


	private Context context;
	private SharedPreferences _prefs;

	public APreferences(Context context) {
		Log.d(this.getClass().getName(), "Creating APreferences");
		this.context = context;
		_prefs = PreferenceManager.getDefaultSharedPreferences(this.context);		
	}

	/**
	 * Set value to shared preferences of application
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Object value)
	{
		Log.d(this.getClass().getName(), "Setting key:" + key + " value: " + value.toString());

		
		Editor edit =  _prefs.edit();

		if (value instanceof String)
			edit.putString(key, (String)value);

		if (value instanceof Boolean)
			edit.putBoolean(key, (Boolean)value);

		if (value instanceof Float)
			edit.putFloat(key, (Float)value);

		if (value instanceof Integer)
			edit.putInt(key, (Integer)value);

		if (value instanceof Long)
			edit.putLong(key, (Long)value);

		edit.commit();
	}

	
	/**
	 * Get value from shared preferences file
	 * @param key
	 * @param classz
	 * @return
	 */
	public Object getValue(String key, Class<?> classz)
	{
		if (classz == String.class)
			return  _prefs.getString(key, "");		

		if (classz == Integer.class)
			return _prefs.getInt(key, -1);

		if (classz == Boolean.class)
			return _prefs.getBoolean(key, false);

		if (classz == Long.class)
			return _prefs.getLong(key, -1);

		if (classz == Float.class)
			return _prefs.getFloat(key, -1);

		return null;		
	}
}
