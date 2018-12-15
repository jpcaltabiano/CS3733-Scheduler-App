function handleOpenTimeSlotClick(sID,slid,dt,tm,sD,eD,sC) {
	var form = document.openTimeSlotForm;
	var scheduleid = sID;
	var slotid = slid;
	var date = dt;
	var time = tm;
	var code = sC;

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
      		processOpenTimeSlotResponse(xhr.responseText,sID,sD,eD,sC);
	    } else {
      		processOpenTimeSlotResponse("N/A");
	    }	
	};
}

function processOpenTimeSlotResponse(result,id,sd,ed,sC) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var slot = js["slot"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		showWeekSchedule(id,sd,ed,sC);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}