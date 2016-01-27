package de.predbo.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface FaultHandler extends Handler<RoutingContext> {

  static FaultHandler create() {
    return new FaultHandlerImpl();
  }

}
