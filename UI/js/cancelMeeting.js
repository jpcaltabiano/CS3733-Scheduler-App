function handleCancelMeetingClick(scheduleid,slotid,code,sd,ed) {

	var data = {};
	data["scheduleID"] = scheduleid;
	data["slotID"] = slotid;
	data["code"] = code;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("DELETE", cancel_meeting_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processCancelMeetingResponse(xhr.responseText,scheduleid,sd,ed);
	    } else {
      		processCancelMeetingResponse("N/A");
	    }	
	};
}

function processCancelMeetingResponse (result,id,sd,ed) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message);
		showWeekSchedule(id,sd,ed);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}