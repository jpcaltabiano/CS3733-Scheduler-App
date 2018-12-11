package zosma.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import dao.ScheduleDao;
import zosma.model.Meeting;
import zosma.model.Schedule;

public class CancelMeetingHandler implements RequestStreamHandler {
	public LambdaLogger logger = null;
	// Load from RDS, if it exists
	//@throws Exception 
	boolean cancelMeeting(String scheduleID, String slotID, String code) throws Exception {
		if (logger != null) { logger.log("in cancelMeeting"); }
		ScheduleDao dao = new ScheduleDao();
		
		Schedule schedule = dao.getSchedule(scheduleID);
		schedule.cancelMeeting(slotID, code);
		return dao.updateSchedule(schedule);
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to cancel meeting");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CancelMeetingResponse response = null;

		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());

			String method = (String) event.get("httpMethod");
			if (method != null && method.equalsIgnoreCase("OPTIONS")) {
				logger.log("Options request");
				response = new CancelMeetingResponse("name", 200);  // OPTIONS needs a 200 response
				responseJson.put("body", new Gson().toJson(response));
				processed = true;
				body = null;
			} else {
				body = (String)event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new CancelMeetingResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			CancelMeetingRequest req = new Gson().fromJson(body, CancelMeetingRequest.class);
			logger.log(req.toString());

			CancelMeetingResponse resp;
			try {
				ScheduleDao dao = new ScheduleDao();
				
				if (cancelMeeting(req.scheduleID,req.slotID,req.code)) {
					resp = new CancelMeetingResponse("Successfully cancel meeting in schedule :" + req.scheduleID 
							+ ", in Timeslot :" + req.slotID, 200);
				} else if (dao.getSchedule(req.scheduleID) == null){
					resp = new CancelMeetingResponse("Unable to find schedule :" + req.scheduleID , 404);
				} else if (dao.getSchedule(req.scheduleID).getSlot(req.slotID) == null){
					resp = new CancelMeetingResponse("Unable to find time slot :" + req.slotID 
							+ ", in schedule :" + req.scheduleID, 404);
				} else if (dao.getSchedule(req.scheduleID).getSlot(req.slotID).getMeeting() == null){
					resp = new CancelMeetingResponse("There is no meeting in schedule :" + req.scheduleID 
							+ ", in Timeslot :" + req.slotID , 404);
				} else if (!dao.getSchedule(req.scheduleID).getSlot(req.slotID).getMeeting().checkCode(req.code)){
					resp = new CancelMeetingResponse("Incorrect secret code :" + req.slotID , 401);
				} else {
					resp = new CancelMeetingResponse("Unable to cancel meeting in schedule :" + req.scheduleID 
							+ ", in Timeslot :" + req.slotID, 200);
				}
			} catch (Exception e) {
				resp = new CancelMeetingResponse("Unable to cancel meeting: " + req.scheduleID + "(" + e.getMessage() + ")", 403);
			}

			// compute proper response
			responseJson.put("body", new Gson().toJson(resp));  
		}

		logger.log("end result:" + responseJson.toJSONString());
		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());  
		writer.close();
	}

}
