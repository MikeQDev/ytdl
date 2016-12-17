package com.ytdl;

public class SingleRequest {
	private String url, title, mp3Url, id;
	private int status;


	public SingleRequest setMp3Url(String mp3Url) {
		this.mp3Url = mp3Url;
		return this;
	}

	public String getMp3Url() {
		return mp3Url;
	}
	
	public String getId() {
		return url;
	}

	public SingleRequest setId(String id) {
		this.id = id;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public SingleRequest setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public SingleRequest setTitle(String title) {
		this.title = title;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public SingleRequest setStatus(int status) {
		this.status = status;
		return this;
	}

	public String toString() {
		if (status == 3)
			return url + "@@@" + title + "@@@" + id;
		return url + "@@@" + title + "@@@" + status;
	}
}
