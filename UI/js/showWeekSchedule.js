function handleShowWeekScheduleClick(e) {
	var form = document.showWeekScheduleForm;
	var id = form.id.value;
	var sdate = form.sdate.value;
	var edate = form.edate.value;
	
	var data = {};
	data["scheduleID"] = id;
	data["startDate"] = sdate + "T00:00";
	data["endDate"] = edate + "T00:00";
	
	var js = JSON.stringify(data);
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
	
	var scheduleJson = JSON.parse(js["schedule"]);
	var message = js["message"];
	var httpResult = js["httpCode"];
	
	console.log(scheduleJson); 
	var days = scheduleJson["days"];
	var startDate = scheduleJson["startDate"];
	var endDate = scheduleJson["endDate"];
	var startHour = scheduleJson["startHour"];
	var endHour = scheduleJson["endHour"];
	var duration = scheduleJson["slotDuration"];
	var dateRange = {};
	var slotRange = {};
	var unavailableSlots = {};
	var scheduledSlots = {};
	
	alert(message + "," + days + "," + startDate + "," + endDate + "," + startHour + "," + endHour + "," + duration);

	if (httpResult == 200) {
		for (var i = 0; i < days.length; i++) {
    		var dayJson = JSON.parse(days[i]);
    		console.log(dayJson); 
    	
			var dayid = dayJson["dayid"];    	
    		var ddate = new Date(dayJson["date"]);
    		dateRange.append({ id: i, title: (ddate.getMonth + "/" + ddate.getDay) });
    		var dshour = dayJson["startHour"];
    		var dehour = dayJson["endHour"];
    		var slots = dayJson["slots"];
    	
    		for (var j = 0; j < slots.length; i++) {
    			var slotJson = JSON.parse(slots[i]);
    			console.log(dayJson); 
    			
    			var slotid = slotJson["slotid"];
    			var time = slotJson["time"];
    			timeRange.append({ id: j, title: time });
    			var state = slotJson["state"];
    			if(state == false) {
    				unavailableSlots.append(i + "-" + j);
    			}
    			
    			var meeting = slotJson["meeting"];
    			var pname = JSON.parse(meeting)["name"];
    			if(pname != null) {
    				scheduledSlots.append({ id: i + "-" + j, title: pname});
    			}
    		}
    	}		
    	
    	// Build UI
		const render = () => {
  			$("#scheduler").empty();
		
			//build table
			let dr = $(`<thead><tr></tr></thead>`);
	
			dr.append("<td>Time</td>");
			dateRange.forEach( (d) => {
	  			dr.append(`<td>${d.title}</td>`);
			})
	
			$("#weekSchedule").append(dr);
	
			timeRange.forEach( (t) => {
				let tr = $(`<tr id='${t.id}'></tr>`);
				tr.append(`<td>${t.title}</td>`);
	  
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
				$("#weekSchedule").append(tr);
			})
		}
		render();
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}