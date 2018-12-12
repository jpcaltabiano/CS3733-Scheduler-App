var base_url = "https://7r780ch9oh.execute-api.us-east-2.amazonaws.com/beta/";

var create_schedule_url			= base_url + "schedule";		//POST
var delete_schedule_url 	 	= base_url + "schedule";		//DELETE
var show_week_schedule_url		= base_url + "schedule";		//GET
var extend_date_url				= base_url + "schedule/date";	//POST
var search_open_time_slot_url 	= base_url + "timeslot";		//GET
var open_slot_url 				= base_url + "timeslot/open";	//POST
var close_slot_url			 	= base_url + "timeslot/close";	//POST	
var create_meeting_url 			= base_url + "meeting";			//POST
var cancel_meeting_url			= base_url + "meeting";			//DELETE
var delete_old_schedules_url	= base_url + "admin";			//DELETE
var report_activity_url			= base_url + "admin";			//POST