package io.castle.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.castle.client.Castle;
import io.castle.client.model.CastleResponse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helpers shared by the demo endpoints: building the scoring payload (with the
 * request context) and turning a {@link CastleResponse} into a plain value the
 * JSON serializer can render.
 */
final class CastleSupport {

    private CastleSupport() {
    }

    /**
     * Copies the echoed payload and attaches the Castle request context (IP,
     * headers, client id) derived from the incoming request. The context is kept
     * off the echoed payload so the browser only sees the meaningful fields.
     */
    static ImmutableMap<Object, Object> withContext(Map<String, Object> payload, HttpServletRequest req) {
        ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            if (entry.getValue() != null) {
                builder.put(entry.getKey(), entry.getValue());
            }
        }
        builder.put("context", Castle.instance().contextBuilder().fromHttpServletRequest(req).build());
        return builder.build();
    }

    /**
     * Converts the SDK's Gson response into a structure Jackson can serialize.
     */
    static Object toJava(CastleResponse response, ObjectMapper mapper) {
        try {
            return mapper.readValue(response.json().toString(), Object.class);
        } catch (Exception e) {
            return response.json().toString();
        }
    }

    static Map<String, Object> error(String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", message);
        return error;
    }
}
