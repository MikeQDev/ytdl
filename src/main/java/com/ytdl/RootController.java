package com.ytdl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ytdl.api.RequestHandler;

@RestController
public class RootController {
	@RequestMapping("/")
	public String index() {
		return "<!doctype html> <html> <head> <link rel=\"stylesheet\" href=\"bootstrap.min.css\"> <link rel=\"stylesheet\" href=\"bootstrap-theme.min.css\"></head> <body> <div class=\"container\"> <div class=\"jumbotron\"> <img src=\"logo1.png\" alt=\"YouTube Downloader\" height=\"160\" width=\"258\" /> </div> <div mainContent> <!--<form action=\"track\" method=\"get\"> </form>--> <center> <form role=\"form\" action=\"/api/v1/request\"> <div class=\"form-group\"> <label>Enter up to 10 YouTube videos, delimited by line and/or space:</label> <textarea rows=\"10\" class=\"form-control\" name=\"vUrls\"></textarea> </div> <button type=\"submit\" class=\"btn btn-default\">Submit</button> </form> </center> </div> </div> </body> </html>";
	}

	@RequestMapping(value = "/files", produces = "audio/mpeg3", method = RequestMethod.GET)
	public void getFile(String fileId, HttpServletResponse response) throws IOException {
		String path = RequestHandler.getMp3FilePath(fileId, true);
		System.err.println("path -> "+path+"\n"+new File(path).getName());
		response.addHeader("Content-Type", "audio/mpeg3");
		response.addHeader("Content-Disposition", "inline; filename=\"" + new File(path).getName() + "\"");
		InputStream is = new FileInputStream(path);
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
	}
}