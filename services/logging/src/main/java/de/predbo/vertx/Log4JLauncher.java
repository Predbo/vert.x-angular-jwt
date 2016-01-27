package de.predbo.vertx;

import io.vertx.core.Launcher;

public class Log4JLauncher extends Launcher {
	
    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
        new Log4JLauncher().dispatch(args);
    }

}
