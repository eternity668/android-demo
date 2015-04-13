package com.pispower.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pispower.BuildConfig;
import com.pispower.R;
import com.pispower.catalog.CatalogActivity;
import com.pispower.util.NetworkInspection;

public class StartActivity extends Activity
{

    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onRestoreInstanceState function is called");
	}
	setContentView(R.layout.activity_start);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.start, menu);
	return true;
    }

    /**
     * 响应按钮点击事件的方法，此方法用来启动一个新的Activity
     * 
     * @param view
     */
    public void startDemo(View view)
    {
	if (NetworkInspection.isExistingAnyNetwork(this))
	{
	    Intent intent = new Intent(this, CatalogActivity.class);
	    this.startActivity(intent);
	}
	else
	{
	    Toast.makeText(this, R.string.noAnyNetworks, Toast.LENGTH_LONG)
		    .show();
	}

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	int id = item.getItemId();
	if (id == R.id.action_settings)
	{
	    this.finish();
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
	super.onRestoreInstanceState(savedInstanceState);
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onRestoreInstanceState function is called");
	}

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
	super.onPostCreate(savedInstanceState);
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onPostCreate function is called");
	}
    }

    @Override
    protected void onStart()
    {
	super.onStart();
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onStart function is called");
	}
    }

    @Override
    protected void onRestart()
    {
	super.onRestart();
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onRestart function is called");
	}
    }

    @Override
    protected void onResume()
    {
	super.onResume();
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onResume function is called");
	}
    }

    @Override
    protected void onPostResume()
    {
	super.onPostResume();
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onPostResume function is called");
	}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
	super.onSaveInstanceState(outState);
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onSaveInstanceState function is called");
	}
    }

    @Override
    protected void onPause()
    {
	super.onPause();
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onPause function is called");
	}
    }

    @Override
    protected void onStop()
    {
	super.onStop();
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onStop function is called");
	}
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onDestroy function is called");
	}
    }

}
