package com.pispower.catalog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pispower.BuildConfig;
import com.pispower.R;
import com.pispower.network.VideoClient;
import com.pispower.util.PullRefreshListView;
import com.pispower.util.PullRefreshListView.OnRefreshListener;

public class CatalogActivity extends Activity
{

    private static final String TAG = "CatalogActivity";

    private LayoutInflater inflater;
    private TextView listEmptyHintTextView;
    private PullRefreshListView pullRefreshListView;

    private CatalogListViewAdapter catalogListViewAdapter;

    private ProgressDialog progressDialog;
    private Resources resources;

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

    @Override
    protected void onCreate(Bundle paramBundle)
    {
	super.onCreate(paramBundle);
	if (BuildConfig.DEBUG)
	{
	    Log.d(TAG, "onCreate function is called");
	}
	setContentView(R.layout.activity_catalog);
	this.inflater = getLayoutInflater();
	// 获取ActionBar 并设置
	ActionBar localActionBar = getActionBar();
	localActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
	localActionBar.setDisplayHomeAsUpEnabled(true);

	this.resources = getResources();
	this.progressDialog = ProgressDialog.show(this,
		this.resources.getString(R.string.loading),
		this.resources.getString(R.string.loadDescription));
	// 设置ListView
	this.pullRefreshListView = ((PullRefreshListView) findViewById(R.id.catalogListView));
	this.listEmptyHintTextView = ((TextView) findViewById(R.id.emptyHint));
	this.pullRefreshListView.setEmptyView(this.listEmptyHintTextView);
	this.pullRefreshListView
		.setOnItemClickListener(new CatalogListViewItemClickListener(
			this));
	this.catalogListViewAdapter = new CatalogListViewAdapter(
		new ArrayList<CatalogInfo>(), this);
	this.pullRefreshListView.setAdapter(this.catalogListViewAdapter);
	this.pullRefreshListView.setOnRefreshListener(new OnRefreshListener() {

	    public void onRefresh()
	    {
		// Your code to refresh the list contents goes here

		// for example:
		// If this is a webservice call, it might be asynchronous so
		// you would have to call listView.onRefreshComplete(); when
		// the webservice returns the data

		new AsyncTask<Void, Void, List<CatalogInfo>>() {

		    @Override
		    protected List<CatalogInfo> doInBackground(
			    Void... paramArrayOfVoid)
		    {
			// 创建用于HTTP通信的VideoClient对象实例
			VideoClient videoClient = new VideoClient();
			// 创建空的分类信息列表
			List<CatalogInfo> catalogInfos = new ArrayList<CatalogInfo>();
			try
			{
			    // 获取所有的分类
			    JSONArray catalogs = videoClient.listCatalog();
			    if (catalogs == null)
			    {
				return catalogInfos;
			    }
			    for (int i = 0; i < catalogs.length(); i++)
			    {

				JSONObject catalog = catalogs.getJSONObject(i);
				CatalogInfo catalogInfo = new CatalogInfo();
				catalogInfo.setId(catalog.getString("id"));
				catalogInfo.setName(catalog.getString("name"));
				catalogInfo.setHoldVideoNums(catalog
					.getString("videoNumber"));
				catalogInfo.setLastModifiedTime(catalog
					.getString("updateTime"));
				catalogInfos.add(catalogInfo);
			    }

			}
			catch (Exception localException)
			{
			    Log.e(TAG, localException.getMessage());
			    return null;
			}
			return catalogInfos;
		    }

		    @Override
		    protected void onPostExecute(List<CatalogInfo> paramList)
		    {
			super.onPostExecute(paramList);

			// 为ListView设置Adapter
			// CatalogListViewAdapter listViewAdapter = null;
			if ((!isCancelled()) && (paramList != null))
			{
			    listEmptyHintTextView.setText(R.string.noAnyVideos);
			    catalogListViewAdapter.setDataList(paramList);
			    pullRefreshListView.onRefreshComplete();
			    catalogListViewAdapter.notifyDataSetChanged();

			}
			else
			{
			    pullRefreshListView.onRefreshComplete();
			}
			return;
		    }

		}.execute(new Void[] { null, null });

	    }
	});
	// 开启异步任务，用于从亦云视频下载视频分类
	new LoadCatalogTask(this.progressDialog, this.listEmptyHintTextView,
		this.catalogListViewAdapter).execute(new Void[] { null, null });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
	switch (paramMenuItem.getItemId())
	{
	    case android.R.id.home:
		// 回到父activity
		NavUtils.navigateUpFromSameTask(this);
	    default:
		break;
	}
	return super.onOptionsItemSelected(paramMenuItem);
    }

    /**
     * 响应ImageButton点击事件的方法，用来显示dialog并创建视频分类
     * 
     * @param paramView
     */
    @SuppressLint({ "InflateParams" })
    public void createCatalog(View paramView)
    {
	// 创建对话框
	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	final View view = this.inflater.inflate(R.layout.add_file, null);
	dialogBuilder.setView(view);
	dialogBuilder.setPositiveButton(R.string.add_file_dialog_add_button,
		new DialogInterface.OnClickListener() {
		    public void onClick(
			    DialogInterface paramAnonymousDialogInterface,
			    int paramAnonymousInt)
		    {
			String str = ((EditText) view
				.findViewById(R.id.addfileName)).getText()
				.toString();
			// 开启异步任务，用于创建视频分类
			new CreateCatalogTask(CatalogActivity.this,
				CatalogActivity.this.catalogListViewAdapter)
				.execute(new String[] { str });

			paramAnonymousDialogInterface.dismiss();
		    }
		});
	dialogBuilder.setNegativeButton(R.string.add_file_dialog_cancel_button,
		new DialogInterface.OnClickListener() {
		    public void onClick(
			    DialogInterface paramAnonymousDialogInterface,
			    int paramAnonymousInt)
		    {
			paramAnonymousDialogInterface.dismiss();
		    }
		});
	dialogBuilder.create().show();
    }

}
