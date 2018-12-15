function handleDeleteScheduleClick(e) {
	var form = document.deleteScheduleForm;
	var scheduleid = form.scheduleid.value;
	var code = form.code.value;

	var data = {};
	data["scheduleID"] = scheduleid;
	data["code"] = code;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("DELETE", delete_schedule_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processDeleteScheduleResponse(xhr.responseText);
	    } else {
      		processDeleteScheduleResponse("N/A");
	    }	
	};
}

function processDeleteScheduleResponse (result) {
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