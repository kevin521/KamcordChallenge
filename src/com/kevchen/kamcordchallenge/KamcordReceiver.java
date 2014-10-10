package com.kevchen.kamcordchallenge;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class KamcordReceiver {
	ProgressDialog pd;
	Activity activity;

	public void retrieveList(Activity act) {
		activity = act;
		new AsyncSearch().execute();
	}

	private class AsyncSearch extends AsyncTask<String, Void, JSONArray> {
		protected void onPreExecute() {
			pd = new ProgressDialog(activity);
			pd.setTitle("Loading...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		protected JSONArray doInBackground(String... urls) {
			try {
				return getList();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(JSONArray videos) {
			try {
				((HomeActivity) activity).listReady(videos);
			} catch (ClassCastException c) {
				((HomeActivity) activity).listReady(videos);
			}
		}

		private JSONArray getList() throws JSONException {
			URL u;
			JSONArray videos = new JSONArray();
			try {
				u = new URL("https://www.kamcord.com/app/v2/videos/feed/?feed_id=0");
				final URLConnection conn = u.openConnection();
				conn.connect();

				System.out.println("Connected");

				BufferedInputStream bis = new BufferedInputStream(
						conn.getInputStream());

				byte[] response = new byte[0];

				System.out.println("Got response");

				int bytesRead = 0;
				String result = "";

				byte[] buff = new byte[999999];
				int k = -1;
				while ((k = conn.getInputStream().read(buff, 0, buff.length)) > -1) {

					// temp buffer size = bytes already read + bytes last read
					byte[] tbuff = new byte[response.length + k];
					System.arraycopy(response, 0, tbuff, 0, response.length);
					// copy previous bytes
					System.arraycopy(buff, 0, tbuff, response.length, k);
					// copy current lot
					response = tbuff; // call the temp buffer as your result
										// buff
				}

				result = new String(response);
				JSONObject temp = new JSONObject(result);
				JSONObject res = (JSONObject) temp.get("response");
				videos = (JSONArray) res.get("video_list");
				
				
				bis.close();

				System.out.println("Closed connection");
			} catch (MalformedURLException e) {
				JSONObject temp = new JSONObject();
				temp.put("text",
						"Error: Could not connect. Please check your connection.");
				videos.put(temp);
			} catch (IOException e) {
				JSONObject temp = new JSONObject();
				temp.put(
						"text",
						"Error: Could not parse data from the Kamcord website. Please check your connection.");
				videos.put(temp);
			}

			return videos;

		}
	}
}
