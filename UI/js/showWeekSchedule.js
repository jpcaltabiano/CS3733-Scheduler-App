var dateRange = [];
var slotRange = [];
var unavailableSlots = [];
var scheduledSlots = [];

function handleShowWeekScheduleClick(e) {
	var form = document.showWeekScheduleForm;
	var id = form.id.value;
	var sdate = form.sdate.value;
	var edate = form.edate.value;

	showWeekSchedule(id,sdate,edate);
}

function showWeekSchedule(id,sdate,edate)	 {

	var data = {};
	data["scheduleID"] = id;
	data["startDate"] = sdate + "T00:00";
	data["endDate"] = edate + "T00:00";
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", show_week_schedule_url, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processShowWeekScheduleResponse(xhr.responseText);
	    } else {
      		processShowWeekScheduleResponse("N/A");
	    }	
	};
}

function processShowWeekScheduleResponse(result) {
	console.log("res: " + result);

	var js = JSON.parse(result);
	
	var schedule = js["schedule"];
	var message = js["message"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		reload(schedule);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}
