# vert.x-angular-jwt
Playground to get in touch with vert.x, angularJS and stateless authentication based on JWT

## Dependencies
* Java 8

## Start app
just clone the project and call:

`./gradlew run`

Then open `http://localhost:8083` in your browser and see the overwhelming "Super Hero Website" example :)

## eclipse integration
Create a `run` or `debug` config, and set `de.predbo.vertx.Log4JLauncher` as main class.
In the arguments tab, set the program arguments: `run de.predbo.vertx.HelloWorldVerticle` If we want to use config file add ` -conf <path to config>`. Set the VM argumets to `-Dvertx.options.blockedThreadCheckInterval=2147483647` (to avoid warnings ala "Thread was blocked long time")