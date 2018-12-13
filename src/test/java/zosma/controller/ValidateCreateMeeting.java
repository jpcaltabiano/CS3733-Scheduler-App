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

import zosma.model.Day;
import zosma.model.Schedule;
import zosma.model.Timeslot;

public class ValidateCreateMeeting {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCreateMeeting() throws IOException {
		//first create schedule
		CreateScheduleHandler cshandler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("test schedule","2018-12-03T00:00:00",
				"2018-12-04T00:00:00",8,9,60);

		String csRequest = new Gson().toJson(csr);
		String csjsonRequest = new Gson().toJson(new PostRequest(csRequest));

		InputStream csinput = new ByteArrayInputStream(csjsonRequest.getBytes());
		OutputStream csoutput = new ByteArrayOutputStream();

		cshandler.handleRequest(csinput, csoutput, createContext("createSchedule"));

		PostResponse cspost = new Gson().fromJson(csoutput.toString(), PostResponse.class);
		CreateScheduleResponse csresp = new Gson().fromJson(cspost.body, CreateScheduleResponse.class);
		Schedule respSchedule = csresp.schedule;

		//create meeting on first slot
		CreateMeetingHandler cmhandler = new CreateMeetingHandler();
		String scheduleid = respSchedule.getScheduleID();
		Day day = respSchedule.getDays().get(0);
		Timeslot slot = day.getSlot().get(0);
		String slotid = slot.getID();
		CreateMeetingRequest cmr = new CreateMeetingRequest(scheduleid,slotid,"test user");

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		cmhandler.handleRequest(input, output, createContext("createMeeting"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		CreateMeetingResponse resp = new Gson().fromJson(post.body, CreateMeetingResponse.class);
		System.out.println(resp);

		Assert.assertEquals("Successfully create meeting for " + "test user" 
				+ " in schedule :" + scheduleid  + ", in Timeslot :" + slotid, resp.message);
		Assert.assertEquals("test user",resp.meeting.getUser());

		//clean this schedule out of database
		DeleteScheduleHandler dshandler = new DeleteScheduleHandler();
		String ocode = respSchedule.getCode();

		DeleteScheduleRequest dsr = new DeleteScheduleRequest(scheduleid,ocode);

		String dsRequest = new Gson().toJson(dsr);
		String dsjsonRequest = new Gson().toJson(new PostRequest(dsRequest));

		InputStream dsinput = new ByteArrayInputStream(dsjsonRequest.getBytes());
		OutputStream dsoutput = new ByteArrayOutputStream();

		dshandler.handleRequest(dsinput, dsoutput, createContext("deleteSchedule"));
	}
}
