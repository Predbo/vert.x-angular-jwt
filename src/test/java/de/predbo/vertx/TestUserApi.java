package de.predbo.vertx;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
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
	public void assureThatGetUser(TestContext context) throws UnirestException {
		assertNoUserById(context, 0);
	}


	
	@Test 
	public void assureThatAddUserWorks(TestContext context) throws UnirestException {
		HttpResponse<User> userResponse = addUser("Günther", "Fischer");
		
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
		HttpResponse<User> userResponse = updateUser(1, "Günther", "Fischer");
		
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
		addUser("Frank", "Werwolf");
		assertUserById(context, 4, "Frank", "Werwolf");
	}



	
	private HttpResponse<User[]> getAllUsers() throws UnirestException {
		return Unirest.get(BASE_URL + "api/users").asObject(User[].class);
	}
	
	private HttpResponse<User> getUser(int id) throws UnirestException {
		return Unirest.get(BASE_URL + "api/users/" + id).asObject(User.class);
	}
	
	private HttpResponse<User> addUser(String name, String lastname) throws UnirestException {
		User user = new User(name, lastname);
		return Unirest.post(BASE_URL + "api/users").body(user).asObject(User.class);
	}
	
	private HttpResponse<User> updateUser(int id, String name, String lastname) throws UnirestException {
		User user = new User(name, lastname);
		return Unirest.put(BASE_URL + "api/users/" + id).body(user).asObject(User.class);
	}
	
	private HttpResponse<String> deleteUser(int id) throws UnirestException {
		return Unirest.delete(BASE_URL + "api/users/" + id).asString();
	}
	
	private HttpResponse<String> reset() throws UnirestException {
		return Unirest.get(BASE_URL + "api/users/reset").asString();
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
		context.assertEquals("There is no user with id '" + id + "'", response.getBody());
	}
	
	
	
	
	@Before
	public void initJacksonObjectMapperForUniRest() {
		Unirest.setObjectMapper(new ObjectMapper() {
		    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

		    public <T> T readValue(String value, Class<T> valueType) {
		        try {
		            return jacksonObjectMapper.readValue(value, valueType);
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
		    }

		    public String writeValue(Object value) {
		        try {
		            return jacksonObjectMapper.writeValueAsString(value);
		        } catch (JsonProcessingException e) {
		            throw new RuntimeException(e);
		        }
		    }
		});
	}
}
