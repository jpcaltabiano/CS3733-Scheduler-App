var dateRange = [];
var slotRange = [];
var unavailableSlots = [];
var scheduledSlots = [];
var sID;
var sD;
var eD;
var sC;

function reload(schedule) {
	
	var weekSchedule = document.getElementById('scheduler');
	
	console.log(schedule); 
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
	unavailableSlots = [];
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
		dateRange.push({ index: i, dayid: dayid, title: (dryear + "-" + drmonth + "-" + drday) });
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
				slotRange.push({ index: j, slotid: slotid, title: hour + ":" + min });
			}
			var state = slot["state"];
			var meeting = slot["meeting"];
			var pname = meeting["user"];
			var pcode = meeting["participantCode"];
			scheduledSlots.push({ index: i + "-" + j, slotid: slotid, state: state, name: pname, code: pcode});
		}
	}
	render();
}

// Build UI
const render = () => {
	$("#weekSchedule").empty();
		
	//build table
	let dr = $(`<thead><tr></tr></thead>`);

	dr.append("<td>Time</td>");
	dateRange.forEach( (d) => {
		dr.append(`<td class='action' id='${d.index}'>${d.title} <button id="OpenSlot" onclick="openSlot(null,'${d.index}',null)">open</button> <button id="CloseSlot" onclick="closeSlot(null,'${d.index}',null)">close</button></td>`);
	})
	
	$("#weekSchedule").append(dr);
	
	slotRange.forEach( (t) => {
		let tr = $(`<tr id='${t.index}'></tr>`);
		tr.append(`<td class='action' id='${t.index}'>${t.title} <button id="OpenSlot" onclick="openSlot(null,null,'${t.index}')">open</button> <button id="CloseSlot" onclick="closeSlot(null,null,'${t.index}')">close</button></td>`);
	  
		dateRange.forEach( (d) => {
		   	let slotBox = `${d.index}-${t.index}`;
	      	scheduledSlotsIds = scheduledSlots.map( (ss) => ss.index);
	      	let index = scheduledSlotsIds.indexOf(slotBox);
	      	let ss = scheduledSlots[index];
	      	if (ss.state == false) {
	      		tr.append(`<td class='unavailable'></td>`);
	      	}
	      	else if(ss.name != "none") {
				tr.append(`<td class='action' id='${slotBox}'>${ss.name} <button id="CancelMeetingg" onclick="cancelMeeting('${slotBox}')">cancel</button></td>`)
	    	} else {
				tr.append(`<td class='action' id='${slotBox}'><button id="CreateMeeting" onclick="createMeeting('${slotBox}')">free</button></td>`)
	    	}
		});
		$("#weekSchedule").append(tr);
	})
}
function createMeeting(slotBox) {
    let index = scheduledSlotsIds.indexOf(slotBox);
	let ss = scheduledSlots[index];
	var slotid = ss.slotid;
	
    var userName = prompt("please input your name : ");
    
    if (name)
    {
        alert("Your meeting: create by " + userName + ", have secret code: " + ss.code);
		var user = userName;
    	handleCreateMeetingClick(sID,slotid,user,sD,eD);
    }
}

function cancelMeeting(slotBox) {
    let index = scheduledSlotsIds.indexOf(slotBox);
	let ss = scheduledSlots[index];
	var slotid = ss.slotid;
	var code = ss.code;
	
    var inputcode = prompt("please input secret code : ");
    
    if (code == inputcode)
    {
        alert("The meeting will be canceled from " + slotBox);
    	handleCancelMeetingClick(sID,slotid,inputcode,sD,eD);
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
		let dr = timeRange[time];
		var timeid = dr.title;
	}
	
	var inputcode = prompt("please input secret code : ");
    if (sC == inputcode)
    {
    	alert("opening time slot");
    	handleOpenTimeSlotClick(sID,slotid,dateid,timeid,sD,eD,inputcode);
    }
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
		let dr = timeRange[time];
		var timeid = dr.title;
	}
	
	var inputcode = prompt("please input secret code : ");
    if (sC == inputcode)
    {
    	alert("closing time slot");
    	handleCloseTimeSlotClick(sID,slotid,dateid,timeid,sD,eD,inputcode);
    }
}