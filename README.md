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

API demos
=========

The home page links to a set of demos exercising the Castle SDK directly:

* **Lists & list items API** (`/lists-demo`) — creates a list, adds an item,
  queries the items, lists all lists and deletes the list.
* **Events API** (`/events-demo`) — fetches the event schema and queries events.
* **Privacy data request** (`/privacy-demo?userId=...`) — requests the data
  Castle holds for a user.
* **Received webhooks** (`/webhooks`) — `POST` a Castle webhook with a valid
  `X-Castle-Signature` header to have it verified against the raw request body
  and listed on the page.

These demos require `castle-java` 2.2.0.
