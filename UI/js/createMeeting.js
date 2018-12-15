function handleCreateMeetingClick(e) {
	var form = document.createMeetingForm;
	var scheduleid = form.scheduleid.value;
	var slotid = form.slotid.value;
	var user = form.user.value;

	var data = {};
	data["scheduleID"] = scheduleid;
	data["slotID"] = slotid;
	data["user"] = user;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", create_meeting_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processCreateMeetingResponse(xhr.responseText);
	    } else {
      		processCreateMeetingResponse("N/A");
	    }	
	};
}

function processCreateMeetingResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var meeting = js["meeting"];
	var pCode = js["participantCode"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message + "," + meeting + "," + pCode);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}