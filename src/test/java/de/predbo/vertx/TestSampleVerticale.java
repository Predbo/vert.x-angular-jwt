package de.predbo.vertx;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class TestSampleVerticale extends SampleVerticaleTestBase {

	@Test // not refactored
	public void assureThatUndefinedPathWillLedToNotFound404Response(TestContext context) {
		final Async async = context.async();

		_vertx.createHttpClient().getNow(_port, "localhost", "/asd", response -> {
			response.handler(body -> {
				String responsePayload = body.toString();
				System.out.println("Response was: " + responsePayload);
				context.assertTrue(responsePayload.contains("Not Found"));
				async.complete();
			});
		});
	}
	
	@Test 
	public void assureThatDefaultPathIsForwardedToIndexPage(TestContext context) {
		String response = sendHttpRequestAndWaitForResponse(context, "/");
		context.assertTrue(response.contains("Super Hero Website"));
	}

	@Test 
	public void assureThatLoginPageIsReturned(TestContext context) {
		String response = sendHttpRequestAndWaitForResponse(context, "/login.html");
		context.assertTrue(response.contains("Login now"));
	}




	private String sendHttpRequestAndWaitForResponse(TestContext context, String path) {
		final Async async = context.async();
		_vertx.createHttpClient().getNow(_port, "localhost", path, response -> {
			context.assertEquals(response.statusCode(), 200);
		    context.assertEquals(response.headers().get("content-type"), "text/html");
			response.handler(body -> {
				context.put("responseMessage", body.toString());
				System.out.println("Response was: " + body.toString());
				async.complete();
			});
		});
		async.awaitSuccess();
		return context.get("responseMessage");
	}
}
