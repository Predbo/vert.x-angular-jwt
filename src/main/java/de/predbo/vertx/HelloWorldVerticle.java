package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class HelloWorldVerticle extends AbstractVerticle {
	
	private static final Logger logger = LoggerFactory.getLogger(HelloWorldVerticle.class);

	@Override
	public void start() {

		createHttpServerOldSchool();

		createHttpServerJava8Style();
		
		createHttpServerUsingVertxWeb();
		
		createHttpServerUsingStaticHandler();
	}

	
	
	private void createHttpServerOldSchool() {
		HttpServer server = vertx.createHttpServer();

		server.requestHandler(new Handler<HttpServerRequest>() {
			public void handle(HttpServerRequest request) {
				logger.info("A request has arrived on port 8080 (old school)!");
				request.response().end("Hello World!");
			}
		});

		server.listen(8080, "localhost");
	}
	
	private void createHttpServerJava8Style() {
		vertx.createHttpServer().requestHandler(request -> {
			logger.info("A request has arrived on port 8081 (Java 8 style)!");
				request.response().end("Hello World!");
		}).listen(8081);
	}

	private void createHttpServerUsingVertxWeb() {
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);
		
		router.route().handler(routingContext -> {
			logger.info("A request has arrived on port 8082 (vertx-web)!");

			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/plain");
			response.end("Hello World from Vert.x-Web!");
		});

		server.requestHandler(router::accept).listen(8082);
	}
	
	private void createHttpServerUsingStaticHandler() {
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);
		
		router.route("/*").handler(StaticHandler.create());
		
		router.route().handler(routingContext -> {
			logger.info("A request has arrived on port 8083 (vertx-web with static content)!");
		});
		
		server.requestHandler(router::accept).listen(8083);
	}

}