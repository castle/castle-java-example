# Castle demo application: Java

This project demonstrates key Castle workflows in a small Spring Boot app built
on the [Castle Java SDK](https://github.com/castle/castle-java). The pages are
rendered server-side with Thymeleaf and the JSON endpoints are plain Spring MVC
controllers.

## What's demonstrated

The app walks through a full user lifecycle. Every action mints a fresh Castle
request token in the browser (`Castle.createRequestToken()`) and forwards it to
the backend, which calls Castle and acts on the verdict.

- **sign up** – `$registration` to `filter` (anonymous, so the email goes in `params`): `$attempted` for a new email, `$failed` (resolved via `matching_user_id`) for an email that already exists
- **login** – `$login` reusing one request token across two calls: `filter` `$attempted` first, then `risk` `$succeeded` on success or `filter` `$failed` (wrong password / unknown user)
- **account** – post-login actions: profile update (`$profile_update` to `risk`), a custom event (`Castle.custom()`), and logout (`$logout` via the non-blocking `log` endpoint)
- **password reset** – `$password_reset` via the non-blocking `log` endpoint
- **lists** – the Lists API (`createList`, `getAllLists`)
- **privacy** – the Privacy API (`requestUserData` and a delete call to the privacy endpoint)
- **webhooks** – incoming Castle webhooks are signature-verified with `verifyWebhookSignature` (against the `X-Castle-Signature` header) and the most recent payloads are listed

## Prerequisites

You'll need a Castle account. If you don't have one, start a free trial at
https://castle.io. For local development, use a **sandbox** environment so demo
traffic from `localhost` stays separate from production data — from the Castle
dashboard (Settings → API) grab the sandbox keys:

- your **publishable key** (`castle_pk`) – used by the browser SDK
- your **API secret** (`castle_api_secret`) – used by the backend SDK

These are the only two values you need to configure.

## Running locally

Requires **JDK 17+** and **Maven**.

```bash
git clone https://github.com/castle/castle-java-example.git
cd castle-java-example
```

The Castle browser SDK and the Tailwind stylesheet are built from npm, so
install the dependencies and build the CSS:

```bash
npm install
npm run build:css
```

Create your `.env` from the example and fill in your two Castle keys:

```bash
cp .env_example .env
```

Run the app:

```bash
mvn spring-boot:run
# Castle Java demo listening on http://localhost:4009
```

The server listens on `:4009` (override with `PORT`). Open
<http://localhost:4009>.

## Running with Docker

The bundled `Dockerfile` builds the stylesheet and browser SDK, compiles the
Spring Boot jar and serves the app on port 80.

```bash
docker build -t castle-java-example .

docker run -d -p 4009:80 \
  -e castle_pk=YOUR_PUBLISHABLE_KEY \
  -e castle_api_secret=YOUR_API_SECRET \
  castle-java-example
```

The app will be available at http://127.0.0.1:4009. Point it at a Castle sandbox
environment when running locally.

## Running the tests

```bash
mvn test
```

## Disclaimer

We're sharing this sample app in the hope that other developers find it
valuable. Although it is not an officially supported sample, we welcome
questions and suggestions at `support@castle.io`.
