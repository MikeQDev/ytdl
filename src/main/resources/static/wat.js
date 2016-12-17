function getUpdates(token) {
	if (issue >= 5) {
		document.getElementById('messages').innerHTML = "Issue completing your request... Ensure that your submission is valid.";
		return;
	} else if (kill) {
		return;
	}
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		var responseText;
		if (xhr.readyState == XMLHttpRequest.DONE) {
			responseText = xhr.responseText;
		}
		if (responseText != null)
			updateTable(responseText);
	}
	var startTime = new Date().getTime();
	while (new Date().getTime() < startTime + 250);
	xhr.open('GET',
			'http://'+window.location.hostname+':'+window.location.port+'/api/v1/status?sessionToken=' + token, false);
	xhr.send(null);
}
var issue = 0;
var kill = false;
var token = window.location.search.substring(1).split('=')[1];
getUpdates(token);

if (!kill)
	setInterval(function() {
		getUpdates(token);
	}, 3000);

function updateTable(responseText) {
	if (responseText == 'Invalid token request') {
		issue++;
	}
	var resp = responseText.split('|||||');
	var temp = '<tbody id="muh">';

	for (var i = 0; i < resp.length - 1; i++) {
		var split = resp[i].split('@@@');
		temp += '<tr>';
		temp += ('<td class="col-sm-1">' + (i + 1) + '</td>');
		temp += ('<td class="col-md-1"><a href="' + split[0] + '">' + split[0] + '</a></td>');
		temp += ('<td class="col-md-1">' + split[1] + '</td>');
		if (split[2] == '0')
			temp += ('<td class="col-md-1">Received</td>');
		else if (split[2] == '1')
			temp += ('<td class="col-md-1">Downloading...</td>');
		else if (split[2] == '2')
			temp += ('<td class="col-md-1">Converting...</td>');
		else
			temp += ('<td class="col-md-1"><a href="/files?fileId=' + split[2] + '">Download</a></td>');
		temp += '</tr>';
	}
	// check if all downloaded?
	var finished = true;
	for (var i = 0; i < resp.length - 1; i++) {
		var split = resp[i].split('@@@');
		if (split[2] == 0 || split[2] == 1 || split[2] == 2)
			finished = false;
	}
	if (finished)
		kill = true;
	temp += '</tbody>';
	document.getElementById('muh').innerHTML = temp;
}
function downloadAll() {
	var xx = document.getElementById('muh').innerHTML;
	alert(xx);
}
