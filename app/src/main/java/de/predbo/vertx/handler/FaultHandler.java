package de.predbo.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class FaultHandler implements Handler<RoutingContext> {

	private static final Logger _logger = LoggerFactory.getLogger(FaultHandler.class);
	
	@Override
	public void handle(RoutingContext failedRoutingContext) {
		if (failedRoutingContext.failed()) {
			int statusCode = failedRoutingContext.statusCode();
			switch (statusCode) {
			case 404:
				_logger.info("file not found '"
						+ failedRoutingContext.normalisedPath() + "'");
				break;
			}
		}
		failedRoutingContext.next();
	}

}
