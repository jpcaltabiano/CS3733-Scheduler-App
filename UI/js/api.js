var base_url = "https://zosmas3bucket.s3.us-east-2.amazonaws.com/UI/";

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

var create_schedule_url		= base_url + "schedule"; //PUT
var show_week_schedule_url	= base_url + "schedule"; //GET