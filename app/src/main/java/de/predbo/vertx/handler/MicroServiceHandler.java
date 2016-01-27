package de.predbo.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class MicroServiceHandler implements Handler<RoutingContext> {

	private static final Logger _logger = LoggerFactory.getLogger(MicroServiceHandler.class);

	@Override
	public void handle(RoutingContext routingContext) {
		routingContext.vertx().eventBus().send("services.internal.logging", "ping", asyncResult -> {
			String response = "";
			if (asyncResult.succeeded()) {
				_logger.info("Response from MicroService: " + asyncResult.result().body().toString());
				response = "MicroService available";
			} else {
				response = "MicroService not available";
				routingContext.response().setStatusCode(404);
			}
			routingContext.response().end(response);
		});
	}
}
