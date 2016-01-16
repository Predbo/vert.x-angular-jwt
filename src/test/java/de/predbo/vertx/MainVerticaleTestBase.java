package de.predbo.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class MainVerticaleTestBase {
	
	protected static final String BASE_URL = "http://localhost:8080/";
	
	protected static Vertx _vertx;
	protected static int _port = 8080;

	@BeforeClass
	public static void deploySampleVerticale(TestContext context) {
		_vertx = Vertx.vertx();
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", _port));
		_vertx.deployVerticle(MainVerticle.class.getName(), options, context.asyncAssertSuccess());
	}
	
	@AfterClass
	public static void stopSampleVerticale(TestContext context) {
		_vertx.close(context.asyncAssertSuccess());
	}
	
}
