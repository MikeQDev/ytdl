package com.ytdl.api;


import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ytdl.SingleRequest;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

	private boolean success = true;

	@RequestMapping("/")
	public String index() {
		return "API v1.0";
	}

	@RequestMapping("/request")
	public ModelAndView getRequests(String vUrls) {
		success = true;
		UUID id = UUID.randomUUID();
		new Thread(new Runnable() {
			public void run() {
				if (!RequestHandler.submitRequest(id, vUrls))
					issueDetected();
			}
		}).start();
		if (success)
			return new ModelAndView(new RedirectView("/api/v1/track?sessionToken=" + id.toString(), true));
		else
			return new ModelAndView("/api/v1/issue?params=" + vUrls);
	}

	void issueDetected() {
		success = false;
	}

	@RequestMapping("/track")
	public String track(String sessionToken) {
		return "<!doctype html> <html> <head> <link rel=\"stylesheet\" href=\"/bootstrap.min.css\"> <link rel=\"stylesheet\" href=\"/bootstrap-theme.min.css\"> </head> <body> <div class=\"container\"> <div class=\"jumbotron\"> <img src=\"/logo1.png\" alt=\"YouTube Downloader\" height=\"160\" width=\"258\" /> </div> <center><p id=\"messages\"></p></center> <div mainContent> <center><a href=\"#\" onClick=\"window.location.reload();\">Nothing showing up? Refresh this page.</a></center><table class=\"table table-striped\" id=\"mytable\"> <thead> <tr> <th>#</th> <th>URL</th> <th>Title</th> <th>Status</th> </tr> </thead> <tbody id=\"muh\"> </tbody> </table> </div> </div> <button onClick=\"downloadAll()\";>Download</button> </body> <script src=\"/wat.js\"></script> </html>";
	}

	@RequestMapping("/issue")
	public String returnIssue(String params) {
		return "Issue getting data for: " + params;
	}

	@RequestMapping("/status")
	public String getStatus(String sessionToken) {
		//System.out.println(".......requesting token " + sessionToken);
		StringBuilder sB = new StringBuilder();
		try {
			List<SingleRequest> statusReq = SessionHandler.getStatus(UUID.fromString(sessionToken));
			for (SingleRequest r : statusReq) {
				sB.append(r + "|||||");
			}
			//System.out.println("ret:" + sB.toString());
		} catch (Exception e) {
			return "Invalid token request";
		}
		return sB.toString();
	}

	/*@RequestMapping("/files/getDownloadPath")
	public String getDownloadPath(String vId) {
		System.out.println("Trying to get path to " + vId);
		try {
			return "<a href=\""+RequestHandler.getMp3FilePath(vId, true)+"\">lol</a>";
		} catch (ArrayIndexOutOfBoundsException ex) {
			return "N/A";
		}
	}*/

}
