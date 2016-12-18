package com.ytdl.conversion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import com.ytdl.YtdlConstants;
import com.ytdl.api.SessionHandler;

public class VideoDownloader {
	private static boolean debug = true;

	/**
	 * 
	 * @param url
	 *            YT video url
	 * @param id
	 *            id of session
	 * @param i
	 *            element of session
	 * @return
	 */
	public static boolean download(String url, UUID id, int i) {
		boolean downloadSuccess = false;
		InputStream stdout = null;
		InputStream stderr = null;
		InputStreamReader isr = null;
		InputStreamReader isrE = null;
		BufferedReader br = null;
		BufferedReader brE = null;
		try {
			ProcessBuilder pb = null;
			// TODO: try doing this with the -o command....
			if (System.getProperty("os.name").equals("Linux")) {
				pb = new ProcessBuilder("./youtube-dl", url, "-x", "--audio-format=mp3", "--no-playlist", "-o",
						YtdlConstants.content_directory + "%(title)s-%(id)s.%(ext)s");
			} else {
				pb = new ProcessBuilder("youtube-dl", url, "-x", "--audio-format=mp3", "--no-playlist", "-o",
						YtdlConstants.content_directory + "%(title)s-%(id)s.%(ext)s");
			}

			Process proc = pb.start();

			stdout = proc.getInputStream();
			stderr = proc.getInputStream();
			isr = new InputStreamReader(stdout);
			isrE = new InputStreamReader(stderr);
			br = new BufferedReader(isr);
			brE = new BufferedReader(isrE);
			String outLine = null;
			String errLine = null;
			while ((outLine = br.readLine()) != null || (errLine = brE.readLine()) != null) {
				if (outLine.startsWith("[ffm")) {
					SessionHandler.updateRequest(id, i, SessionHandler.STATUS_CONVERTING);
					// break;
				} else if (outLine.startsWith("Deleting")) {
					downloadSuccess = true;
					// break;
				}
				if (debug) {
					if (outLine != null)
						System.out.println("[Debug] " + outLine);
					if (errLine != null)
						System.err.println("<Error>" + errLine + "<Error>");
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				stdout.close();
				stderr.close();
				isr.close();
				isrE.close();
				br.close();
				brE.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return downloadSuccess;
	}

	private VideoDownloader() {
	}
}
