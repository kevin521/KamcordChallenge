package com.kevchen.kamcordchallenge;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeActivity extends Activity implements OnItemClickListener {

	JSONArray videoList = null;
	KamcordReceiver kr = null;
	int numVideos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// If the list of videos isn't loaded, it queries them from the server
		if (videoList == null) {
			kr = new KamcordReceiver();
			kr.retrieveList(this);
		}
	}

	// After a response is received from the server, the JSON file is parsed
	public void listReady(JSONArray videos) {
		videoList = videos; // This videoList will be used later to see which row was clicked by the user
		
		if (videos != null) {

			numVideos = videos.length();

			Video videoData[] = new Video[numVideos];

			for (int i = 0; i < videos.length(); i++) {

				try {
					System.out.println(videos.getJSONObject(i).getString(
							"title"));
					String title = videos.getJSONObject(i).getString("title");
					String thumbnail_url = ((JSONObject) videos
							.getJSONObject(i).get("thumbnails"))
							.getString("REGULAR");
					Bitmap thumbnail = new GetImage().execute(thumbnail_url)
							.get();
					String video_url = videos.getJSONObject(i).getString(
							"video_url");

					videoData[i] = new Video(title, thumbnail, video_url);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			
			// dismiss the loading screen (NEED TO CHANGE THIS IS AWFUL CODE)
			kr.pd.dismiss();

			// Custom list adapter for the custom rows
			VideoListAdapter adapter = new VideoListAdapter(this,
					R.layout.activity_listview, videoData);

			ListView listView = (ListView) findViewById(R.id.videoListView);
			listView.setOnItemClickListener(this);
			listView.setAdapter(adapter);

		}
	}

	// Gets the bitmap images
	// TODO move this to a seperate class for async tasks
	private class GetImage extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(
						params[0]).getContent());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}

	}

	// Right now clicking a row opens the video in the default video app on the phone
	// TODO Have the video load in the app
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		String message = null;
		try {
			message = ((JSONObject) videoList.get(position))
					.getString("video_url");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(message), "video/*");
		startActivity(intent);

	}
}
