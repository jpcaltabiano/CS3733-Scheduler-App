function processCreateScheduleResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var schedule = js["schedule"];
	var scheduleID = js["scheduleID"];
	var message = js["message"];
	var code = js["secretCode"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message + ", and secret code :" + code);
		document.showWeekScheduleForm.result.value = scheduleID;
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}

function handleCreateScheduleClick(e) {
	var form = document.createScheduleForm;
	var name = form.name.value;
	var sdate = form.sdate.value;
	var edate = form.edate.value;
	var shour = form.shour.value;
	var ehour = form.ehour.value;
	var duration = form.duration.value;

	var data = {};
	data["name"] = name;
	data["startDate"] = sdate + "T00:00:00";
	data["endDate"] = edate + "T00:00:00";
	data["startHour"] = shour;
	data["endHour"] = ehour;
	data["slotDuration"] = duration;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", create_schedule_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processCreateScheduleResponse(xhr.responseText);
	    } else {
      		processCreateScheduleResponse("N/A");
	    }	
	};
}