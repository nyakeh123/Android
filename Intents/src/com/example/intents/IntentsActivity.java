package com.example.intents;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class IntentsActivity extends Activity {
	int request_Code = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	public void onClickWebBrowser(View view) {
		Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com"));
		startActivity(Intent.createChooser(i, "Open URL using..."));
	}
	
	public void onClickMakeCalls(View view) {
		Intent i = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:+651234567"));
		startActivity(i);
		
		// The following would simply display the dialer -- Intent i = new Intent(android.content.Intent.ACTION_DIAL);
	}

	public void onClickShowMap(View view) {
		Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:37.827500,-122.481670"));
		startActivity(i);
	}
	
	public void onClickLaunchMyBrowser(View view) {
		Intent i = new Intent("net.example.MyBrowser");
		i.setData(Uri.parse("http://www.amazon.com"));
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intents, menu);
		return true;
	}

}