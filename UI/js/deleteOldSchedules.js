function handleDeleteOldSchedulesClick(e) {
	var form = document.deleteOldSchedulesForm;
	var day = form.day.value;

	var data = {};
	data["day"] = day;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("DELETE", delete_old_schedules_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processDeleteOldSchedulesResponse(xhr.responseText);
	    } else {
      		processDeleteOldSchedulesResponse("N/A");
	    }	
	};
}

function processDeleteOldSchedulesResponse (result) {
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