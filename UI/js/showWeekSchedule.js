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
	//TODO: put proper element name below
	var weekSchedule = document.getElementById('scheduler');

	var output = "";
	
	var schedule = js["schedule"];
	var message = js["message"];
	var httpResult = js["httpCode"];
	
	console.log(schedule); 
	var days = schedule["days"];
	var startDate = schedule["startDate"];
	var endDate = schedule["endDate"];
	var startHour = schedule["startHour"];
	var endHour = schedule["endHour"];
	var duration = schedule["slotDuration"];
	var dateRange = [];
	var slotRange = [];
	var unavailableSlots = [];
	var scheduledSlots = [];

	if (httpResult == 200) {
		for (var i = 0; i < days.length; i++) {
    		var day = days[i];
    		console.log(day); 
    	
			var dayid = day["dayid"];    	
    		var ddate = day["date"];
    		dateRange.push({ index: i, dayid: dayid, title: (ddate.month + "/" + ddate.day) });
    		var dshour = day["startHour"];
    		var dehour = day["endHour"];
    		var slots = day["slots"];
    	
    		for (var j = 0; j < slots.length; j++) {
    			var slot = slots[j];
    			console.log(slot); 
    			
    			var slotid = slot["slotid"];
    			var time = slot["time"];
    			if (i == 0) {
    				slotRange.push({ index: j, slotid: slotid, title: time.time.hour + ":" + time.time.minute });
    			}
    			var state = slot["state"];
    			if(state == false) {
    				unavailableSlots.push({ index: i + "-" + j, slotid: slotid});
    			}
    			
    			var meeting = slot["meeting"];
    			var pname = meeting["name"];
       			if(pname != null) {
    				scheduledSlots.push({ index: i + "-" + j, slotid: slotid, title: pname});
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
	
			slotRange.forEach( (t) => {
				let tr = $(`<tr id='${t.index}'></tr>`);
				tr.append(`<td>${t.title}</td>`);
	  
				dateRange.forEach( (d) => {
				   	let slotBox = `${d.index}-${t.index}`;
				    if(unavailableSlots.indexOf(slotBox) >= 0) {
				      	tr.append(`<td class='unavailable'></td>`)
				    } else {
				      	scheduledSlotsIds = scheduledSlots.map( (ss) => ss.index);
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

//A box will be appear to let user input the name of meeting 
function prom(slotBoxId) {
    
    var userName = prompt("please input your name is: ");

    
    if (name)//如果返回的有内容
    {
        alert("Your meeting: create by " + userName)
    }
  if (confirm("Do you want to make this meeting? ")) {
        alert("The meeting will be created at " + slotBoxId);
      let newScheduledSlots = scheduledSlots.concat({
        id: slotBoxId,
        name: userName
      })
      scheduledSlots = newScheduledSlots;
      render();
    }
    else {
        alert("This meeting will be canceled");
    }
}