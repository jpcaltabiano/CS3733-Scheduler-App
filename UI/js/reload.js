var dateRange = [];
var slotRange = [];
var scheduledSlots = [];
var sID;
var sD;
var eD;
var sC;

function reload(schedule,organizer) {
	
	var weekSchedule = document.getElementById('weekSchedule');
	
	console.log(schedule); 
	var name = schedule["name"];
	var view;
	if(organizer) {
		view = "organizer view";
	}
	else {
		view = "participant view";
	}
	$("#weekScheduleName").value = "Schedule : " + name + " : "  + view;
	var scheduleID = schedule["scheduleid"];
		sID = scheduleID;
	var days = schedule["days"];
	var startDate = schedule["startDate"];
		var syear = startDate.date.year;
		var smonth = startDate.date.month;
		var sday = startDate.date.day;
		if (smonth < 10) {
			smonth = "0" + smonth;
		}
		if (sday < 10) {
			sday = "0" + sday;
		}
		sD = syear + "-" + smonth + "-" + sday;
	var endDate = schedule["endDate"];
		var eyear = endDate.date.year;
		var emonth = endDate.date.month;
		var eday = endDate.date.day;
		if (emonth < 10) {
			emonth = "0" + emonth;
		}
		if (eday < 10) {
			eday = "0" + eday;
		} 
		eD = eyear + "-" + emonth + "-" + eday;
	var startHour = schedule["startHour"];
	var endHour = schedule["endHour"];
	var duration = schedule["slotDuration"];
	var code = schedule["code"];
		sC = code;
	
	dateRange = [];
	slotRange = [];
	scheduledSlots = [];
	
	for (var i = 0; i < days.length; i++) {
		var day = days[i];
		console.log(day); 
	
		var dayid = day["dayid"];    	
		var ddate = day["date"];
		dryear = ddate.year;
		drmonth = ddate.month;
		drday = ddate.day;
		if (drmonth < 10) {
			drmonth = "0" + drmonth;
		}
		if (drday < 10) {
			drday = "0" + drday;
		}
		dateRange.push({ index: "" + i, dayid: dayid, title: (dryear + "-" + drmonth + "-" + drday) });
		var dshour = day["startHour"];
		var dehour = day["endHour"];
		var slots = day["slots"];
	
		for (var j = 0; j < slots.length; j++) {
			var slot = slots[j];
			console.log(slot); 
			
			var slotid = slot["slotid"];
			var time = slot["time"];
			if (i == 0) {
				var hour = time.time.hour;
				var min = time.time.minute;
				if (hour < 10) {
					hour = "0" + hour;
				}
				if (min == 0) {
					min = "00";
				}
				slotRange.push({ index: "" + j, slotid: slotid, title: hour + ":" + min });
			}
			var state = slot["state"];
			var meeting = slot["meeting"];
			var pname = meeting["user"];
			var pcode = meeting["participantCode"];
			scheduledSlots.push({ index: i + "-" + j, slotid: slotid, state: state, name: pname, code: pcode});
		}
	}
	render(organizer);
}

// Build UI
function render(organizer) {
	$("#weekSchedule").empty();
	
	dateRange.sort(function(a, b) {
  		if (a.index.toLowerCase() < b.index.toLowerCase()) {return -1;}
  		if (a.index.toLowerCase() > b.index.toLowerCase()) {return 1;}
  		return 0;
	});
	slotRange.sort(function(a, b) {
  		if (a.index.toLowerCase() < b.index.toLowerCase()) {return -1;}
  		if (a.index.toLowerCase() > b.index.toLowerCase()) {return 1;}
  		return 0;
	});
	scheduledSlots.sort(function(a, b) {
  		if (a.index.toLowerCase() < b.index.toLowerCase()) {return -1;}
  		if (a.index.toLowerCase() > b.index.toLowerCase()) {return 1;}
  		return 0;
	});
		
	//build table
	let dr = $(`<thead><tr></tr></thead>`);
	
	if(organizer) {
		dr.append(`<td class='action' id='${d.index}'>Extend Date <button id="ExtendStart" onclick="extend(sD,null)">start</button> <button id="ExtendEnd" onclick="extend(null,eD)">end</button></td>`);
	}
	else {
		dr.append(`<td>Time</td>`);
	}
	dr.append("<td>Time</td>");
	dateRange.forEach( (d) => {
		if(organizer) {
			dr.append(`<td class='action' id='${d.index}'>${d.title} <button id="OpenSlot" onclick="openSlot(null,'${d.index}',null)">open</button> <button id="CloseSlot" onclick="closeSlot(null,'${d.index}',null)">close</button></td>`);
		}
		else {
			dr.append(`<td>${d.title}</td>`);
		}
	})
	
	$("#weekSchedule").append(dr);
	
	slotRange.forEach( (t) => {
		let tr = $(`<tr id='${t.index}'></tr>`);
		if(organizer) {
			tr.append(`<td class='action' id='${t.index}'>${t.title} <button id="OpenSlot" onclick="openSlot(null,null,'${t.index}')">open</button> <button id="CloseSlot" onclick="closeSlot(null,null,'${t.index}')">close</button></td>`);
		}
		else {
			tr.append(`<td>${t.title}</td>`);
		}
			  
		dateRange.forEach( (d) => {
		   	let slotBox = `${d.index}-${t.index}`;
	      	scheduledSlotsIds = scheduledSlots.map( (ss) => ss.index);
	      	let index = scheduledSlotsIds.indexOf(slotBox);
	      	let ss = scheduledSlots[index];
	      	if(organizer) {
	      		if (ss.state == false) {
		      		tr.append(`<td class='action' id='${slotBox}'> CLOSED <button id="OpenSlot" onclick="openSlot('${slotBox}',null,null)">open</button></td>`);
		      	}
		      	else if(ss.name != "none") {
					tr.append(`<td class='action' id='${slotBox}'>${ss.name} <button id="CancelMeeting" onclick="cancelMeeting('${slotBox}',true)">cancel</button> <button id="CloseSlot" onclick="closeSlot('${slotBox}',null,null)">close</button></td>`);
		    	} else {
					tr.append(`<td class='action' id='${slotBox}'> <button id="CloseSlot" onclick="closeSlot('${slotBox}',null,null)">close</button></td>`);
		    	}
	      	}
	      	else {
		      	if (ss.state == false) {
		      		tr.append(`<td class='unavailable'> CLOSED </td>`);
		      	}
		      	else if(ss.name != "none") {
					tr.append(`<td class='action' id='${slotBox}'>${ss.name} <button id="CancelMeeting" onclick="cancelMeeting('${slotBox}',false)">cancel</button> </td>`)
		    	} else {
					tr.append(`<td class='action' id='${slotBox}'><button id="CreateMeeting" onclick="createMeeting('${slotBox}')">free</button></td>`)
		    	}
		    }
		});
		$("#weekSchedule").append(tr);
	})
}
function createMeeting(slotBox) {
    let index = scheduledSlotsIds.indexOf(slotBox);
	let ss = scheduledSlots[index];
	var slotid = ss.slotid;
	
    var user = prompt("please input your name : ");
	handleCreateMeetingClick(sID,slotid,user,sD,eD);
}

function cancelMeeting(slotBox,orgAcc) {
    let index = scheduledSlotsIds.indexOf(slotBox);
	let ss = scheduledSlots[index];
	var slotid = ss.slotid;
	var code = ss.code;
	if (orgAcc) {
		handleCancelMeetingClick(sID,slotid,sD,eD,sC);
	}
	else {
		var inputcode = prompt("please input secret code : ");
		
		if (code == inputcode)
		{
			handleCancelMeetingClick(sID,slotid,sD,eD,inputcode);
		}
		else {
			alert("Incorrect secret code");
		}
    }
}

function openSlot(slot,date,time) {
	var slotid;
	var dateid;
	var timeid;
	if (slot != null) {
		let index = scheduledSlotsIds.indexOf(slot);
		let ss = scheduledSlots[index];
		var slotid = ss.slotid;
	}
	if (date != null) {
		let dr = dateRange[date];
		var dateid = dr.title;
	}
	if (time != null) {
		let dr = slotRange[time];
		var timeid = dr.title;
	}
	
   	alert("opening time slot");
    handleOpenTimeSlotClick(sID,slotid,dateid,timeid,sD,eD,sC);
}

function closeSlot(slot,date,time) {
	var slotid;
	var dateid;
	var timeid;
	if (slot != null) {
		let index = scheduledSlotsIds.indexOf(slot);
		let ss = scheduledSlots[index];
		var slotid = ss.slotid;
	}
	if (date != null) {
		let dr = dateRange[date];
		var dateid = dr.title;
	}
	if (time != null) {
		let dr = slotRange[time];
		var timeid = dr.title;
	}
	
    alert("closing time slot");
   	handleCloseTimeSlotClick(sID,slotid,dateid,timeid,sD,eD,sC);
}