package de.predbo.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

public class MainVerticaleTestBase {
	
	protected static final String BASE_URL = "http://localhost:8080/";
	
	protected static Vertx _vertx;
	protected static int _port = 8080;
	protected static String _validJwtTokenAsCookie = "asd=wer; httpOnly; auth_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXVsbyIsImlhdCI6MTQ1Mjk5NjYyNH0=.tX5mXLIghs1-v24o6uA1fZnqODWAU1ZvXyQFAXchXwg=";
	protected static String _validJwtTokenAsAuthHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXVsbyIsImlhdCI6MTQ1Mjk5NjYyNH0=.tX5mXLIghs1-v24o6uA1fZnqODWAU1ZvXyQFAXchXwg=";

	@BeforeClass
	public static void deployMainVerticale(TestContext context) {
		System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
		_vertx = Vertx.vertx();
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", _port));
		_vertx.deployVerticle(MainVerticle.class.getName(), options, context.asyncAssertSuccess());
	}
	
	@BeforeClass
	public static void initJacksonObjectMapperForUniRest() {
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
	
	
	
	@AfterClass
	public static void stopSampleVerticale(TestContext context) {
		_vertx.close(context.asyncAssertSuccess());
	}
	
}
