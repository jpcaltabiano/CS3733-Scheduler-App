function handleShowWeekScheduleClick(e) {
	var form = document.showWeekScheduleForm;
	var id = form.id.value;
	var sdate = form.sdate.value;
	var edate = form.edate.value;

	alert(id + "," + sdate + "," + edate);
	
	var data = {};
	data["scheduleID"] = id;
	data["startDate"] = sdate + "T00:00:00";
	data["endDate"] = edate + "T00:00:00";
	
	var js = JSON.stringify(data);
	
	refreshWeekView(js); 
}

function refreshWeekView(js) {
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("GET", show_week_schedule_url, true);

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
	//TODO: put proper element name below
	var weekSchedule = document.getElementById('scheduler');

	var output = "";
	
	var scheduleJson = js["schedule"];
	var message = js["message"];
	var httpResult = js["httpCode"];
	
	console.log(dayJson); 
	var days = scheduleJson["days"];
	var startDate = scheduleJson["startDate"];
	var endDate = scheduleJson["endDate"];
	var startHour = scheduleJson["startHour"];
	var endHour = scheduleJson["endHour"];
	var duration = scheduleJson["slotDuration"];
	var dateRange;

	alert(message + "," + days + "," + startDate + "," + endDate + "," + startHour + "," + endHour + "," + duration);

	if (httpResult == 200) {
		for (var i = 0; i < days.length; i++) {
    	var dayJson = days[i];
    	console.log(dayJson); 
    	
    	var ddate = new Date(constantJson["date"]);
    	dateRange.append(ddate.getMonth+"/"+ddate.getDay);
    	var dshour = constantJson["startHour"];
    	var dehour = constantJson["endHour"];
    	var dslot = constantJson["slots"];
    	
    	const timeRanges = [
  			{ id: 1, title: "09:00 - 09:15"},
  			{ id: 2, title: "09:15 - 09:30"},
  			{ id: 3, title: "09:30 - 09:45"},
  			{ id: 4, title: "09:45 - 10:00"},
  			{ id: 5, title: "10:00 - 10:15"},
  			{ id: 6, title: "10:15 - 10:30"},
  			{ id: 7, title: "10:30 - 10:45"},
  			{ id: 8, title: "10:45 - 11:00"}
			]
				//build table
				let dr = $(`<thead><tr></tr></thead>`);

				dr.append("<td>Time</td>");
				dateRange.forEach( (d) => {
  				dr.append(`<td>${d.title}</td>`);
				})

				$("#scheduler").append(dr);

				timeRanges.forEach( (t) => {
				  let tr = $(`<tr id='${t.id}'></tr>`);
				  tr.append(`<td>${t.title}</td>`)
  
			  	dateRange.forEach( (d) => {
			   		let slotBox = `${d.id}-${t.id}`;
			    	if(unavailableSlots.indexOf(slotBox) >= 0) {
			      	tr.append(`<td class='unavailable'></td>`)
			    	} else {
			      	scheduledSlotsIds = scheduledSlots.map( (ss) => ss.id);
			      	let index = scheduledSlotsIds.indexOf(slotBox);
			      	if(index >= 0) {
			        	let ss = scheduledSlots[index];
			        	tr.append(`<td id='${slotBox}'>${ss.name}</td>`)
			      	} else {
			        	tr.append(`<td class='action' id='${slotBox}'><button id="Free" onclick="prom('${slotBox}')">free</button></td>`)
			      	}
			    }
			  });
			  $("#scheduler").append(tr);
			})
			}
			
		render();
    	}
	} else {
		var msg = js ["message"];
		alert(msg);
	}

}