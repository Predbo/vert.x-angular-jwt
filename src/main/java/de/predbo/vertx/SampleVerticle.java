package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class SampleVerticle extends AbstractVerticle {
	
	private static final Logger logger = LoggerFactory.getLogger(SampleVerticle.class);
	
	@Override
	public void start() {
		Integer listenPort = config().getInteger("http.port", 8080);
		vertx.createHttpServer().requestHandler(createRouter()::accept).listen(listenPort);
	}

	private Router createRouter() {
		Router router = Router.router(vertx);
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		router.route().failureHandler(this::handleFaults);
		return router;
	}
	
	private void handleFaults(RoutingContext failedRoutingContext) { 
		if (failedRoutingContext.failed()) {
			int statusCode = failedRoutingContext.statusCode();
			if (statusCode == 404) {
				logger.info("file not found '" + failedRoutingContext.normalisedPath() + "'");
			}
		}
		failedRoutingContext.next();
	}

}