package zosma.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import zosma.model.Schedule;

public class ValidateReportActivity {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testReportActivity() throws IOException {
		//first create schedule
		CreateScheduleHandler cshandler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("test schedule","2018-12-03T00:00:00",
				"2018-12-04T00:00:00",8,9,30);

		String csRequest = new Gson().toJson(csr);
		String csjsonRequest = new Gson().toJson(new PostRequest(csRequest));

		InputStream csinput = new ByteArrayInputStream(csjsonRequest.getBytes());
		OutputStream csoutput = new ByteArrayOutputStream();

		cshandler.handleRequest(csinput, csoutput, createContext("createSchedule"));

		//report activity
		ReportActivityHandler handler = new ReportActivityHandler();

		ReportActivityRequest cmr = new ReportActivityRequest(10);

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("reportActivity"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		ReportActivityResponse resp = new Gson().fromJson(post.body, ReportActivityResponse.class);
		System.out.println(resp);

		Assert.assertEquals("Report activity in last " + 10 + " hours :", resp.message);
		for (Schedule respSchedule : resp.schedules) {
			Assert.assertTrue(LocalDateTime.now().minusHours(10).isBefore(respSchedule.getCreatedDate()));
		}

		//clean this schedule out of database
		PostResponse cspost = new Gson().fromJson(csoutput.toString(), PostResponse.class);
		CreateScheduleResponse csresp = new Gson().fromJson(cspost.body, CreateScheduleResponse.class);
		Schedule respSchedule = csresp.schedule;
		String scheduleid = respSchedule.getScheduleID();
		String code = respSchedule.getCode();
		
		DeleteScheduleHandler dshandler = new DeleteScheduleHandler();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest(scheduleid,code);

		String dsRequest = new Gson().toJson(dsr);
		String dsjsonRequest = new Gson().toJson(new PostRequest(dsRequest));

		InputStream dsinput = new ByteArrayInputStream(dsjsonRequest.getBytes());
		OutputStream dsoutput = new ByteArrayOutputStream();

		dshandler.handleRequest(dsinput, dsoutput, createContext("deleteSchedule"));
	}
}
