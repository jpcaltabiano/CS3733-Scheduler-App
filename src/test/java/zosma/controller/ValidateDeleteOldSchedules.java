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

public class ValidateDeleteOldSchedules {

	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testDeleteOldSchedules() throws IOException {
		DeleteOldSchedulesHandler handler = new DeleteOldSchedulesHandler();

		DeleteOldSchedulesRequest cmr = new DeleteOldSchedulesRequest(10);

		String cmRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(cmRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, createContext("deleteOldSchedules"));

		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
		DeleteOldSchedulesResponse resp = new Gson().fromJson(post.body, DeleteOldSchedulesResponse.class);
		System.out.println(resp);

		Assert.assertEquals("Successfully delete schedules older than" + 10 + "days", resp.message);
	}
}
