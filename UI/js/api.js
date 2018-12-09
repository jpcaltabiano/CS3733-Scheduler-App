var base_url = "https://7r780ch9oh.execute-api.us-east-2.amazonaws.com/beta/";

/* INCORRECT
var createSchedlue 			 = base_url + "createSchedlue";				//PUT
var showWeekSchedule 		 = base_url + "showWeekSchedule";			//GET
var deleteSchedule 			 = base_url + "deleteSchedule";				//DELETE
var openSlot 				 = base_url + "openSlot";					//POST
var closeSlot 				 = base_url + "closeSlot";					//POST
var searchOpenTimeSlot 		 = base_url + "searchOpenTimeSlot";
var createMeeting 			 = base_url + "createMeeting";
var cancelMeeting 			 = base_url + "cancelMeeting";
var cancelMeetingByOrganizer = base_url + "cancelMeetingByOrganizer";	//
var extendStartingDate		 = base_url + "extendStartingDate";
var extendEndingDate		 = base_url + "extendEndingDate";
var deleteOldSchedules		 = base_url + "deleteOldSchedules";
var reportActivity			 = base_url + "reportActivity";
INCORRECT */

var create_schedule_url		= base_url + "schedule";		//POST
var show_week_schedule_url	= base_url + "schedule";		//GET
var searchOpenTimeSlot 		 = base_url + "timeslot";		//GET
var createMeeting 			 = base_url + "meeting";		//POST
var cancelMeeting 			 = base_url + "meeting";		//DELETE
var deleteOldSchedules		 = base_url + "admin";			//DELETE
var reportActivity			 = base_url + "admin";			//POST