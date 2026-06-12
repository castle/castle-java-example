package io.castle.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.client.model.CastleResponse;
import io.castle.client.model.generated.ListColor;
import io.castle.client.model.generated.ListRequest;
import io.castle.client.model.generated.ListResponse;
import io.castle.example.config.DemoEnv;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The JSON endpoints driven by the browser. Each one builds the payload it
 * sends to Castle, echoes it back (without the noisy context object) and
 * returns the verdict so the page can render it.
 */
@RestController
public class DemoController {

    // A fixed timestamp reused for the simulated valid user.
    private static final String REGISTERED_AT = "2020-02-23T22:28:55.387Z";

    private final DemoEnv env;
    private final ObjectMapper mapper;
    private final WebhookStore webhooks;

    public DemoController(DemoEnv env, ObjectMapper mapper, WebhookStore webhooks) {
        this.env = env;
        this.mapper = mapper;
        this.webhooks = webhooks;
    }

    // --- Filter (registration) ------------------------------------------------

    @PostMapping("/evaluate_signup")
    public Map<String, Object> evaluateSignup(@RequestBody(required = false) Map<String, Object> body,
                                              HttpServletRequest req) {
        Map<String, Object> b = orEmpty(body);
        String email = str(b, "email");
        String requestToken = str(b, "request_token");

        String type = "$registration";
        String status;

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", type);
        payload.put("params", mapOf("email", email));
        payload.put("request_token", requestToken);
        if (email.equals(env.get("valid_username"))) {
            status = "$failed";
            payload.put("matching_user_id", env.get("valid_user_id"));
        } else {
            status = "$attempted";
        }
        payload.put("status", status);

        Object result = scoring(() -> client().filter(CastleSupport.withContext(payload, req)));

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("api_endpoint", "filter");
        out.put("payload_to_castle", payload);
        out.put("result", result);
        out.put("castle_type", type);
        out.put("castle_status", status);
        return out;
    }

    // --- Filter -> Risk (login) ----------------------------------------------

    @PostMapping("/evaluate_login")
    public Map<String, Object> evaluateLogin(@RequestBody(required = false) Map<String, Object> body,
                                             HttpServletRequest req) {
        Map<String, Object> b = orEmpty(body);
        String email = str(b, "email");
        String password = str(b, "password");
        String requestToken = str(b, "request_token");

        List<Map<String, Object>> steps = new ArrayList<>();

        // Step 1 — always filter the attempt up front (anonymous -> params).
        steps.add(loginStep(req, "filter", "$attempted", requestToken,
                Map.of("params", mapOf("email", email))));

        // Step 2 — the outcome, on the same request token.
        if (email.equals(env.get("valid_username")) && password.equals(env.get("valid_password"))) {
            Map<String, Object> user = new LinkedHashMap<>();
            user.put("id", env.get("valid_user_id"));
            user.put("email", email);
            user.put("registered_at", REGISTERED_AT);
            steps.add(loginStep(req, "risk", "$succeeded", requestToken, Map.of("user", user)));
        } else {
            Map<String, Object> fields = new LinkedHashMap<>();
            fields.put("params", mapOf("email", email));
            if (email.equals(env.get("valid_username"))) {
                fields.put("matching_user_id", env.get("valid_user_id"));
            }
            steps.add(loginStep(req, "filter", "$failed", requestToken, fields));
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("steps", steps);
        return out;
    }

    private Map<String, Object> loginStep(HttpServletRequest req, String apiEndpoint, String status,
                                          String requestToken, Map<String, Object> fields) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "$login");
        payload.put("status", status);
        payload.put("request_token", requestToken);
        payload.putAll(fields);

        Object result = scoring(() -> apiEndpoint.equals("risk")
                ? client().risk(CastleSupport.withContext(payload, req))
                : client().filter(CastleSupport.withContext(payload, req)));

        Map<String, Object> step = new LinkedHashMap<>();
        step.put("api_endpoint", apiEndpoint);
        step.put("payload_to_castle", payload);
        step.put("result", result);
        step.put("castle_type", "$login");
        step.put("castle_status", status);
        return step;
    }

    // --- Risk (profile update) -----------------------------------------------

    @PostMapping("/evaluate_profile_update")
    public Map<String, Object> evaluateProfileUpdate(@RequestBody(required = false) Map<String, Object> body,
                                                     HttpServletRequest req) {
        Map<String, Object> b = orEmpty(body);
        String name = str(b, "name");
        String email = str(b, "email");
        if (email.isEmpty()) {
            email = env.get("valid_username");
        }
        String requestToken = str(b, "request_token");

        String type = "$profile_update";
        String status = "$succeeded";

        Map<String, Object> user = new LinkedHashMap<>();
        user.put("id", env.get("valid_user_id"));
        user.put("email", email);
        user.put("name", name);
        user.put("registered_at", REGISTERED_AT);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", type);
        payload.put("status", status);
        payload.put("user", user);
        payload.put("request_token", requestToken);

        Object result = scoring(() -> client().risk(CastleSupport.withContext(payload, req)));

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("api_endpoint", "risk");
        out.put("payload_to_castle", payload);
        out.put("result", result);
        out.put("castle_type", type);
        out.put("castle_status", status);
        return out;
    }

    // --- Log (password reset) ------------------------------------------------

    @PostMapping("/evaluate_new_password")
    public Map<String, Object> evaluateNewPassword(@RequestBody(required = false) Map<String, Object> body,
                                                   HttpServletRequest req) {
        Map<String, Object> b = orEmpty(body);
        String password = str(b, "password");
        String requestToken = str(b, "request_token");

        // A new password that differs from the current one is a successful reset.
        String status = password.equals(env.get("valid_password")) ? "$failed" : "$succeeded";
        String type = "$password_reset";

        Map<String, Object> user = new LinkedHashMap<>();
        user.put("id", env.get("valid_user_id"));
        user.put("email", env.get("valid_username"));
        user.put("registered_at", REGISTERED_AT);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", type);
        payload.put("status", status);
        payload.put("user", user);
        payload.put("request_token", requestToken);

        Object result = logResult(() -> client().log(CastleSupport.withContext(payload, req)));

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("api_endpoint", "log");
        out.put("payload_to_castle", payload);
        out.put("result", result);
        out.put("type", type);
        out.put("status", status);
        return out;
    }

    // --- Log (logout) --------------------------------------------------------

    @PostMapping("/evaluate_logout")
    public Map<String, Object> evaluateLogout(@RequestBody(required = false) Map<String, Object> body,
                                              HttpServletRequest req) {
        Map<String, Object> b = orEmpty(body);
        String requestToken = str(b, "request_token");

        String type = "$logout";
        String status = "$succeeded";

        Map<String, Object> user = new LinkedHashMap<>();
        user.put("id", env.get("valid_user_id"));
        user.put("email", env.get("valid_username"));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", type);
        payload.put("status", status);
        payload.put("user", user);
        payload.put("request_token", requestToken);

        Object result = logResult(() -> client().log(CastleSupport.withContext(payload, req)));

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("api_endpoint", "log");
        out.put("payload_to_castle", payload);
        out.put("result", result);
        out.put("castle_type", type);
        out.put("castle_status", status);
        return out;
    }

    // --- Lists API -----------------------------------------------------------

    @PostMapping("/create_list")
    public Map<String, Object> createList(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> b = orEmpty(body);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("name", orDefault(str(b, "name"), "demo-blocklist"));
        payload.put("color", orDefault(str(b, "color"), "$red"));
        payload.put("primary_field", orDefault(str(b, "primary_field"), "user.email"));

        Object result;
        try {
            CastleApi client = client();
            ListRequest request = new ListRequest();
            request.setName((String) payload.get("name"));
            request.setColor(ListColor.fromValue((String) payload.get("color")));
            request.setPrimaryField((String) payload.get("primary_field"));
            ListResponse created = client.createList(request);
            List<ListResponse> all = client.listAllLists();
            Map<String, Object> ok = new LinkedHashMap<>();
            ok.put("created", mapper.convertValue(created, Object.class));
            ok.put("all_lists", mapper.convertValue(all, Object.class));
            result = ok;
        } catch (Exception e) {
            result = CastleSupport.error(e.getMessage());
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("api_endpoint", "lists");
        out.put("payload_to_castle", payload);
        out.put("result", result);
        return out;
    }

    // --- Privacy API ---------------------------------------------------------

    @PostMapping("/privacy_user_data")
    public Map<String, Object> privacyUserData(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> b = orEmpty(body);
        String action = orDefault(str(b, "action"), "request");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("identifier", orDefault(str(b, "identifier"), env.get("valid_username")));
        payload.put("identifier_type", orDefault(str(b, "identifier_type"), "$email"));

        String apiEndpoint;
        Object result;
        try {
            CastleApi client = client();
            CastleResponse response;
            if ("delete".equals(action)) {
                apiEndpoint = "privacy (delete)";
                response = client.delete("/v1/privacy/users", toImmutable(payload));
            } else {
                apiEndpoint = "privacy (request)";
                response = client.requestUserData(toImmutable(payload));
            }
            result = CastleSupport.toJava(response, mapper);
        } catch (Exception e) {
            apiEndpoint = "privacy";
            result = CastleSupport.error(e.getMessage());
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("api_endpoint", apiEndpoint);
        out.put("payload_to_castle", payload);
        out.put("result", result);
        return out;
    }

    // --- Events API ----------------------------------------------------------

    @PostMapping("/events_demo")
    public Map<String, Object> eventsDemo(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> b = orEmpty(body);
        String type = orDefault(str(b, "type"), "$login");

        List<Map<String, Object>> steps = new ArrayList<>();
        CastleApi client = client();

        Map<String, Object> schemaStep = new LinkedHashMap<>();
        schemaStep.put("api_endpoint", "events/schema");
        try {
            schemaStep.put("result", CastleSupport.toJava(client.eventsSchema(), mapper));
        } catch (Exception e) {
            schemaStep.put("result", CastleSupport.error(e.getMessage()));
        }
        steps.add(schemaStep);

        Map<String, Object> queryPayload = new LinkedHashMap<>();
        queryPayload.put("type", type);

        Map<String, Object> queryStep = new LinkedHashMap<>();
        queryStep.put("api_endpoint", "events/query");
        queryStep.put("payload_to_castle", queryPayload);
        try {
            queryStep.put("result", CastleSupport.toJava(client.queryEvents(toImmutable(queryPayload)), mapper));
        } catch (Exception e) {
            queryStep.put("result", CastleSupport.error(e.getMessage()));
        }
        steps.add(queryStep);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("steps", steps);
        return out;
    }

    // --- Webhook receiver ----------------------------------------------------

    @PostMapping(value = "/webhooks/castle")
    public ResponseEntity<String> receiveWebhook(@RequestBody(required = false) byte[] rawBody,
                                                 HttpServletRequest req) {
        byte[] body = rawBody != null ? rawBody : new byte[0];

        if (!Castle.instance().verifyWebhookSignature(req, body)) {
            return ResponseEntity.status(404).contentType(MediaType.TEXT_PLAIN)
                    .body("invalid signature");
        }

        webhooks.add(prettyPrint(body));
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("ok");
    }

    // --- helpers -------------------------------------------------------------

    private CastleApi client() {
        return Castle.instance().client();
    }

    private Object scoring(Supplier<CastleResponse> call) {
        try {
            return CastleSupport.toJava(call.get(), mapper);
        } catch (Exception e) {
            return CastleSupport.error(e.getMessage());
        }
    }

    private Object logResult(Supplier<CastleResponse> call) {
        try {
            call.get();
            return Map.of("logged", true);
        } catch (Exception e) {
            return CastleSupport.error(e.getMessage());
        }
    }

    private String prettyPrint(byte[] body) {
        String raw = new String(body, java.nio.charset.StandardCharsets.UTF_8);
        try {
            Object parsed = mapper.readValue(raw, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parsed);
        } catch (Exception e) {
            return raw;
        }
    }

    private static ImmutableMap<Object, Object> toImmutable(Map<String, Object> payload) {
        ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
        payload.forEach((k, v) -> {
            if (v != null) {
                builder.put(k, v);
            }
        });
        return builder.build();
    }

    private static Map<String, Object> mapOf(String key, Object value) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    private static Map<String, Object> orEmpty(Map<String, Object> body) {
        return body != null ? body : new LinkedHashMap<>();
    }

    private static String str(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? "" : value.toString();
    }

    private static String orDefault(String value, String fallback) {
        return value == null || value.isEmpty() ? fallback : value;
    }
}
