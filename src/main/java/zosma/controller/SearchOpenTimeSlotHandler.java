package zosma.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import ScheduleDao.ScheduleDao;
import zosma.model.Schedule;
import zosma.model.Timeslot;

public class SearchOpenTimeSlotHandler implements RequestStreamHandler {
	public LambdaLogger logger = null;
	// Load from RDS, if it exists
	//@throws Exception 
	ArrayList<Timeslot> searchOpenTimeSlot(String scheduleID, int month, int year, int dayOfWeek, int dayOfMonth) throws Exception {
		if (logger != null) { logger.log("in searchOpenTimeSlot"); }
		ScheduleDao dao = new ScheduleDao();
		
		return dao.getSchedule(scheduleID).searchOpenSlots(month, year, dayOfWeek, dayOfMonth);
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to search for open time slot");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		SearchOpenTimeSlotResponse response = null;

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
				response = new SearchOpenTimeSlotResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new SearchOpenTimeSlotResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			SearchOpenTimeSlotRequest req = new Gson().fromJson(body, SearchOpenTimeSlotRequest.class);
			logger.log(req.toString());

			SearchOpenTimeSlotResponse resp;
			try {
				ArrayList<Timeslot> slots = searchOpenTimeSlot(req.scheduleID,req.month,req.year,req.dayOfWeek,req.dayOfMonth);
				if (slots != null) {
					resp = new SearchOpenTimeSlotResponse("List of open time slots in schedule :" + req.scheduleID, slots,200);
				} else {
					resp = new SearchOpenTimeSlotResponse("Unable to find open time slot in schedule :" + req.scheduleID, 422);
				}
			} catch (Exception e) {
				resp = new SearchOpenTimeSlotResponse("Unable to search for open time slot(" + e.getMessage() + ")", 403);
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
