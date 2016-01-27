package de.predbo.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;
import de.predbo.vertx.api.user.UserRegistry;

public interface HandlerFactory extends Handler<RoutingContext> {

	static IssueTokenHandler createIssueTokenHandler(JWTAuth jwtAuthProvider, UserRegistry userRegistry) {
		return new IssueTokenHandler(jwtAuthProvider, userRegistry);
	}

	static WebsocketHandler createWebsocketHandler(Vertx vertx) {
		return new WebsocketHandler(vertx);
	}
	
	static MicroServiceHandler createMicroServiceHandler() {
		return new MicroServiceHandler();
	}
	
	static JwtAuthHandler createJwtAuthHandlerImpl(JWTAuth jwtAuth) {
		return new JwtAuthHandler(jwtAuth);
	}
	
	static FaultHandler createFaultHandlerImpl() {
		return new FaultHandler();
	}

}
