function processCreateScheduleResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var schedule = js["schedule"];
	var message = js["message"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		document.createForm.result.value = httpResult;
	} else {
		var msg = js ["errorMessage"];
		document.addForm.result.value = "error: " + msg;
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
	data["startDate"] = sdate;
	data["endDate"] = edate;
	data["startHour"] = shour;
	data["endHour"] = ehour;
	data["duration"] = duration;

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