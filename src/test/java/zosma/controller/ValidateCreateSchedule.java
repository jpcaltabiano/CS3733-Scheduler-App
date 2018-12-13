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

public class ValidateCreateSchedule {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCreateSchedule() throws IOException {
		CreateScheduleHandler handler = new CreateScheduleHandler();

		CreateScheduleRequest cmr = new CreateScheduleRequest("test schedule","2018-12-03T00:00",
				"2018-12-04T00:00",8,9,30);

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("createSchedule"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
		System.out.println(resp);
		Schedule respSchedule = resp.schedule;

		Assert.assertEquals("Successfully create schedule :" + "test schedule" + " with schedule id :" + respSchedule.getScheduleID(), resp.message);
		Assert.assertEquals("test schedule", respSchedule.getName());
		Assert.assertEquals("2018-12-03T00:00", respSchedule.getSDate().toString());
		Assert.assertEquals("2018-12-04T00:00", respSchedule.getEDate().toString());
		Assert.assertEquals(8, respSchedule.getSHour());
		Assert.assertEquals(9, respSchedule.getEHour());
		Assert.assertEquals(30, respSchedule.getDur());

		/*/clean this schedule out of database
		DeleteScheduleHandler dshandler = new DeleteScheduleHandler();
		String scheduleid = respSchedule.getScheduleID();
		String code = respSchedule.getCode();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest(scheduleid,code);

		String dsRequest = new Gson().toJson(dsr);
		String dsjsonRequest = new Gson().toJson(new PostRequest(dsRequest));

		InputStream dsinput = new ByteArrayInputStream(dsjsonRequest.getBytes());
		OutputStream dsoutput = new ByteArrayOutputStream();

		dshandler.handleRequest(dsinput, dsoutput, createContext("deleteSchedule")); /*/
	}
}
