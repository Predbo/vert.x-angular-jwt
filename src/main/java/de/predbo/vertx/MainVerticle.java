package de.predbo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.StaticHandler;
import de.predbo.vertx.api.user.UserApiProvider;
import de.predbo.vertx.api.user.UserRegistry;
import de.predbo.vertx.handler.FaultHandler;
import de.predbo.vertx.handler.IssueTokenHandler;
import de.predbo.vertx.handler.JwtAuthHandler;

public class MainVerticle extends AbstractVerticle {
	
	private final UserRegistry _userRegistry = new UserRegistry();
	
	@Override
	public void start() {
		Integer listenPort = config().getInteger("http.port", 8080);
		JsonObject authConfig = new JsonObject().put(
				"keyStore",
				new JsonObject().put("type", "jceks")
						.put("path", "keystore.jceks")
						.put("password", "secret"));

		JWTAuth jwtAuthProvider = JWTAuth.create(vertx, authConfig);
		UserApiProvider userApiProvider = new UserApiProvider(_userRegistry);
		
		
		Router router = Router.router(vertx);
		router.route().handler(CookieHandler.create());
		router.get("/issueToken").handler(IssueTokenHandler.create(jwtAuthProvider, _userRegistry));
		router.route("/protected/*").handler(JwtAuthHandler.create(jwtAuthProvider));
		router.mountSubRouter("/protected/api/", userApiProvider.createSubRouter(vertx));
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		router.route().failureHandler(FaultHandler.create());
		
		vertx.createHttpServer().requestHandler(router::accept).listen(listenPort);
	}
	
	

	

}