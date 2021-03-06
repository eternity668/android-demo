package com.pispower.catalog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.pispower.R;
import com.pispower.network.VideoClient;

public class LoadCatalogTask extends AsyncTask<Void, Void, List<CatalogInfo>> {
	private static final String TAG = "LoadPackageTask";
 
	// TextView对象
	private TextView listViewEmptyHintTextView;
	// ListView 对象
	// private ListView listView;
	// ProgressDialog 对象
	private ProgressDialog progressDialog;
	// adapter
	private CatalogListViewAdapter catalogListViewAdapter;

	/**
	 * 有参构造方法
	 * 
	 * @param progressDialog
	 * @param listView
	 * @param listViewEmptyHintTextView
	 * @param context
	 */
	public LoadCatalogTask(ProgressDialog progressDialog,
			TextView listViewEmptyHintTextView,
			CatalogListViewAdapter catalogListViewAdapter) {
		this.progressDialog = progressDialog;
		// this.listView = listView;
		this.listViewEmptyHintTextView = listViewEmptyHintTextView;
		this.catalogListViewAdapter = catalogListViewAdapter;
	}

	@Override
	protected List<CatalogInfo> doInBackground(Void... paramArrayOfVoid) {
		// 创建用于HTTP通信的VideoClient对象实例
		VideoClient videoClient = new VideoClient();
		// 创建空的分类信息列表
		List<CatalogInfo> catalogInfos = new ArrayList<CatalogInfo>();
		try {
			// 获取所有的分类
			JSONArray catalogs = videoClient.listCatalog();
			if (catalogs == null) {
				return catalogInfos;
			}
			for (int i = 0; i < catalogs.length(); i++) {

				JSONObject catalog = catalogs.getJSONObject(i);
				CatalogInfo catalogInfo = new CatalogInfo();
				catalogInfo.setId(catalog.getString("id"));
				catalogInfo.setName(catalog.getString("name"));
				catalogInfo.setHoldVideoNums(catalog
						.getString("videoNumber"));
				catalogInfo.setLastModifiedTime(catalog.getString("updateTime"));
				catalogInfos.add(catalogInfo);
			}

		} catch (Exception localException) {
			Log.e(TAG, localException.getMessage());
			return null;
		}
		return catalogInfos;
	}

	@Override
	protected void onPostExecute(List<CatalogInfo> paramList) {
		super.onPostExecute(paramList);
		if (this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}
		// 为ListView设置Adapter
		if ((!isCancelled()) && (paramList != null)) {
			this.listViewEmptyHintTextView.setText(R.string.noAnyVideos);
			this.catalogListViewAdapter.setDataList(paramList);
		} else {
			this.listViewEmptyHintTextView.setText(R.string.loadError);
			this.catalogListViewAdapter
					.setDataList(new ArrayList<CatalogInfo>());
		}
		this.catalogListViewAdapter.notifyDataSetChanged();
		return;
	}
}