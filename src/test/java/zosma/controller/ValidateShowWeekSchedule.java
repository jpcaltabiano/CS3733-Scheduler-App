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
		//first create schedule
		CreateScheduleHandler cshandler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("test schedule","2018-12-01T00:00:00",
				"2018-12-31T00:00:00",8,9,60);

		String csRequest = new Gson().toJson(csr);
		String csjsonRequest = new Gson().toJson(new PostRequest(csRequest));

		InputStream csinput = new ByteArrayInputStream(csjsonRequest.getBytes());
		OutputStream csoutput = new ByteArrayOutputStream();

		cshandler.handleRequest(csinput, csoutput, createContext("createSchedule"));

		PostResponse cspost = new Gson().fromJson(csoutput.toString(), PostResponse.class);
		CreateScheduleResponse csresp = new Gson().fromJson(cspost.body, CreateScheduleResponse.class);
		Schedule respSchedule = csresp.schedule;

		//show this schedule
		ShowWeekScheduleHandler swhandler = new ShowWeekScheduleHandler();
		String scheduleid = respSchedule.getScheduleID();

		ShowWeekScheduleRequest swr = new ShowWeekScheduleRequest(scheduleid,"2018-12-03T00:00"
				,"2018-12-07T00:00");

		String swRequest = new Gson().toJson(swr);
		String swjsonRequest = new Gson().toJson(new PostRequest(swRequest));

		InputStream swinput = new ByteArrayInputStream(swjsonRequest.getBytes());
		OutputStream swoutput = new ByteArrayOutputStream();

		swhandler.handleRequest(swinput, swoutput, createContext("showWeekSchedule"));

		PostResponse swpost = new Gson().fromJson(swoutput.toString(), PostResponse.class);
		ShowWeekScheduleResponse swresp = new Gson().fromJson(swpost.body, ShowWeekScheduleResponse.class);
		System.out.println(swresp);
		Schedule respWeekSchedule = swresp.schedule;

		Assert.assertEquals("Show week schedule of schedule :" + scheduleid, swresp.message);
		Assert.assertEquals(scheduleid, respWeekSchedule.getScheduleID());
		Assert.assertEquals("2018-12-03T00:00", respWeekSchedule.getSDate().toString());
		Assert.assertEquals("2018-12-07T00:00", respWeekSchedule.getEDate().toString());

		//clean this schedule out of database
		DeleteScheduleHandler dshandler = new DeleteScheduleHandler();
		String code = respSchedule.getCode();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest(scheduleid,code);

		String dsRequest = new Gson().toJson(dsr);
		String dsjsonRequest = new Gson().toJson(new PostRequest(dsRequest));

		InputStream dsinput = new ByteArrayInputStream(dsjsonRequest.getBytes());
		OutputStream dsoutput = new ByteArrayOutputStream();

		dshandler.handleRequest(dsinput, dsoutput, createContext("deleteSchedule"));
	}
}
