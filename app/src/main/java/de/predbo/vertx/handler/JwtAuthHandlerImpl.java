package de.predbo.vertx.handler;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;

import java.util.regex.Pattern;

public class JwtAuthHandlerImpl extends AuthHandlerImpl implements JwtAuthHandler {

	private static final Logger _logger = LoggerFactory.getLogger(JwtAuthHandlerImpl.class);
	private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

	public JwtAuthHandlerImpl(JWTAuth jwtAuth) {
		super(jwtAuth);
	}

	@Override
	public void handle(RoutingContext context) {
		User user = context.user();
		if (user != null) {
			// Already authenticated in, just authorise
			authorise(user, context);
		} else {
			final HttpServerRequest request = context.request();
			String token = null;

			token = getJwtTokenFromBasicAuth(request); 
			if (token == null) {
				token = getJwtTokenFromCookie(request);
				if (token == null) {
					context.response().putHeader("location", "/loginForced.html").setStatusCode(302).end();
					_logger.warn("No JWT Auth Token found, neither in Cookie nor in Authorization Header so redirect to login page");
					return;
				} 
			}
			
			JsonObject authInfo = new JsonObject().put("jwt", token);

			authProvider.authenticate(authInfo, res -> {
				if (res.succeeded()) {
					final User user2 = res.result();
					context.setUser(user2);
					authorise(user2, context);
				} else {
					_logger.warn("JWT decode failure");
					context.fail(401);
				}
			});
		}
	}

	private String getJwtTokenFromCookie(final HttpServerRequest request) {
		final String cookies = request.headers().get(HttpHeaders.COOKIE);
		if (cookies != null) {
			String[] parts = cookies.split(" ");
			for (String string : parts) {
				if (string.startsWith("auth_token=")) {
					_logger.info("found JWT Auth Token in Cookie");
					return string.substring(11);
				}
			}
		}
		return null;
	}

	private String getJwtTokenFromBasicAuth(final HttpServerRequest request) {
		final String authorization = request.headers().get(HttpHeaders.AUTHORIZATION);

		if (authorization != null) {
			String[] parts = authorization.split(" ");
			if (parts.length == 2) {
				final String scheme = parts[0], credentials = parts[1];

				if (BEARER.matcher(scheme).matches()) {
					_logger.info("found JWT Auth Token in Authorization Header");
					return credentials;
				}
			}
		}
		return null;
	}
}
