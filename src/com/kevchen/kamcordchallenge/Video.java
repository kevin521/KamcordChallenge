package com.kevchen.kamcordchallenge;

import android.graphics.Bitmap;

public class Video {
	public String video_url;
	public String title;
	public Bitmap thumbnail;

	public Video() {
		super();
	}

	public Video(String title, Bitmap thumbnail) {
		super();
		this.title = title;
		this.thumbnail = thumbnail;
	}

	public Video(String title, Bitmap thumbnail, String video_url) {
		super();
		this.video_url = video_url;
		this.title = title;
		this.thumbnail = thumbnail;
	}
}
