package de.predbo.vertx;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RunWith(VertxUnitRunner.class)
public class TestStaticResources extends SampleVerticaleTestBase {

	
	@Test
	public void assureThatUndefinedPathWillLedToNotFound404Response(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "/asd").asString();
		
		context.assertEquals(404, jsonResponse.getStatus());
		context.assertTrue(jsonResponse.getBody().contains("Not Found"));
	}
	
	@Test 
	public void assureThatDefaultPathIsForwardedToIndexPage(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL).asString();
		
		assertStatus200WithHtmlContent(context, jsonResponse);
		context.assertTrue(jsonResponse.getBody().contains("Super Hero Website"));
	}


	@Test 
	public void assureThatLoginPageIsReturned(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "login.html").asString();
		
		assertStatus200WithHtmlContent(context, jsonResponse);
		context.assertTrue(jsonResponse.getBody().contains("to access proteced sites"));
	}

	
	private void assertStatus200WithHtmlContent(TestContext context, HttpResponse<String> jsonResponse) {
		context.assertEquals(200, jsonResponse.getStatus());
		context.assertEquals("text/html", jsonResponse.getHeaders().get("content-type").get(0));
	}

}
