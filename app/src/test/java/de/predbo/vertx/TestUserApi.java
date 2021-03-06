package de.predbo.vertx;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import de.predbo.vertx.api.user.User;

@RunWith(VertxUnitRunner.class)
public class TestUserApi extends MainVerticaleTestBase {
	
	@Before
	public void resetUserRegistry() throws UnirestException {
		reset();
		// 1, "Hugo", "Müller"
		// 2, "Berta", "Schmidt"
		// 3, "Max", "Kruse"
	}
	
	@Test 
	public void assureThatGetAllUsersWorks(TestContext context) throws UnirestException {
		HttpResponse<User[]> jsonResponse = getAllUsers();
		User user1 = jsonResponse.getBody()[0];
		context.assertEquals(200, jsonResponse.getStatus())
			   .assertEquals("application/json; charset=utf-8", jsonResponse.getHeaders().get("content-type").get(0))
			   .assertEquals(1 ,user1.getId())
			   .assertEquals("Hugo" ,user1.getName())
			   .assertEquals("Müller" ,user1.getLastname());
	}

	@Test 
	public void assureThatGetUserWorks(TestContext context) throws UnirestException {
		assertUserById(context, 1, "Hugo", "Müller");
	}
	
	@Test 
	public void assureThatGetUserWithNonExistingIdLetsTo404(TestContext context) throws UnirestException {
		assertNoUserById(context, 0);
	}


	
	@Test 
	public void assureThatAddUserWorks(TestContext context) throws UnirestException {
		HttpResponse<User> userResponse = addUser("Günther", "Fischer", "geheim");
		
		User user = userResponse.getBody();
		context.assertEquals(201, userResponse.getStatus())
			   .assertEquals("application/json; charset=utf-8", userResponse.getHeaders().get("content-type").get(0))
			   .assertEquals("Günther" ,user.getName())
			   .assertEquals("Fischer" ,user.getLastname());
	}

	@Test 
	public void assureThatDeleteUserWorks(TestContext context) throws UnirestException {
		HttpResponse<String> response = deleteUser(1);
		
		context.assertEquals(204, response.getStatus())
			   .assertNull(response.getHeaders().get("content-type"))
			   .assertNull(response.getBody());
		assertNoUserById(context, 1);
	}
	
	@Test 
	public void assureThatUpdateUserWorks(TestContext context) throws UnirestException {
		HttpResponse<User> userResponse = updateUser(1, "Günther", "Fischer", "geheim");
		
		User user = userResponse.getBody();
		context.assertEquals(200, userResponse.getStatus())
			   .assertEquals("application/json; charset=utf-8", userResponse.getHeaders().get("content-type").get(0))
			   .assertEquals(1 ,user.getId())
			   .assertEquals("Günther" ,user.getName())
			   .assertEquals("Fischer" ,user.getLastname());
	}
	
	
	@Test 
	public void assureThatDeleteAndAddCreatesNewUserId(TestContext context) throws UnirestException {
		deleteUser(2);
		assertNoUserById(context, 2);
		addUser("Frank", "Werwolf", "geheim");
		assertUserById(context, 4, "Frank", "Werwolf");
	}



	
	private HttpResponse<User[]> getAllUsers() throws UnirestException {
		return Unirest.get(BASE_URL + "protected/api/users").header("cookie", _validJwtTokenAsCookie).asObject(User[].class);
	}
	
	private HttpResponse<User> getUser(int id) throws UnirestException {
		return Unirest.get(BASE_URL + "protected/api/users/" + id).header("Cookie", _validJwtTokenAsCookie).asObject(User.class);
	}
	
	private HttpResponse<User> addUser(String name, String lastname, String password) throws UnirestException {
		User user = new User(name, lastname, password);
		return Unirest.post(BASE_URL + "protected/api/users").header("Cookie", _validJwtTokenAsCookie).body(user).asObject(User.class);
	}
	
	private HttpResponse<User> updateUser(int id, String name, String lastname, String password) throws UnirestException {
		User user = new User(name, lastname, password);
		return Unirest.put(BASE_URL + "protected/api/users/" + id).header("Cookie", _validJwtTokenAsCookie).body(user).asObject(User.class);
	}
	
	private HttpResponse<String> deleteUser(int id) throws UnirestException {
		return Unirest.delete(BASE_URL + "protected/api/users/" + id).header("Cookie", _validJwtTokenAsCookie).asString();
	}
	
	private HttpResponse<String> reset() throws UnirestException {
		return Unirest.get(BASE_URL + "protected/api/users/reset").header("Cookie", _validJwtTokenAsCookie).asString();
	}
	
	
	private void assertUserById(TestContext context, int id, String name, String lastname) throws UnirestException {
		HttpResponse<User> userResponse = getUser(id);
		
		User user = userResponse.getBody();
		context.assertEquals(200, userResponse.getStatus())
			   .assertEquals("application/json; charset=utf-8", userResponse.getHeaders().get("content-type").get(0))
			   .assertEquals(id ,user.getId())
			   .assertEquals(name ,user.getName())
			   .assertEquals(lastname ,user.getLastname());
		
	}
	private void assertNoUserById(TestContext context, int id) throws UnirestException {
		HttpResponse<String> response = Unirest.get(BASE_URL + "api/users/" + id).asString();
		
		context.assertEquals(404, response.getStatus());
		context.assertEquals("Not Found", response.getBody());
	}
	
}
