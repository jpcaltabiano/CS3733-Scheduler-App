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

public class ValidateCreateMeeting {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testCreateMeeting() throws IOException {
		CreateMeetingHandler handler = new CreateMeetingHandler();

		CreateMeetingRequest cmr = new CreateMeetingRequest("schedule-id","slot-id","test user");

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("createMeeting"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		CreateMeetingResponse resp = new Gson().fromJson(post.body, CreateMeetingResponse.class);
		System.out.println(resp);

		// TODO: have valid attributes
		Assert.assertEquals("Successfully create meeting for " + "test user" 
				+ " in schedule :" + "schedule-id"  + ", in Timeslot :" + "slot-id", resp.message);
		Assert.assertEquals("test user",resp.meeting.getUser());
	}
}
