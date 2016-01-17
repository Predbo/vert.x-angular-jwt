package de.predbo.vertx;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import de.predbo.vertx.testutils.JwtTokenHelper;

@RunWith(VertxUnitRunner.class)
public class TestIssueTokenHandler extends MainVerticaleTestBase {
	
	@Test 
	public void assureThatLoginFailsWithoutCredentials(TestContext context) throws UnirestException {
		HttpResponse<String> response = issueToken("","");
		
		context.assertNull(response.getHeaders().get("set-cookie"));
		context.assertEquals(200, response.getStatus());
		context.assertEquals("Login failed", response.getBody());
	}
	
	@Test 
	public void assureThatLoginFailsWithOnlyCorrectUsername(TestContext context) throws UnirestException {
		HttpResponse<String> response = issueToken("Hugo","");
		
		context.assertNull(response.getHeaders().get("set-cookie"));
		context.assertEquals(200, response.getStatus());
		context.assertEquals("Login failed", response.getBody());
	}
	
	@Test 
	public void assureThatLoginFailsWithOnlyCorrectPassword(TestContext context) throws UnirestException {
		HttpResponse<String> response = issueToken("","geheim");
		
		context.assertNull(response.getHeaders().get("set-cookie"));
		context.assertEquals(200, response.getStatus());
		context.assertEquals("Login failed", response.getBody());
	}
	
	@Test 
	public void assureThatLoginWorksWithCorrectUsernameAndPassword(TestContext context) throws UnirestException {
		HttpResponse<String> response = issueToken("Berta","geheim");
		assertSuccessfullLoginAndCorrectToken(context, response, "Berta");
	}

	
	
	
	
	private HttpResponse<String> issueToken(String username, String password) throws UnirestException {
		return Unirest.get(BASE_URL + "issueToken?username=" + username + "&password=" + password).asString();
	}
	
	private void assertSuccessfullLoginAndCorrectToken(TestContext context, HttpResponse<String> response, String username) {
		context.assertEquals(200, response.getStatus());
		context.assertEquals("Login successfull", response.getBody());
		String cookieHeader = response.getHeaders().get("set-cookie").get(0);
		String tokenString = JwtTokenHelper.decodeTokenToStringFromCookieHeader(cookieHeader);
		context.assertNotNull(tokenString);
//		System.out.println(cookieHeader);
		context.assertTrue(tokenString.contains("\"user\":\"" + username + "\""));
		context.assertTrue(tokenString.contains("\"sub\":\"predbo\""));
		context.assertTrue(cookieHeader.contains("HTTPOnly"));
		context.assertTrue(cookieHeader.contains("Path=/"));
	}
}
