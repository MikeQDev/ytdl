package com.ytdl.conversion;

@Deprecated
//...deprecated because youtube-dl does conversions for us
public class Conversion {
	/*
	 * private static boolean debug = true; public static boolean convert(String
	 * webmSource) { if(debug) System.out.println("Converting to mp3"); boolean
	 * success = true; InputStream stdout = null; InputStream stderr = null;
	 * InputStreamReader isr = null; InputStreamReader isrE = null;
	 * BufferedReader br = null; BufferedReader brE = null; String target =
	 * webmSource.toString().substring(0, webmSource.toString().length() - 5) +
	 * ".mp3"; if (new File(target).exists()) { if (debug) System.out.println(
	 * "mp3 file already exists"); return true; } try { ProcessBuilder pb = new
	 * ProcessBuilder(
	 * "D:/downloads/ffmpeg-20160701-2a8dadb-win64-static/bin/ffmpeg.exe", "-i",
	 * webmSource, target);
	 * 
	 * Process proc = pb.start(); stdout = proc.getInputStream(); stderr =
	 * proc.getInputStream(); isr = new InputStreamReader(stdout); isrE = new
	 * InputStreamReader(stderr); br = new BufferedReader(isr); brE = new
	 * BufferedReader(isrE); String outLine = null; String errLine = null; while
	 * ((outLine = br.readLine()) != null || (errLine = brE.readLine()) != null)
	 * { if (debug) { if (outLine != null) System.out.println("[Debug] " +
	 * outLine); if (errLine != null) System.err.println("<Error>" + errLine +
	 * "<Error>"); } }
	 * 
	 * } catch (Throwable t) { t.printStackTrace(); System.err.println(
	 * "(Error when trying to convert w/ ffmpeg"); success = false; } finally {
	 * try { stdout.close(); stderr.close(); isr.close(); isrE.close();
	 * br.close(); brE.close(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } } return success; }
	 * 
	 * private Conversion() { }
	 * 
	 */
}