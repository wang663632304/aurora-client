package it.vivido.aurora.client.base;

import com.androidquery.AQuery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class AuroraActivity extends Activity {
	
	private AQuery aq_;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initAQuery();
	}	
	/**
	 * Init AQuery on current activity
	 */
	private void initAQuery()
	{
		aq_ = new AQuery(this);
	}
	
	protected AQuery getAQuery() 
	{
		return aq_;
	}
	/**
	 * Show debug toast
	 * @param text
	 */
	protected void debug(String text)
	{
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

}
