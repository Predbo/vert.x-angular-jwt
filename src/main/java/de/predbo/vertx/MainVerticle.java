package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.StaticHandler;
import de.predbo.vertx.api.user.User;
import de.predbo.vertx.api.user.UserApiProvider;
import de.predbo.vertx.api.user.UserRegistry;
import de.predbo.vertx.handler.JwtAuthHandler;

public class MainVerticle extends AbstractVerticle {
	
	private static final Logger _logger = LoggerFactory.getLogger(MainVerticle.class);
	
	private final UserRegistry _userRegistry = new UserRegistry();
	
	private JWTAuth jwtAuthProvider;
	
	@Override
	public void start() {
		UserApiProvider userApiProvider = new UserApiProvider(_userRegistry);
		Integer listenPort = config().getInteger("http.port", 8080);
		
		JsonObject authConfig = new JsonObject().put(
				"keyStore",
				new JsonObject().put("type", "jceks")
						.put("path", "keystore.jceks")
						.put("password", "secret"));

		jwtAuthProvider = JWTAuth.create(vertx, authConfig);
		
		Router router = Router.router(vertx);
		router.route().handler(CookieHandler.create());

		router.get("/issueToken").handler(this::issueNewJwtTokenAsCookie);

		router.route("/protected/*").handler(JwtAuthHandler.create(jwtAuthProvider));

		router.mountSubRouter("/protected/api/", userApiProvider.createSubRouter(vertx));
		
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		
		router.route().failureHandler(this::handleFaults);
		
		vertx.createHttpServer().requestHandler(router::accept).listen(listenPort);
	}
	
	
	private void issueNewJwtTokenAsCookie(RoutingContext routingContext) {
		_logger.info("uri: " + routingContext.request().uri());
		String username = routingContext.request().getParam("username");
		String password = routingContext.request().getParam("password");
		
		User user = _userRegistry.getUserByName(username);
		if (user != null && user.getPassword().equals(password)) {
			String jwtToken = jwtAuthProvider.generateToken(new JsonObject().put("sub", "paulo"), new JWTOptions());
			routingContext.addCookie(Cookie.cookie("auth_token", jwtToken).setPath("/"));
//			routingContext.response().putHeader("location", "/protected/index.html").setStatusCode(302).end();
//			routingContext.response().end(jwtToken);
			routingContext.response().end("Login successfull!");
		} else {
			routingContext.response().end("Login failed");
		}
		
		
		
	}
	
	private void handleFaults(RoutingContext failedRoutingContext) { 
		if (failedRoutingContext.failed()) {
			int statusCode = failedRoutingContext.statusCode();
			switch (statusCode) {
			case 404:
				_logger.info("file not found '" + failedRoutingContext.normalisedPath() + "'");
				break;
			}
		}
		failedRoutingContext.next();
	}
}