package zosma.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;

import ScheduleDao.ScheduleDao;
import zosma.model.Schedule;

public class ReportActivityHandler implements RequestStreamHandler  {

	public LambdaLogger logger = null;
	// Load from RDS, if it exists
	//@throws Exception 
	Set<Schedule> reportActivity(int hour) throws Exception {
		if (logger != null) { logger.log("in reportActivity"); }
		ScheduleDao dao = new ScheduleDao();
		
		Set<Schedule> schedules = dao.getAllSchedules();
		LocalDateTime time = LocalDateTime.now().minusHours(hour); 
		for (Schedule s : schedules) {
			if (s.getCreatedDate().isBefore(time)) {
				schedules.remove(s);
			}
		}
		return schedules;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to report activity");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ReportActivityResponse response = null;

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
				response = new ReportActivityResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new ReportActivityResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			ReportActivityRequest req = new Gson().fromJson(body, ReportActivityRequest.class);
			logger.log(req.toString());

			ReportActivityResponse resp;
			try {
				Set<Schedule> schedules = reportActivity(req.hour);
				if (schedules.size() > 0) {
					resp = new ReportActivityResponse("Report activity in last " + req.hour + "hours :", schedules,200);
				} else {
					resp = new ReportActivityResponse("Unable to report activity ", 422);
				}
			} catch (Exception e) {
				resp = new ReportActivityResponse("Unable to report activity: "  + "(" + e.getMessage() + ")", 403);
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
