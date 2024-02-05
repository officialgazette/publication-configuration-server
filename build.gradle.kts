plugins {
  id( "com.github.johnrengelman.shadow" ) version "8.1.1"
  id( "io.micronaut.application" ) version "4.2.1"
  id( "io.micronaut.aot" ) version "4.2.1"
}

version = "1.1.0"
group = "ch.amtsblattportal.terms"

repositories {
  mavenCentral()
}

configurations.all {
  resolutionStrategy {
    force( "com.oracle.database.jdbc:ojdbc10:19.16.0.0" )
    force( "com.oracle.database.jdbc:ucp:19.16.0.0" )
    force( "com.oracle.database.ha:ons:19.16.0.0" )
    force( "com.oracle.database.security:oraclepki:19.16.0.0" )
    force( "com.oracle.database.security:osdt_core:19.16.0.0" )
    force( "com.oracle.database.security:osdt_cert:19.16.0.0" )
  }
}

dependencies {
  annotationProcessor( "org.projectlombok:lombok" )
  implementation( "org.projectlombok:lombok" )
  implementation( "io.micronaut:micronaut-jackson-databind" )
  implementation( "jakarta.validation:jakarta.validation-api" )
  runtimeOnly( "ch.qos.logback:logback-classic" )
  runtimeOnly( "org.yaml:snakeyaml" )
  compileOnly( "com.google.code.findbugs:jsr305" )
  implementation( "com.oracle.database.jdbc:ojdbc10" )
  implementation( "com.oracle.database.jdbc:ucp" )
  implementation( "com.oracle.database.ha:ons" )
  implementation( "com.oracle.database.security:oraclepki" )
  implementation( "com.oracle.database.security:osdt_core" )
  implementation( "com.oracle.database.security:osdt_cert" )
}

application {
  mainClass.set( "ch.amtsblattportal.terms.App" )
}
java {
  sourceCompatibility = JavaVersion.toVersion( "17" )
  targetCompatibility = JavaVersion.toVersion( "17" )
}

graalvmNative.toolchainDetection.set( false )

micronaut {
  runtime( "netty" )
  testRuntime( "junit5" )
  processing {
    incremental( true )
    annotations( "ch.amtsblattportal.terms.*" )
  }
}
