package com.pispower.video;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pispower.R;
import com.pispower.network.VideoClient;
import com.pispower.util.MediaUtil;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

 
public class LoadVideoTask extends AsyncTask<String, Void, List<VideoInfo>> {

	private static final String TAG = "LoadVideoTask";

	private ProgressDialog progressDialog;

	private TextView videoEmpTextView;

	private VideoListAdapter videoListAdapter;

	private Resources resources;

	public LoadVideoTask(ProgressDialog progressDialog,
			TextView videoEmpTextView, VideoListAdapter videoListAdapter,
			Resources resources) {
		super();
		this.progressDialog = progressDialog;
		this.videoEmpTextView = videoEmpTextView;
		this.videoListAdapter = videoListAdapter;
		this.resources = resources;
	}

	@Override
	protected void onPostExecute(List<VideoInfo> result) {
		super.onPostExecute(result);
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (!isCancelled() && result != null) {
			videoEmpTextView.setText(R.string.videoListEmpty);
			videoListAdapter.setVideoInfoList(result);
			videoListAdapter.notifyDataSetChanged();
		} else {
			videoEmpTextView.setText(R.string.videoListLoadError);
		}
	}

	@Override
	protected List<VideoInfo> doInBackground(String... params) {
		String catalogId = params[0];
		VideoClient videoClient = new VideoClient();
		List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
		try {
			JSONArray jsonArray = videoClient.listVideo(catalogId);
			if (jsonArray == null) {
				return videoInfoList;
			}
			if (jsonArray.length() == 0) {
				return videoInfoList;
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				VideoInfo videoInfo = new VideoInfo();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				videoInfo.setId(jsonObject.getString("id"));
				String fileName=jsonObject.getString("name");
				videoInfo.setName(fileName);
				videoInfo.setSize("100MB");
				String status = jsonObject.getString("status");
				videoInfo.setStatus(status);
				if (status.equals("FINISH")) {
					if(MediaUtil.isAudioFileType(fileName)){
						String embedCode = videoClient.getVideoEmbedCode(
								jsonObject.getString("id"),
								resources.getString(R.string.audioClarity));
						videoInfo
								.setUrl(videoClient.getUrlFromEmbedCode(embedCode));	
					}else{
						String embedCode = videoClient.getVideoEmbedCode(
								jsonObject.getString("id"),
								resources.getString(R.string.clarity));
						videoInfo
								.setUrl(videoClient.getUrlFromEmbedCode(embedCode));
					}
				
				} else {
					videoInfo.setUrl(null);
				}
				videoInfoList.add(videoInfo);
			}
			return videoInfoList;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
	}

}