function handleReportActivityClick(e) {
	var form = document.reportActivityForm;
	var hour = form.hour.value;

	var data = {};
	data["hour"] = hour;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", report_activity_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processReportActivityResponse(xhr.responseText);
	    } else {
      		processReportActivityResponse("N/A");
	    }	
	};
}

function processReportActivityResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var schedules = js["schedules"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message + "," + schedules);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}