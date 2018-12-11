package zosma.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import zosma.model.Schedule;

public class ValidateShowWeekSchedule {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testShowWeekSchedule() throws IOException {
		ShowWeekScheduleHandler handler = new ShowWeekScheduleHandler();

		// TODO: have valid attributes
		ShowWeekScheduleRequest cmr = new ShowWeekScheduleRequest("XHdtKhi4jR4l","2018-12-03T00:00:00"
				,"2018-12-07T00:00:00");

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("showWeekSchedule"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		ShowWeekScheduleResponse resp = new Gson().fromJson(post.body, ShowWeekScheduleResponse.class);
		System.out.println(resp);
		Schedule respSchedule = resp.schedule;

		// TODO: have valid attributes
		Assert.assertEquals("Show week schedule of schedule :" + "XHdtKhi4jR4l", resp.message);
		Assert.assertEquals("XHdtKhi4jR4l", respSchedule.getScheduleID());
		Assert.assertEquals("2018-12-03T00:00:00", respSchedule.getSDate().toString());
		Assert.assertEquals("2018-12-07T00:00:00", respSchedule.getEDate().toString());
	}
}
