# vert.x-angular-jwt
Playground to get in touch with vert.x, angularJS and stateless authentication based on JWT. Currently the Angular and JWT part is still missing, the following aspects are already considered:
* vertx-core
* vertx-web
* Logging with log4j
* CRUD REST API
* junit tests with vertx-unit, junit and unirest
* a simple stress test


## Dependencies
* Java 8
* bootstrap for nicer html output
* see build.gradle file for all dependencies


## Development
just clone the project and call:

`./gradlew clean run`

Then open `http://localhost:8080` in your browser and see the overwhelming "Super Hero Website" example :)
Any code changes will let to an automatically redeployment of the app.

### Eclipse integration

* call `./gradlew clean eclipse`, open eclipse and Import as existing eclipse project.
* Create a `run` or `debug` config, and set `de.predbo.vertx.Log4JLauncher` as main class.
* In the arguments tab, set the program arguments: `run de.predbo.vertx.MainVerticle -conf src/main/resources/vertx.config`. 
* Set the VM argumets to `-Dvertx.options.blockedThreadCheckInterval=600000` (to avoid warnings ala "Thread was blocked long time")


## Build, package and start app
`./gradlew clean shadowJar` will do everything for you. It will create a jar file in `build/lib/` directory. This jar can be executed with `java -jar {jar file}` 
####Example
`java -jar build/libs/vert.x-angular-jwt-3.2.0-fat.jar -conf src/main/resources/vertx.config -instances 4`

That's all!