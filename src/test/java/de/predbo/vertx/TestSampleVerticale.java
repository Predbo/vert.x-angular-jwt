package de.predbo.vertx;

import io.vertx.core.MultiMap;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class TestSampleVerticale extends SampleVerticaleTestBase {

	private String _responseMessage;
	private Integer _statusCode;
	private MultiMap _headers; 
	
	@After
	public void cleanupTestResources() {
		_responseMessage = null;
		_statusCode = null;
		_headers = null;
	}
	
	@Test
	public void assureThatUndefinedPathWillLedToNotFound404Response(TestContext context) {
		sendHttpRequestAndWaitForResponse(context, "/asd");
		
		context.assertTrue(_responseMessage.contains("Not Found"));
		context.assertEquals(404, _statusCode);
	}
	
	@Test 
	public void assureThatDefaultPathIsForwardedToIndexPage(TestContext context) {
		sendHttpRequestAndWaitForResponse(context, "/");
		
		context.assertTrue(_responseMessage.contains("Super Hero Website"));
		context.assertEquals("text/html", _headers.get("content-type"));
		context.assertEquals(200, _statusCode);
	}


	@Test 
	public void assureThatLoginPageIsReturned(TestContext context) {
		sendHttpRequestAndWaitForResponse(context, "/login.html");
		
		context.assertTrue(_responseMessage.contains("Login now"));
		context.assertEquals("text/html", _headers.get("content-type"));
		context.assertEquals(200, _statusCode);
	}
	
	@Test 
	public void assureThatApiUsersWorks(TestContext context) {
		sendHttpRequestAndWaitForResponse(context, "/api/users");
		
		context.assertEquals(200, _statusCode);
		context.assertEquals("application/json; charset=utf-8", _headers.get("content-type"));
		context.assertTrue(_responseMessage.contains("\"lastName\" : \"Schmidt\""));
	}




	private void sendHttpRequestAndWaitForResponse(TestContext context, String path) {
		final Async async = context.async();
		_vertx.createHttpClient().getNow(_port, "localhost", path, response -> {
			_statusCode = response.statusCode();
			_headers = response.headers();
			response.handler(body -> {
				_responseMessage = body.toString();
				async.complete();
			});
		});
		async.awaitSuccess();
	}
}
