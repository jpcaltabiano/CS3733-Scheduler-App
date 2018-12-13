package zosma.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;

import dao.ScheduleDao;
import zosma.model.Schedule;

public class ShowWeekScheduleHandler implements RequestStreamHandler  {

	public LambdaLogger logger = null;
	// Load from RDS, if it exists
	//@throws Exception 
	Schedule showWeekSchedule(String scheduleID, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
		if (logger != null) { logger.log("in showWeekSchedule"); }
		ScheduleDao dao = new ScheduleDao();
		Schedule weekSchedule = dao.getSchedule(scheduleID).showWeekSchedule(startDate, endDate);
		weekSchedule.setScheduleID(scheduleID);
		return weekSchedule;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to show week schedule");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ShowWeekScheduleResponse response = null;

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
				response = new ShowWeekScheduleResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new ShowWeekScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			ShowWeekScheduleRequest req = new Gson().fromJson(body, ShowWeekScheduleRequest.class);
			logger.log(req.toString());

			ShowWeekScheduleResponse resp;
			try {
				Schedule schedule = showWeekSchedule(req.scheduleID, LocalDateTime.parse(req.startDate), LocalDateTime.parse(req.endDate));
				if (schedule != null) {
					resp = new ShowWeekScheduleResponse("Show week schedule of schedule :" + req.scheduleID, schedule,200);
				} else {
					resp = new ShowWeekScheduleResponse("Unable to find schedule :" + req.scheduleID, 404);
				}
			} catch (Exception e) {
				resp = new ShowWeekScheduleResponse("Unable to show week schedule(" + e.getMessage() + ")", 403);
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
