function handleSearchOpenTimeSlotClick(e) {
	var form = document.searchOpenTimeSlotForm;
	var scheduleid = form.scheduleid.value;
	var month = form.month.value;
	var year = form.year.value;
	var dweek = form.dweek.value;
	var dmonth = form.dmonth.value;

	var data = {};
	data["scheduleID"] = scheduleid;
	data["month"] = month;
	data["year"] = year;
	data["dayOfWeek"] = dweek;
	data["dayOfMonth"] = dmonth;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", search_open_time_slot_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processSearchOpenTimeSlotResponse(xhr.responseText);
	    } else {
      		processSearchOpenTimeSlotResponse("N/A");
	    }	
	};
}

function processSearchOpenTimeSlotResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var slots = js["slots"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message + "," + slots);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}