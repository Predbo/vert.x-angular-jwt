package de.predbo.vertx.microservices.logging;

import static org.junit.Assert.fail;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

	protected static Vertx _vertx;

	@BeforeClass
	public static void deployMainVerticale(TestContext context) {
		System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
		_vertx = Vertx.vertx();
		_vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions(), context.asyncAssertSuccess());
	}
	
	@AfterClass
	public static void stopSampleVerticale(TestContext context) {
		_vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void assureThatLogMessageIsReceivedFromEventBus(TestContext context) {
		_vertx.eventBus().send("services.internal.logging", "Test Message From EventBus");
		// TODO assert correct logging
	}

}
