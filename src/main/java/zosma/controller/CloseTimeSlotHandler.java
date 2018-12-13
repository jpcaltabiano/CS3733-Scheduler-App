package zosma.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import dao.ScheduleDao;
import zosma.model.Schedule;
import zosma.model.Timeslot;

public class CloseTimeSlotHandler implements RequestStreamHandler {
	public LambdaLogger logger = null;
	// Load from RDS, if it exists
	//@throws Exception 
	ArrayList<Timeslot> closeTimeSlot(String scheduleID, String slotID, String date, String time, String code) throws Exception {
		if (logger != null) { logger.log("in closeTimeSlot"); }
		ScheduleDao dao = new ScheduleDao();
		LocalDate parseDate = null;
		LocalTime parseTime = null;
		if(date != null) {
			parseDate = LocalDate.parse(date);
		}
		if(time != null) {
			parseTime = LocalTime.parse(time);
		}
		
		Schedule schedule = dao.getSchedule(scheduleID);
		ArrayList<Timeslot> close = schedule.closeSlot(slotID,parseDate,parseTime, code);
		dao.updateSchedule(schedule);
		return close;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to close time slot");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CloseTimeSlotResponse response = null;

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
				response = new CloseTimeSlotResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new CloseTimeSlotResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			CloseTimeSlotRequest req = new Gson().fromJson(body, CloseTimeSlotRequest.class);
			logger.log(req.toString());

			CloseTimeSlotResponse resp;
			try {
				ArrayList<Timeslot> close = closeTimeSlot(req.scheduleID,req.slotID,req.date,req.time,req.code);
				if (close != null) {
					resp = new CloseTimeSlotResponse("Successfully close time slot :"+ req.slotID 
							+ ", in schedule :" + req.scheduleID,close,200);
				} else {
					resp = new CloseTimeSlotResponse("Unable to close time slot :" + req.slotID 
							+ ", in schedule :" + req.scheduleID, 200);
				}
			} catch (Exception e) {
				resp = new CloseTimeSlotResponse("Unable to close time slot(" + e.getMessage() + ")", 403);
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
