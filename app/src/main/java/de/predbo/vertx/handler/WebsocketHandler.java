package de.predbo.vertx.handler;

import java.util.Date;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class WebsocketHandler implements Handler<RoutingContext> {

	private static final Logger _logger = LoggerFactory.getLogger(WebsocketHandler.class);
	
	private final SockJSHandler _sockJSHandler;

	public WebsocketHandler(Vertx vertx) {
		_sockJSHandler = createSockJsBridge(vertx);
		
		EventBus eventBus = vertx.eventBus();
				
		vertx.setPeriodic(3000, time -> {
			eventBus.publish("web.server2client.heartbeat", new Date().toString() + " Server is still alive!");
		});
		
		eventBus.consumer("web.client2server.send.test", message -> {
			_logger.info("individual request: " + message.body());
			message.reply("this is your response to '" +  message.body() + "'");
		});
		
		eventBus.consumer("web.client2server.publish.test", message -> {
			_logger.info("message to publish: " + message.body());
			eventBus.publish("web.server2client.publish.test", message.body());
		});
	}
	
	@Override
	public void handle(RoutingContext event) {
		_sockJSHandler.handle(event);
	}

	
	
	
	
	private SockJSHandler createSockJsBridge(Vertx vertx) {
		SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions()
        .addOutboundPermitted(new PermittedOptions().setAddressRegex("web.server2client.*"))
        .addInboundPermitted(new PermittedOptions().setAddressRegex("web.client2server.*"));
		sockJSHandler.bridge(options,be -> {
			if (be.type() == BridgeEventType.SOCKET_CREATED) {
				_logger.info("A socket was created");
			}
//			if (be.type() == BridgeEventType.RECEIVE) {
//				_logger.info("Send message to client: " + be.rawMessage().toString());
//			}
//			if (be.type() == BridgeEventType.SEND) {
//				_logger.info("Received message from client: " + be.rawMessage().toString());
//			}
			be.complete(true);
		});
		
		return sockJSHandler;
	}
}
