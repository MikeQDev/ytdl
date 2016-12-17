package com.ytdl.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.ytdl.SingleRequest;
import com.ytdl.YtdlConstants;
import com.ytdl.conversion.VideoDownloader;
import com.ytdl.tagging.MetaTagger;

public class RequestHandler {
	private static final String NOEMBED_BASE = "https://noembed.com/embed?url=";

	public static boolean submitRequest(UUID id, String cUrls) {
		String[] vUrls = cUrls.split("[\\s,]+");
		// System.err.println("sbmtreq received id : " + id + " w/ " +
		// vUrls.length);
		LinkedList<SingleRequest> li = new LinkedList<SingleRequest>();
		for (String s : vUrls)
			try {
				String title = getTitle(s);
				li.add(new SingleRequest().setUrl(s).setId(getId(s)).setTitle(title)
						.setStatus(SessionHandler.STATUS_RECEIVED));
			} catch (JSONException x) {
				return false;
			}
		SessionHandler.addNewRequest(id, li);
		int i = -1;
		for (String vUrl : vUrls) {
			i++;
			if (exists(vUrl, "mp3")) {
				SessionHandler.updateRequest(id, i, SessionHandler.STATUS_FINISHED);
				continue;
			}
		}
		i = -1;
		for (String vUrl : vUrls) {
			i++;
			if (exists(vUrl, "mp3")) {
				SessionHandler.updateRequest(id, i, SessionHandler.STATUS_FINISHED);
				continue;
			}
			SessionHandler.updateRequest(id, i, SessionHandler.STATUS_DOWNLOADING);
			boolean mp3Created = VideoDownloader.download(vUrl, id, i);
			if (mp3Created) {
				try {
					MetaTagger.tag(new File(getMp3FilePath(vUrl, false)), getTitle(vUrl));
				} catch (ArrayIndexOutOfBoundsException | UnsupportedTagException | InvalidDataException
						| NotSupportedException | IOException e) {
					System.err.println("Issue getting metadata for :" + vUrl);
				}
				SessionHandler.updateRequest(id, i, SessionHandler.STATUS_FINISHED);
			} else
				SessionHandler.updateRequest(id, i, SessionHandler.STATUS_ERROR);
		}
		return true;
	}

	private static String getId(String id) {
		if (id.contains("&"))
			id = id.split("&")[0];
		if (id.contains("v="))
			id = id.split("v=")[1];

		return id;
	}

	public static boolean exists(String videoUrl, String extension) {
		File[] files = getFiles(videoUrl, extension);
		return files.length == 1;
	}

	public static String getMp3FilePath(String vUrl) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getFiles(vUrl, "mp3")[0].getAbsolutePath();
	}

	public static String getMp3FilePath(String vUrl, boolean vIdOnly) throws ArrayIndexOutOfBoundsException {
		if (!vIdOnly)
			return getFiles(vUrl, "mp3")[0].getAbsolutePath();
		return getFiles(vUrl)[0].getAbsolutePath();
	}

	public static File[] getFiles(String videoUrl, String extension) {
		File f = new File(YtdlConstants.content_directory);
		if (videoUrl.contains("&"))
			videoUrl = videoUrl.split("&")[0];
		String fileName = videoUrl.split("v=")[1] + "." + extension;
		// System.out.println("checking for " + fileName);
		return f.listFiles(new java.io.FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.toString().endsWith(fileName);
			}
		});
	}

	public static File[] getFiles(String vId) {
		File f = new File(YtdlConstants.content_directory);
		System.out.println("checking with " + vId);
		return f.listFiles(new java.io.FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.toString().endsWith(vId + ".mp3");
			}
		});
	}

	private static String getTitle(String url) {
		StringBuffer response = new StringBuffer();
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(NOEMBED_BASE + url).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "N/A";
		} catch (IOException e) {
			e.printStackTrace();
			return "N/A";
		}
		return new JSONObject(response.toString()).getString("title");
	}

	private RequestHandler() {
	}
}
