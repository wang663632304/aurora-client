package it.vivido.aurora.client.auroraclient;

import android.os.Bundle;

import com.google.android.maps.MapActivity;


public class MapPosActivity extends MapActivity  {

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.map_layout);
		super.onCreate(arg0);
	}

}
