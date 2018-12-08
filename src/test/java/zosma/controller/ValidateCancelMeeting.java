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

public class ValidateCancelMeeting {
	
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCancelMeeting() throws IOException {
		CancelMeetingHandler handler = new CancelMeetingHandler();

		// TODO: have valid attributes
		CancelMeetingRequest cmr = new CancelMeetingRequest("schedule-id","slot-id","code");

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("cancelMeeting"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		CancelMeetingResponse resp = new Gson().fromJson(post.body, CancelMeetingResponse.class);
		System.out.println(resp);

		// TODO: have valid attributes
		Assert.assertEquals("Successfully cancel meeting in schedule :" + "schedule-id" 
		+ ", in Timeslot :" + "slot-id", resp.message);
	}
}
