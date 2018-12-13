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
import zosma.model.Timeslot;

public class ValidateSearchOpenTimeSlot {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testSearchOpenTimeSlot() throws IOException {
		//first create schedule
		CreateScheduleHandler cshandler = new CreateScheduleHandler();

		CreateScheduleRequest csr = new CreateScheduleRequest("test schedule","2018-12-03T00:00:00",
				"2018-12-07T00:00:00",8,9,30);

		String csRequest = new Gson().toJson(csr);
		String csjsonRequest = new Gson().toJson(new PostRequest(csRequest));

		InputStream csinput = new ByteArrayInputStream(csjsonRequest.getBytes());
		OutputStream csoutput = new ByteArrayOutputStream();

		cshandler.handleRequest(csinput, csoutput, createContext("createSchedule"));

		PostResponse cspost = new Gson().fromJson(csoutput.toString(), PostResponse.class);
		CreateScheduleResponse csresp = new Gson().fromJson(cspost.body, CreateScheduleResponse.class);
		Schedule respSchedule = csresp.schedule;

		//search open time slot from this schedule
		SearchOpenTimeSlotHandler handler = new SearchOpenTimeSlotHandler();
		String scheduleid = respSchedule.getScheduleID();
		SearchOpenTimeSlotRequest cmr = new SearchOpenTimeSlotRequest(scheduleid,12,2018,1,3);

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("searchOpenTimeSlot"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		SearchOpenTimeSlotResponse resp = new Gson().fromJson(post.body, SearchOpenTimeSlotResponse.class);
		System.out.println(resp);

		Assert.assertEquals("List of open time slots in schedule :" + scheduleid, resp.message);
		for (Timeslot respTimeslot : resp.slots) {
			Assert.assertEquals(12, respTimeslot.getTime().getMonth().getValue());
			Assert.assertEquals(2018, respTimeslot.getTime().getYear());
			Assert.assertEquals(1, respTimeslot.getTime().getDayOfWeek().getValue());
			Assert.assertEquals(3, respTimeslot.getTime().getDayOfMonth());
		}

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
