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

public class ValidateExtendDate {
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	@Test
	public void testExtendDate() throws IOException {
		//first create schedule
		CreateScheduleHandler cshandler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("test schedule","2018-12-03T00:00",
				"2018-12-07T00:00",8,9,60);

		String csRequest = new Gson().toJson(csr);
		String csjsonRequest = new Gson().toJson(new PostRequest(csRequest));

		InputStream csinput = new ByteArrayInputStream(csjsonRequest.getBytes());
		OutputStream csoutput = new ByteArrayOutputStream();

		cshandler.handleRequest(csinput, csoutput, createContext("createSchedule"));

		PostResponse cspost = new Gson().fromJson(csoutput.toString(), PostResponse.class);
		CreateScheduleResponse csresp = new Gson().fromJson(cspost.body, CreateScheduleResponse.class);
		Schedule respSchedule = csresp.schedule;

		//extend date
		ExtendDateHandler edhandler = new ExtendDateHandler();
		String scheduleid = respSchedule.getScheduleID();
		String code = respSchedule.getCode();

		ExtendDateRequest edr = new ExtendDateRequest(scheduleid,"2018-12-01T00:00","2018-12-14T00:00",code);

		String edRequest = new Gson().toJson(edr);
		String edjsonRequest = new Gson().toJson(new PostRequest(edRequest));

		InputStream edinput = new ByteArrayInputStream(edjsonRequest.getBytes());
		OutputStream edoutput = new ByteArrayOutputStream();

		edhandler.handleRequest(edinput, edoutput, createContext("extendDate"));

		PostResponse edpost = new Gson().fromJson(edoutput.toString(), PostResponse.class);
		ExtendDateResponse edresp = new Gson().fromJson(edpost.body, ExtendDateResponse.class);
		System.out.println(edresp);
		Schedule edrespSchedule = edresp.schedule;

		Assert.assertEquals("Extend date of schedule :" + scheduleid, edresp.message);
		Assert.assertEquals("2018-12-01T00:00", edrespSchedule.getSDate().toString());
		Assert.assertEquals("2018-12-14T00:00", edrespSchedule.getEDate().toString());
		
		//clean this schedule out of database
		DeleteScheduleHandler dshandler = new DeleteScheduleHandler();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest(scheduleid,code);

		String dsRequest = new Gson().toJson(dsr);
		String dsjsonRequest = new Gson().toJson(new PostRequest(dsRequest));

		InputStream dsinput = new ByteArrayInputStream(dsjsonRequest.getBytes());
		OutputStream dsoutput = new ByteArrayOutputStream();

		dshandler.handleRequest(dsinput, dsoutput, createContext("deleteSchedule"));
	}
}
