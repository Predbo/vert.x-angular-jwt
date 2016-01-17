package de.predbo.vertx.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;
import de.predbo.vertx.api.user.User;
import de.predbo.vertx.api.user.UserRegistry;

public class IssueTokenHandlerImpl implements IssueTokenHandler {

	private static final Logger _logger = LoggerFactory.getLogger(IssueTokenHandlerImpl.class);
	
	private JWTAuth _jwtAuthProvider;
	private UserRegistry _userRegistry;

	public IssueTokenHandlerImpl(JWTAuth jwtAuthProvider, UserRegistry userRegistry) {
		_jwtAuthProvider = jwtAuthProvider;
		_userRegistry = userRegistry;
	}

	@Override
	public void handle(RoutingContext routingContext) {
		_logger.debug("uri: " + routingContext.request().uri());
		String username = routingContext.request().getParam("username");
		String password = routingContext.request().getParam("password");

		User user = _userRegistry.getUserByName(username);
		if (user != null && user.getPassword().equals(password)) {
			JsonObject claim = new JsonObject().put("sub", "predbo")
												.put("user", user.getName())
												.put("lastname", user.getLastname());
			String jwtToken = _jwtAuthProvider.generateToken(claim, new JWTOptions());
			_logger.debug("Login successfull, token is: " + jwtToken);
			routingContext.addCookie(Cookie.cookie("auth_token", jwtToken).setPath("/").setHttpOnly(true));
			// routingContext.response().putHeader("location", "/protected/index.html").setStatusCode(302).end();
			// routingContext.response().end(jwtToken);
			routingContext.response().end("Login successfull");
		} else {
			_logger.debug("Login failed");
			routingContext.response().end("Login failed");
		}

	}

}
