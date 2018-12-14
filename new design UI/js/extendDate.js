function handleExtendDateClick(e) {
	var form = document.extendDateForm;
	var scheduleid = form.scheduleid.value;
	var sdate = form.sdate.value;
	var edate = form.edate.value;
	var code = form.code.value;

	var data = {};
	data["scheduleID"] = scheduleid;
	data["startDate"] = sdate;
	data["endDate"] = edate;
	data["code"] = code;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", extend_date_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processExtendDateResponse(xhr.responseText);
	    } else {
      		processExtendDateResponse("N/A");
	    }	
	};
}

function processExtendDateResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var schedule = js["schedule"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message + "," + schedule);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}