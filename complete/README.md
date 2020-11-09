# Micronaut + JPA Example

## Using Gradle

To run the application locally use:

```
./gradlew run -t
```

The `-t` flag enables automatical reload.

To build an executable JAR file run:

```
./gradlew assemble
java -jar build/libs/complete-0.1-all.jar
```

To build a native image (requires JDK 8 version of GraalVM) run:

```
./gradlew nativeImage
./build/native-image/application
```

## Using Maven

To run the application locally use:

```
./mvnw mn:run
```

To build an executable JAR file run:

```
./mvnw package
java -jar target/demo-0.1.jar
```

To build a native image (requires JDK 8 version of GraalVM) run:

```
native-image --report-unsupported-elements-at-runtime -H:Name=application -H:Class=example.micronaut.Application -cp target/demo-0.1.jar
./application
```