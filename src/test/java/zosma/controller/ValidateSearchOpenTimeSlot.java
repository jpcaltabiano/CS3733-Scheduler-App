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

import zosma.model.Timeslot;

public class ValidateSearchOpenTimeSlot {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testSearchOpenTimeSlot() throws IOException {
		SearchOpenTimeSlotHandler handler = new SearchOpenTimeSlotHandler();

		// TODO: have valid attributes
		SearchOpenTimeSlotRequest cmr = new SearchOpenTimeSlotRequest("schedule-id",12,2018,1,10);

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("searchOpenTimeSlot"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		SearchOpenTimeSlotResponse resp = new Gson().fromJson(post.body, SearchOpenTimeSlotResponse.class);
		System.out.println(resp);
		
		// TODO: have valid attributes
		Assert.assertEquals("List of open time slots in schedule :" + "schedule-id", resp.message);
		for (Timeslot respTimeslot : resp.slots) {
		Assert.assertEquals(12, respTimeslot.getTime().getMonth().getValue());
		Assert.assertEquals(2018, respTimeslot.getTime().getYear());
		Assert.assertEquals(1, respTimeslot.getTime().getDayOfWeek().getValue());
		Assert.assertEquals(10, respTimeslot.getTime().getDayOfMonth());
		}
	}
}
