package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class SampleVerticle extends AbstractVerticle {
	
	private static final Logger _logger = LoggerFactory.getLogger(SampleVerticle.class);
	
	private final UserRegistry _userRegistry = new UserRegistry();
	
	@Override
	public void start() {
		Integer listenPort = config().getInteger("http.port", 8080);
		_userRegistry.createSampleRegistry();
		vertx.createHttpServer().requestHandler(createRouter()::accept).listen(listenPort);
	}

	private Router createRouter() {
		Router router = Router.router(vertx);
        router.get("/api/users").handler(this::getAllUsers);
//      router.get("/api/users").handler(routing -> {
//          getAllUsers(routing);
//      });
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		router.route().failureHandler(this::handleFaults);
		return router;
	}
	
	private void getAllUsers(RoutingContext routingContext) {
		routingContext.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(_userRegistry.getUsers().values()));
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