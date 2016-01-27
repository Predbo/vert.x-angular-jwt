package de.predbo.vertx.microservices;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class LoggingService extends AbstractVerticle {

	private static final Logger _logger = LoggerFactory.getLogger(LoggingService.class);
	
	@Override
	public void start() {
		vertx.eventBus().consumer("services.internal.logging", message -> {
			simulateComplexCalculation();
			_logger.info("[" + Thread.currentThread().getName() + "] " + message.body());
		});

	}

	
	
	
	private void simulateComplexCalculation() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}