# Castle Java Example

[![CircleCI](https://circleci.com/gh/castle/castle-java-example.svg?style=svg)](https://circleci.com/gh/castle/castle-java-example)

This is an example of integrating Castle with a standard Java web application.

Prerequisites
==============

* Maven
* Java 7+

Running the code
================

Set environment variables `CASTLE_SDK_API_SECRET` and `CASTLE_SDK_APP_ID`.

```
export CASTLE_SDK_API_SECRET=....
export CASTLE_SDK_APP_ID=...
```

Run the web server with:

```
$ mvn jetty:run
```

Navigate to:

http://localhost:8080/
