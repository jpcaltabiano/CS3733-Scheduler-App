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

public class ValidateCloseTimeSlot {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCloseTimeSlot() throws IOException {
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

		//close first slot
		CloseTimeSlotHandler cthandler = new CloseTimeSlotHandler();
		String scheduleid = respSchedule.getScheduleID();
		String slotid = respSchedule.getDays().get(0).getSlot().get(0).getID();
		String code = respSchedule.getCode();
		CloseTimeSlotRequest ctr = new CloseTimeSlotRequest(scheduleid,slotid,null,null,code);

		String ctRequest = new Gson().toJson(ctr);
		String ctjsonRequest = new Gson().toJson(new PostRequest(ctRequest));

		InputStream ctinput = new ByteArrayInputStream(ctjsonRequest.getBytes());
		OutputStream ctoutput = new ByteArrayOutputStream();

		cthandler.handleRequest(ctinput, ctoutput, createContext("closeTimeSlot"));

		PostResponse ctpost = new Gson().fromJson(ctoutput.toString(), PostResponse.class);
		CloseTimeSlotResponse ctresp = new Gson().fromJson(ctpost.body, CloseTimeSlotResponse.class);
		System.out.println(ctresp);

		Assert.assertEquals("Successfully close time slot in schedule :" + scheduleid, ctresp.message);
		Assert.assertFalse(ctresp.slot.get(0).getST());

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
