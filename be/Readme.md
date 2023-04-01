# Introduction

Rest API for Hack Koscie 2023 for 15 - minute city

## Build

    ./gradlew build

## JavaDoc

    ./gradlew javadoc

# Run application

#### Production

    java -jar -Dspring.profiles.active=prod hackkosice-{version}.jar

#### Development

    java -jar hackkosice-{version}.jar

# Security

Default is security enabled for all paths
except `/, /version, /oauth2/** /actuator/**, /swagger-ui/**, /api-docs/**`.  
see [Spring Security reference](https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)

### Proxy

set JVM args  `https.proxyHost` and `https.proxyPort` (also `https.proxyUser` and `https.proxyPassword` with
authentication) to communication with Azure servers through proxy.

