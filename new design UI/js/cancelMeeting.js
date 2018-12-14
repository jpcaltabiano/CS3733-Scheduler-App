function handleCancelMeetingClick(e) {
	var form = document.cancelMeetingForm;
	var scheduleid = form.scheduleid.value;
	var slotid = form.slotid.value;
	var code = form.code.value;

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
      		processCancelMeetingResponse(xhr.responseText);
	    } else {
      		processCancelMeetingResponse("N/A");
	    }	
	};
}

function processCloseTimeSlotResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}