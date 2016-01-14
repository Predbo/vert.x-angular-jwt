package de.predbo.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;

import org.junit.After;
import org.junit.Before;

public class SampleVerticaleTestBase {
	
	protected Vertx _vertx;

	@Before
	public void deploySampleVerticale(TestContext context) {
		_vertx = Vertx.vertx();
		_vertx.deployVerticle(SampleVerticle.class.getName(),
				context.asyncAssertSuccess());
	}

	@After
	public void stopSampleVerticale(TestContext context) {
		_vertx.close(context.asyncAssertSuccess());
	}

}
