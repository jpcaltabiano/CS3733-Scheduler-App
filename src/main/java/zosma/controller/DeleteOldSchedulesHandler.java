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

import dao.ScheduleDao;
import zosma.model.Schedule;

public class DeleteOldSchedulesHandler implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	// Load from RDS, if it exists
	//@throws Exception 
	boolean deleteOldSchedules(int day) throws Exception {
		if (logger != null) { logger.log("in deleteOldSchedules"); }
		ScheduleDao dao = new ScheduleDao();
		
		ArrayList<Schedule> schedules = dao.getAllSchedules();
		if(schedules.size() == 0) {
			return false;
		}
		
		LocalDateTime time = LocalDateTime.now().minusDays(day); 
		for (Schedule s : schedules) {
			if (s.getCreatedDate().isBefore(time)) {
				dao.removeSchedule(s.getScheduleID());
			}
		}
		return true;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to delete old schedules");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "DELETE,GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		DeleteOldSchedulesResponse response = null;

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
				response = new DeleteOldSchedulesResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new DeleteOldSchedulesResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			DeleteOldSchedulesRequest req = new Gson().fromJson(body, DeleteOldSchedulesRequest.class);
			logger.log(req.toString());

			DeleteOldSchedulesResponse resp;
			try {
				if (deleteOldSchedules(req.day)) {
					resp = new DeleteOldSchedulesResponse("Successfully delete schedules older than " + req.day + " days",200);
				} else {
					resp = new DeleteOldSchedulesResponse("Unable to  delete old schedules", 422);
				}
			} catch (Exception e) {
				resp = new DeleteOldSchedulesResponse("Unable to  delete old schedules(" + e.getMessage() + ")", 403);
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
