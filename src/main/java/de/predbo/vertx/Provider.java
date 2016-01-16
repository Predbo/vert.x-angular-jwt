package de.predbo.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface Provider {
	
	public Router createSubRouter(Vertx vertx);

}
