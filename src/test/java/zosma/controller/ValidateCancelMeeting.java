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

public class ValidateCancelMeeting {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCancelMeeting() throws IOException {
		//first create schedule
		CreateScheduleHandler cshandler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("test schedule","2018-12-03T00:00:00",
				"2018-12-04T00:00:00",8,9,30);

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
		String slotid = respSchedule.getDays().get(0).getSlot().get(0).getID();
		CreateMeetingRequest cmr = new CreateMeetingRequest(scheduleid,slotid,"test user");

		String cmRequest = new Gson().toJson(cmr);
		String cmjsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream cminput = new ByteArrayInputStream(cmjsonRequest.getBytes());
		OutputStream cmoutput = new ByteArrayOutputStream();

		cmhandler.handleRequest(cminput, cmoutput, createContext("createMeeting"));

		PostResponse cmpost = new Gson().fromJson(cmoutput.toString(), PostResponse.class);
		CreateMeetingResponse cmresp = new Gson().fromJson(cmpost.body, CreateMeetingResponse.class);

		//cancel meeting on same slot
		String code = cmresp.meeting.getPSC();
		CancelMeetingHandler cahandler = new CancelMeetingHandler();

		CancelMeetingRequest car = new CancelMeetingRequest(scheduleid,slotid,code);

		String caRequest = new Gson().toJson(car);
		String cajsonRequest = new Gson().toJson(new PostRequest(caRequest));

		InputStream cainput = new ByteArrayInputStream(cajsonRequest.getBytes());
		OutputStream caoutput = new ByteArrayOutputStream();

		cahandler.handleRequest(cainput, caoutput, createContext("cancelMeeting"));

		PostResponse capost = new Gson().fromJson(caoutput.toString(), PostResponse.class);
		CancelMeetingResponse caresp = new Gson().fromJson(capost.body, CancelMeetingResponse.class);
		System.out.println(caresp);

		Assert.assertEquals("Successfully cancel meeting in schedule :" + scheduleid 
				+ ", in Timeslot :" + slotid, caresp.message);

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
