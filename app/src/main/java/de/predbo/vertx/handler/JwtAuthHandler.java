package de.predbo.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

public interface JwtAuthHandler extends Handler<RoutingContext> {

  static JwtAuthHandler create(JWTAuth jwtAuth) {
    return new JwtAuthHandlerImpl(jwtAuth);
  }

}
