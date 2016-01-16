package de.predbo.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;

import org.junit.After;
import org.junit.Before;

public class SampleVerticaleTestBase {
	
	protected static final String BASE_URL = "http://localhost:8080/";
	
	protected Vertx _vertx;
	protected int _port = 8080;

	@Before
	public void deploySampleVerticale(TestContext context) {
		_vertx = Vertx.vertx();
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", _port));
		_vertx.deployVerticle(SampleVerticle.class.getName(), options, context.asyncAssertSuccess());
	}
	
	@After
	public void stopSampleVerticale(TestContext context) {
		_vertx.close(context.asyncAssertSuccess());
	}
	
}
