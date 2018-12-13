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
import zosma.model.Schedule;

public class DeleteScheduleHandler  implements RequestStreamHandler {
	public LambdaLogger logger = null;
	// Load from RDS, if it exists
	//@throws Exception 
	boolean deleteSchedule(String scheduleID, String code) throws Exception {
		if (logger != null) { logger.log("in deleteSchedule"); }
		ScheduleDao dao = new ScheduleDao();
		if (dao.getSchedule(scheduleID) != null && dao.getSchedule(scheduleID).checkCode(code)) {
			return dao.removeSchedule(scheduleID);
		}
		return false;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to delete schedule");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "DELETE,GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		DeleteScheduleResponse response = null;

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
				response = new DeleteScheduleResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new DeleteScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			DeleteScheduleRequest req = new Gson().fromJson(body, DeleteScheduleRequest.class);
			logger.log(req.toString());

			DeleteScheduleResponse resp;
			try {
				if (deleteSchedule(req.scheduleID, req.code)) {
					resp = new DeleteScheduleResponse("Successfully delete schedule :" + req.scheduleID, 200);
				} else {
					resp = new DeleteScheduleResponse("Unable to delete schedule :" + req.scheduleID, 200);
				}
			} catch (Exception e) {
				resp = new DeleteScheduleResponse("Unable to delete schedule(" + e.getMessage() + ")", 403);
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
