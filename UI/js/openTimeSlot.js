function handleOpenTimeSlotClick(e) {
	var form = document.openTimeSlotForm;
	var scheduleid = form.scheduleid.value;
	var slotid = form.slotid.value;
	var date = form.date.value;
	var time = form.time.value;
	var code = form.code.value;

	var data = {};
	data["scheduleID"] = scheduleid;
	data["slotID"] = slotid;
	data["date"] = date;
	data["time"] = time;
	data["code"] = code;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", open_slot_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processOpenTimeSlotResponse(xhr.responseText);
	    } else {
      		processOpenTimeSlotResponse("N/A");
	    }	
	};
}

function processOpenTimeSlotResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var slot = js["slot"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message + slot);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}