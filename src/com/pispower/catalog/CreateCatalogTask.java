package com.pispower.catalog;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.pispower.R;
import com.pispower.network.VideoClient;

public class CreateCatalogTask extends AsyncTask<String, Void, CatalogInfo> {
	private static final String TAG = "CreateFileTask";
	// 场景对象
	private Context context;
	//CatalogListViewAdapter 对象
	private CatalogListViewAdapter catalogListViewAdapter;
	// 资源对象
	private Resources resources;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 有参构造方法
	 * 
	 * @param context
	 * @param listView
	 */
	public CreateCatalogTask(Context context, CatalogListViewAdapter catalogListViewAdapter) {
		this.context = context;
		this.catalogListViewAdapter = catalogListViewAdapter;
		this.resources = context.getResources();
	}

	@Override
	protected CatalogInfo doInBackground(String... paramArrayOfString) {
		// 获得新建分类的名字
		String catalogName = paramArrayOfString[0];
		// 创建用于HTTP通信的客服端
		VideoClient videoClient = new VideoClient();
		// 创建分类信息实例对象
		CatalogInfo catalogInfo = new CatalogInfo();
		try {
			// 创建分类通过VideoClient
			JSONObject catalogJSONObject = videoClient
					.createCatalog(catalogName);
			// 创建失败
			if (catalogJSONObject == null) {
				return null;
			}
			catalogInfo.setId(catalogJSONObject.getString("id"));
			catalogInfo.setName(catalogJSONObject.getString("name"));
			catalogInfo.setHoldVideoNums(this.context.getResources().getString(R.string.zero));
		    
			catalogInfo.setLastModifiedTime(dateFormat.format(new Date()));
			return catalogInfo;
		} catch (Exception localException) {
			Log.e(TAG, localException.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(CatalogInfo catalogInfo) {
		// 失败
		if (catalogInfo == null) {
			Toast.makeText(this.context,
					this.resources.getString(R.string.createCatalogFail),
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// 成功，更新相应的ListView
		catalogListViewAdapter.addData(catalogInfo, 0);
		catalogListViewAdapter.notifyDataSetChanged();

		Toast.makeText(this.context,
				this.resources.getString(R.string.createCatalogSuccess),
				Toast.LENGTH_LONG).show();

	}
}
