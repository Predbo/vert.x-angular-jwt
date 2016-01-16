package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import de.predbo.vertx.api.user.UserApiProvider;
import de.predbo.vertx.content.ContentProvider;

public class MainVerticle extends AbstractVerticle {
	
	private static final Logger _logger = LoggerFactory.getLogger(MainVerticle.class);
	
	@Override
	public void start() {
		UserApiProvider userApiProvider = new UserApiProvider();
		ContentProvider contentProvider = new ContentProvider();
		Integer listenPort = config().getInteger("http.port", 8080);

		Router router = Router.router(vertx);
		router.mountSubRouter("/api/", userApiProvider.createSubRouter(vertx));
		_logger.info("UserApiProvider Router is configured!");
		router.mountSubRouter("/", contentProvider.createSubRouter(vertx));
		_logger.info("ContentProvider Router is configured!");
		
		vertx.createHttpServer().requestHandler(router::accept).listen(listenPort);
	}
}