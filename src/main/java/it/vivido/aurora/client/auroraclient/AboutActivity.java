package it.vivido.aurora.client.auroraclient;

import com.androidquery.AQuery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class AboutActivity extends Activity {

	private AQuery aq = new AQuery(this);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);

		aq.find(R.id.ivLogo).getImageView().setOnLongClickListener(IvLogoOLC);
		aq.id(R.id.bntBack).clicked(this, "OnbntBackClick");
	}

	private OnLongClickListener IvLogoOLC = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			Toast.makeText(getApplicationContext(), R.string.msgDedication , Toast.LENGTH_LONG).show();
			return true;
		}
	};
	
	public void OnbntBackClick(View view)
	{
	   finish();	
	}

}
