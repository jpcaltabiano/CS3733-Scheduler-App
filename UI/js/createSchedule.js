function processCreateScheduleResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	//var computation = js["value"];
	var httpResult = js["response"];

	if (httpResult == 200) {
		document.createForm.result.value = httpResult;
	} else {
		var msg = js ["errorMessage"];
		document.addForm.result.value = "error: " + msg;
	}
}

function handleCreateScheduleClick(e) {
	var form = document.createScheduleForm;
	var arg1 = form.name.value;
	var arg2 = form.value.value;

	var data = {};
	data["arg1"] = arg1;
	data["arg2"] = arg2;

	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("PUT", createSchedule, true);

	xhr.send(js);

	xhr.onloadend = function () {
		console.log(xhr);
		console.log(xhr.request);

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processAddResponse(arg1, arg2, xhr.responseText);
	    } else {
      		processAddResponse(arg1, arg2, "N/A");
	    }	
	};
}