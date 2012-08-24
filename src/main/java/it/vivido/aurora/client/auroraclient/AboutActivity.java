package it.vivido.aurora.client.auroraclient;

import it.vivido.aurora.client.base.AuroraActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class AboutActivity extends AuroraActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);

		getAQuery().find(R.id.ivLogo).getImageView().setOnLongClickListener(IvLogoOLC);
		getAQuery().id(R.id.bntBack).clicked(this, "OnbntBackClick");
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
