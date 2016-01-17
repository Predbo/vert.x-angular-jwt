package de.predbo.vertx.handler;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class FaultHandlerImpl implements FaultHandler {

	private static final Logger _logger = LoggerFactory.getLogger(FaultHandlerImpl.class);
	
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
