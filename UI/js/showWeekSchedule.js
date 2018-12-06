function refreshWeekView() {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", show_week_schedule_url, true);
	xhr.send();

	console.log("sent");

	xhr.onloadend = function () {
		if (xhr.readyState == XMLHttpRequest.DONE) {
			console.log("XHR: " + xhr.responseText);
			processShowWeekScheduleResponse(xhr.responseText);
		} else {
			processShowWeekScheduleResponse("N/A");
		}
	};
}

function processShowWeekScheduleResponse(result) {
	console.log("res: " + result);

	var js = JSON.parse(result);
	//TODO: put proper element name below
	var weekSchedule = document.getElementById('weekSchedule');

	var output = "";

	//do something
}