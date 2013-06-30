package com.example.activity101;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	String tag = "Lifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(tag, "In the onCreate() event");
    }
    public void onStart()
    {
    super.onStart();
    Log.d(tag, "In the onStart() event");
    }
    public void onRestart()
    {
    super.onRestart();  
    Log.d(tag, "In the onRestart() event");
    }
    public void onResume()
    {
    super.onResume();
    Log.d(tag, "In the onResume() event");
    }
    public void onPause()
    {
    super.onPause();
    Log.d(tag, "In the onPause() event");
    }
    public void onStop()
    {
    	super.onStop();
    	Log.d(tag, "In the onStop() event");
	}
	public void onDestroy()
	{
    	super.onDestroy();
    	Log.d(tag, "In the onDestroy() event");
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
