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
			Assert.assertEquals(LocalDateTime.now().minusHours(10).getHour(), respSchedule.getCreatedDate().getHour());
		}
	}
}
