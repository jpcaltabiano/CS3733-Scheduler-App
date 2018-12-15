function handleCreateMeetingClick(scheduleid,slotid,user,sd,ed) {

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
      		processCreateMeetingResponse(xhr.responseText,user,scheduleid,sd,ed);
	    } else {
      		processCreateMeetingResponse("N/A");
	    }	
	};
}

function processCreateMeetingResponse (result,user,id,sd,ed) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var meeting = js["meeting"];
	var pCode = js["participantCode"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		if(user == meeting["user"]) {
			alert("Your meeting: create by " + meeting["user"] + ", have secret code: " + pCode);
		}
		else {
			alert("This slot had been occupied");
		}
		showWeekSchedule(id,sd,ed);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}