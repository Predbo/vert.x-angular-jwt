package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

public class HelloWorldVerticle extends AbstractVerticle {

	@Override
	public void start() {

		// Create an HTTP server which simply returns "Hello World!" to each request.
		vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);

		createHttpServerWithoutJava8Lamdas();
	}

	private void createHttpServerWithoutJava8Lamdas() {
		HttpServer server = vertx.createHttpServer();

		server.requestHandler(new Handler<HttpServerRequest>() {
			public void handle(HttpServerRequest request) {
				System.out.println("A request has arrived on the server!");
				request.response().end("Hello World!");
			}
		});

		server.listen(8081, "localhost");
	}
}