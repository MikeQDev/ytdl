package com.ytdl.tagging;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MetaTagger {
	private static String API_BASE = "https://api.spotify.com/v1/search?type=artist,track&q=";
	private final static String[] BREAK_CHARS = { "[", "]", "(", ")", "lyrics", "LYRICS", "Lyrics", "feat", "FEAT",
			"Feat" };

	public static void tag(File in, String title)
			throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		System.err.println("meta tagger received: " + title);
		int min = 1337;
		for (String breakChar : BREAK_CHARS) {
			int cur = title.indexOf(breakChar);
			if (cur < min && cur != -1)
				min = cur;
		}
		// System.out.println("min: " + min);
		JSONObject jsO;
		if (min != 1337) {
			// System.err.println("querying: " + title.substring(0, min));
			jsO = hitApi(title.substring(0, min));
		} else {
			// System.err.println("querying entire title: " + title);
			jsO = hitApi(title);
		}
		if (jsO == null) {
			System.out.println("Could not retrieve API data for " + title);
			return;
		}

		MyMp3FileOverride mp3file = new MyMp3FileOverride(in);
		ID3v1 id3v1Tag;// mp3file.getId3v1Tag();
		if (mp3file.hasId3v1Tag()) {
			id3v1Tag = mp3file.getId3v1Tag();
		} else {
			// mp3 does not have an ID3v1 tag, let's create one..
			id3v1Tag = new ID3v1Tag();
			mp3file.setId3v1Tag(id3v1Tag);
		}

		id3v1Tag.setArtist(getArtist(jsO));
		id3v1Tag.setTitle(getTitle(jsO));
		id3v1Tag.setAlbum(getAlbum(jsO));

		mp3file.setId3v1Tag(id3v1Tag);
		mp3file.save(in.getAbsolutePath() + "t");

		mp3file = new MyMp3FileOverride(in.getAbsoluteFile() + "t");

		try {
			HttpURLConnection con = (HttpURLConnection) new URL(getAlbumArt(jsO)).openConnection();
			ID3v23Tag id3v2Tag = new ID3v23Tag();
			id3v2Tag.setAlbumImage(IOUtils.toByteArray(con.getInputStream()), "image/jpeg");

			mp3file.setId3v2Tag(id3v2Tag);
			in.getAbsolutePath().indexOf(".mp3");

			mp3file.save(in.getAbsolutePath());
		} catch (Exception x) {
			System.err.println("Issue getting album image for " + title);
		}

		if (!new File(in.getAbsoluteFile() + "t").delete())
			System.err.println("issue deleting temp file for: " + title);
	}

	private static String getAlbumArt(JSONObject jsObject) {
		return jsObject.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
	}

	private static String getAlbum(JSONObject jsObject) {
		return jsObject.getJSONObject("album").getString("name");
	}

	private static String getArtist(JSONObject jsObject) {
		return jsObject.getJSONArray("artists").getJSONObject(0).getString("name");
	}

	private static String getTitle(JSONObject jsObject) {
		return jsObject.getString("name");
	}

	private static JSONObject hitApi(String query) {
		query = query.replaceAll("[\\s]+", "%20");
		StringBuffer response = new StringBuffer();
		try {
			// System.err.println("hitting : " + API_BASE + query);
			HttpURLConnection con = (HttpURLConnection) new URL(API_BASE + query).openConnection();

			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
			con.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return new JSONObject(response.toString()).getJSONObject("tracks").getJSONArray("items").getJSONObject(0);
		} catch (Exception x) {
			return null;
		}
	}

	private MetaTagger() {
	}
}
