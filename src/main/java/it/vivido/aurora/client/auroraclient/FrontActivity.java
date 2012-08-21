package it.vivido.aurora.client.auroraclient;

import com.androidquery.AQuery;

import android.app.Activity;
import android.os.Bundle;

public class FrontActivity extends Activity{
	
	private AQuery aq;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		aq = new AQuery(this);
		setContentView(R.layout.front_layout);
	}

}
