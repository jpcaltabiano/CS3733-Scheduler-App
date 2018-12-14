var dateRange = [];
var slotRange = [];
var unavailableSlots = [];
var scheduledSlots = [];

function handleShowWeekScheduleClick(e) {
	var form = document.showWeekScheduleForm;
	var id = form.scheduleid.value;
	var sdate = form.sdate.value;
	var edate = form.edate.value;
	var code = form.code.value;

	showWeekSchedule(id,sdate,edate,code);
}

function showWeekSchedule(id,sdate,edate,code) {

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
      		processShowWeekScheduleResponse(xhr.responseText,code);
	    } else {
      		processShowWeekScheduleResponse("N/A");
	    }	
	};
}

function processShowWeekScheduleResponse(result,code) {
	console.log("res: " + result);

	var js = JSON.parse(result);
	
	var schedule = js["schedule"];
	var message = js["message"];
	var httpResult = js["httpCode"];
	var sc = schedule["code"];

	if (httpResult == 200) {
		if(sc == code) {
		alert("organizer view of schedule : " + schedule.name);
		reload(schedule,true);
		}
		else {
		alert("participant view of schedule : " + schedule.name);
		reload(schedule,false);
		}
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}
