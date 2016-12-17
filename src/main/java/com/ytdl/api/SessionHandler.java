package com.ytdl.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.ytdl.SingleRequest;

public class SessionHandler {
	private static HashMap<UUID, List<SingleRequest>> hM = new HashMap<>();
	public static final int STATUS_RECEIVED = 0, STATUS_DOWNLOADING = 1, STATUS_CONVERTING = 2, STATUS_FINISHED = 3,
			STATUS_ERROR = 4;

	public static void addNewRequest(UUID id, LinkedList<SingleRequest> li) {
		hM.put(id, li);
	}
	
	// call this to free up memory?
	public static void resetSessions(){
		hM.clear();
	}

	public static List<SingleRequest> getStatus(UUID id) throws NullPointerException{
		System.out.println("getting status for : "+id+"....length:"+hM.get(id).size());
		return hM.get(id);
	}

	public static void updateRequest(UUID id, int element, int newStatus) {
		List<SingleRequest> tempList = hM.get(id);
		tempList.set(element, hM.get(id).get(element).setStatus(newStatus));
		hM.put(id, tempList);
		//getStatus(id);
	}
	/*public static void updateRequest(UUID id, int element, int newStatus, String dlLink) {
		List<SingleRequest> tempList = hM.get(id);
		tempList.set(element, hM.get(id).get(element).setStatus(newStatus).setMp3Url(dlLink));
		hM.put(id, tempList);
		getStatus(id);
	}*/

	private SessionHandler() {
	}
}
