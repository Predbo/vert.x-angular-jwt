package de.predbo.vertx;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RunWith(VertxUnitRunner.class)
public class TestStaticResourceHandler extends MainVerticaleTestBase {

	
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

	@Test 
	public void assureThatNonAuthenticatedAccessToUsermanagerIsRedirectedToLoginForcedPage(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "protected/usermanager.html").asString();
		
		assertStatus200WithHtmlContent(context, jsonResponse);
		context.assertTrue(jsonResponse.getBody().contains("to access protected area you have to login first"));
	}
	
	@Test 
	public void assureThatAuthenticatedAccessViaCookieToUsermanagerWorks(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "protected/usermanager.html").header("cookie", _validJwtTokenAsCookie).asString();
		
		assertStatus200WithHtmlContent(context, jsonResponse);
		context.assertTrue(jsonResponse.getBody().contains("<h2>On this protected site you can manage all users</h2>"));
	}
	
	@Test 
	public void assureThatNonAuthenticatedAccessToProtectedIndexIsRedirectedToLoginForcedPage(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "protected/index.html").asString();
		
		assertStatus200WithHtmlContent(context, jsonResponse);
		context.assertTrue(jsonResponse.getBody().contains("to access protected area you have to login first"));
	}
	
	@Test 
	public void assureThatAuthenticatedAccessViaCookieToProtectedIndexWorks(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "protected/index.html").header("Cookie", _validJwtTokenAsCookie).asString();
		
		assertStatus200WithHtmlContent(context, jsonResponse);
		context.assertTrue(jsonResponse.getBody().contains("you successfully have entered protected area"));
	}
	
	@Test
	public void assureSpecialLoggingIsDoneAsynchonous(TestContext context) throws UnirestException {
		for (int i = 1; i <=10; i++) {
			HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "specialLogging" + i).asString();
			System.out.println(i + " Response was: " + jsonResponse.getBody());
			context.assertTrue(jsonResponse.getBody().contains("direct http response, while logging is still ongoing"));
		}
		System.out.println("All Testrequests successfully completed, waiting for special logging output...");
	}
	
	@Test 
	public void assureThatAuthenticatedAccessViaBasicAuthToProtectedIndexWorks(TestContext context) throws UnirestException {
		HttpResponse<String> jsonResponse = Unirest.get(BASE_URL + "protected/index.html").header("Authorization", _validJwtTokenAsAuthHeader).asString();
		
		assertStatus200WithHtmlContent(context, jsonResponse);
		context.assertTrue(jsonResponse.getBody().contains("you successfully have entered protected area"));
	}

	
	
	
	
	private void assertStatus200WithHtmlContent(TestContext context, HttpResponse<String> jsonResponse) {
		context.assertEquals(200, jsonResponse.getStatus());
		context.assertEquals("text/html", jsonResponse.getHeaders().get("content-type").get(0));
	}

}
