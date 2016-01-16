package de.predbo.vertx.content;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import de.predbo.vertx.Provider;

public class ContentProvider implements Provider {

	private static final Logger _logger = LoggerFactory.getLogger(ContentProvider.class);
	
	@Override
	public Router createSubRouter(Vertx vertx) {
		Router router = Router.router(vertx);
		router.route("/api/*").handler(context -> {
			context.fail(500); // api called should have completely been managed by api module
		});
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		router.route().failureHandler(this::handleFaults);
		return router;
	}
	
	
	
	
	private void handleFaults(RoutingContext failedRoutingContext) { 
		if (failedRoutingContext.failed()) {
			int statusCode = failedRoutingContext.statusCode();
			if (statusCode == 404) {
				_logger.info("file not found '" + failedRoutingContext.normalisedPath() + "'");
			}
		}
		failedRoutingContext.next();
	}

}
