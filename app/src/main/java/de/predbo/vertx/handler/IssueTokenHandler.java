package de.predbo.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;
import de.predbo.vertx.api.user.UserRegistry;

public interface IssueTokenHandler extends Handler<RoutingContext> {

  static IssueTokenHandler create(JWTAuth jwtAuthProvider, UserRegistry userRegistry) {
    return new IssueTokenHandlerImpl(jwtAuthProvider, userRegistry);
  }

}
