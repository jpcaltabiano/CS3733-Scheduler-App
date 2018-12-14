function getSchedule(name,sdate,edate,shour,ehour,duration) {
	var nm = name;
	var sd = new Date(sdate);
	var ed = new Date(edate);
	var sh = shour;
	var eh = ehour;
	var du = duration;
	
	alert(nm + "," + sd + "," + ed + "," + ah + "," + eh + "," + du);
}

function refreshTable() {
var time = new Date()
const dateRange = [
  {id: 1, title: (time.getMonth()+1)+"/"+(time.getDate())},
  {id: 2, title: (time.getMonth()+1)+"/"+(time.getDate()+1)},
  {id: 3, title: (time.getMonth()+1)+"/"+(time.getDate()+2)},
  {id: 4, title: (time.getMonth()+1)+"/"+(time.getDate()+3)},
  {id: 5, title: (time.getMonth()+1)+"/"+(time.getDate()+4)},
];

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

//States

let unavailableSlots = [
  "1-1","1-2","1-3","1-4","1-5",
  "2-8",
  "3-1","3-2","3-3","3-4","3-5","3-6","3-7","3-8",
  "4-1","4-2","4-3","4-4","4-5","4-7","4-8",
  "5-1","5-6","5-7","5-8",
]

let scheduledSlots = [
  { id: "1-8", name: "Serina Jones"},
  { id: "2-2", name: "Stan Lee"},
  { id: "2-5", name: "Bob Thompson"},
  { id: "4-6", name: "Alex Krichovsky"}
]

// Build UI
const render = () => {
  $("#scheduler").empty();



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