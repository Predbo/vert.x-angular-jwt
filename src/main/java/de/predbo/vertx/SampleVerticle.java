package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import de.predbo.vertx.model.User;

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
		router.mountSubRouter("/api", createApiRouter());
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		router.route().failureHandler(this::handleFaults);
		return router;
	}
	
    private Router createApiRouter() {
        Router router = Router.router(vertx);
        
		router.get("/users").handler(this::getAllUsers);
		router.get("/users/reset").handler(this::resetUserRegistry);
		router.route("/users*").handler(BodyHandler.create());
		router.get("/users/:id").handler(this::getUser);
		router.post("/users").handler(this::addUser);
		router.delete("/users/:id").handler(this::deleteUser);
		router.put("/users/:id").handler(this::updateUser);
        

        return router;
    }
	
	private void getAllUsers(RoutingContext routingContext) {
		_logger.debug("get all Users");
		routingContext.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(_userRegistry.getUsers().values()));
	}
	
	private void resetUserRegistry(RoutingContext routingContext) {
		_logger.debug("reset User Registry");
		_userRegistry.createSampleRegistry();
		routingContext.response()
				.putHeader("content-type", "text/plain; charset=utf-8")
				.end("successfully reset User Registry");
	}
	
	private void getUser(RoutingContext routingContext) {
		String idString = routingContext.request().getParam("id");
		User user = _userRegistry.getUserByUnvalidatedId(idString);
		if (user != null) {
			routingContext.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(user));
		} else {
			_logger.warn(String.format("There is no user with id '%s'", idString));
			routingContext.response().setStatusCode(404).end(String.format("There is no user with id '%s'", idString));
		}
	}
	


	private void addUser(RoutingContext routingContext) {
		final User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
		_logger.debug("add new user");
		_userRegistry.registerUser(user);
		routingContext.response().setStatusCode(201)
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(user));
	}
	
	private void deleteUser(RoutingContext routingContext) {
		String idString = routingContext.request().getParam("id");
		User user = _userRegistry.getUserByUnvalidatedId(idString);
		if (user != null) {
			_logger.debug("delete user with id " + idString);
			_userRegistry.unregisterUser(user.getId());
			routingContext.response().setStatusCode(204).end();
		} else {
			_logger.warn(String.format("There is no user with id '%s'", idString));
			routingContext.response().setStatusCode(404).end(String.format("There is no user with id '%s'", idString));
		}
	}
	
	private void updateUser(RoutingContext routingContext) {
		String idString = routingContext.request().getParam("id");
		User updatedUser = Json.decodeValue(routingContext.getBodyAsString(), User.class);
		User registeredUser = _userRegistry.getUserByUnvalidatedId(idString);
		if (registeredUser != null && updatedUser != null) {
	    	_logger.debug("update user with id " + idString);
	    	registeredUser.setName(updatedUser.getName());
	    	registeredUser.setLastname(updatedUser.getLastname());
	        routingContext.response()
	            .putHeader("content-type", "application/json; charset=utf-8")
	            .end(Json.encodePrettily(registeredUser));
		} else {
			_logger.warn(String.format("There is no user with id '%s'", idString));
			routingContext.response().setStatusCode(404).end(String.format("There is no user with id '%s'", idString));
		}
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