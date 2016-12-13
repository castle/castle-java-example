# Castle Java Example

This is an example of integrating Castle with a standard Java web application.

Prerequisites
==============

* Maven
* Java 7+
* castle-java added to your local Maven repository (see below)

Building castle-java
====================

Download the code from: https://github.com/castle/castle-java.

Build it with:

```
$ mvn clean install
```

Running the code
================

Set environment variables `CASTLE_API_SECRET` and `CASTLE_APP_ID`.

Run the web server with:

```
$ mvn tomcat7:run
```

Navigate to:

http://localhost:8080/castle-example/
