package de.predbo.vertx.microservices.logging;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

	private static final Logger _logger = LoggerFactory.getLogger(MainVerticle.class);
	
	@Override
	public void start() {
		vertx.eventBus().consumer("services.internal.logging", message -> {
			_logger.info("Request from MicroService: " + message.body());
			message.reply("Pong");
		});
	}
}