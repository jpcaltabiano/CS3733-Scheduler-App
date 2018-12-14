var dateRange = [];
var slotRange = [];
var unavailableSlots = [];
var scheduledSlots = [];
var sID;
var sD;
var eD;

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
	
	dateRange = [];
	slotRange = [];
	unavailableSlots = [];
	scheduledSlots = [];
	
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
			if(state == false) {
				unavailableSlots.push({ index: i + "-" + j, slotid: slotid});
			}
			
			var meeting = slot["meeting"];
			var pname = meeting["user"];
			scheduledSlots.push({ index: i + "-" + j, slotid: slotid, name: pname});
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
		      	let ss = scheduledSlots[index];
		      	if(ss.name != "none") {
					tr.append(`<td id='${slotBox}'>${ss.name}</td>`)
		    	} else {
					tr.append(`<td class='action' id='${slotBox}'><button id="Free" onclick="prom('${slotBox}')">free</button></td>`)
		    	}
			}
		});
		$("#weekSchedule").append(tr);
	})
}
function prom(slotBox) {
    let index = scheduledSlotsIds.indexOf(slotBox);
	let ss = scheduledSlots[index];
	var slotid = ss.slotid;
	
    var userName = prompt("please input your name : ");
    
    if (name)
    {
        alert("Your meeting: create by " + userName)
    }
	alert("The meeting will be created at " + slotBox);
	var user = userName;
    handleCreateMeetingClick(sID,slotid,user,sD,eD);
}