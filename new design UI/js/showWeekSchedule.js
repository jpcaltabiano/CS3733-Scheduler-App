var dateRange = [];
var timeRange = [];
var slotRange = [];
var unavailableSlots = [];
var scheduledSlots = [];
var scheduleID = "";

function handleShowWeekScheduleClick(e) {
	var form = document.showWeekScheduleForm;
	var id = form.id.value;
	scheduleID = id;
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
		if (xhr.readyState == XMLHttpRequest.DONE) {
      		processShowWeekScheduleResponse(xhr.responseText);
	    } else {
      		processShowWeekScheduleResponse("N/A");
	    }	
	};
}

function pad(num, n) {
  var len = num.toString().length;
  while(len < n) {
    num = "0" + num;
    len++;
  }
  return num;
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
	var days = scheduleJson["days"];
	var startDate = scheduleJson["startDate"];
	var endDate = scheduleJson["endDate"];
	var startHour = scheduleJson["startHour"];
	var endHour = scheduleJson["endHour"];
	var duration = scheduleJson["slotDuration"];
	console.log(duration);
	dateRange = [];
	timeRange = [];
	slotRange = [];
	unavailableSlots = [];
	scheduledSlots = [];
	var cut = 60 / duration;
	var index = 1;
	for (var i = startHour; i < endHour; i++) {
		var mins = 0;
		for (var j = 0; j < cut - 1; j++) {
			timeRange.push({ id: index, title: pad(i, 2) + ":" + pad(mins, 2) + " - " + pad(i, 2) + ":" + pad(mins + duration)});
			mins += duration;
			index++;
		}
		timeRange.push({ id: index, title: pad(i, 2) + ":" + pad(mins, 2) + " - " + pad(i + 1, 2) + ":00"});
		index++;
	}


	if (httpResult == 200) {
		for (var i = 0; i < days.length; i++) {
    		var dayJson = days[i];
    	
			var dayid = dayJson["dayid"];    	
    		var ddate = new Date(dayJson["date"]);
    		dateRange.push({ id: i, title: (dayJson["date"]["month"] + "/" + dayJson["date"]["day"]) });
    		var dshour = dayJson["startHour"];
    		var dehour = dayJson["endHour"];
    		var slots = dayJson["slots"];
    	
    		for (var j = 0; j < slots.length; j++) {
    			var slotJson = slots[j];
    			
    			var slotid = slotJson["slotid"];
    			var time = slotJson["time"];
    			
    			var state = slotJson["state"];
    			if(state == false) {
    				unavailableSlots.push(i + "-" + j);
    			}
    			
    			var meeting = slotJson["meeting"];
    			var pname = meeting["name"];
    			if(pname != null) {
    				scheduledSlots.push({ id: i + "-" + j, title: pname});
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

function processCreateMeetingResponse (result) {
	console.log("result: " + result);

	var js = JSON.parse(result);

	var message = js["message"];
	var meeting = js["meeting"];
	var pCode = js["participantCode"];
	var httpResult = js["httpCode"];

	if (httpResult == 200) {
		alert(message + "," + meeting + "," + pCode);
	} else {
		var msg = js ["message"];
		alert(msg);
	}
}

function prom(slotBoxId) {
    
    var userName = prompt("please input your name is: ");

    
    if (name)//如果返回的有内容
    {
        alert("Your meeting: create by " + userName)
    }
  if (confirm("Do you want to make this meeting? ")) {
        alert("The meeting will be created at " + slotBoxId);

	var slotid = slotBoxId;
	var user = userName;

	var data = {};
	data["scheduleID"] = scheduleID;
	data["slotID"] = slotid;
	data["user"] = user;
	
	var js = JSON.stringify(data);
	console.log("JS: " + js);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", create_meeting_url, true);

	xhr.send(js);

	xhr.onloadend = function () {

		if (xhr.readyState == XMLHttpRequest.DONE) {
      		processCreateMeetingResponse(xhr.responseText);
	    } else {
      		processCreateMeetingResponse("N/A");
	    }	
	};
    }
    else {
        alert("This meeting will be canceled");
    }
}
