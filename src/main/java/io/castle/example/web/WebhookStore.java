package io.castle.example.web;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory store of the most recent webhooks received from Castle. A real app
 * would persist these; a list is plenty for a localhost demo.
 */
@Component
public class WebhookStore {

    public static final int MAX = 50;

    private final AtomicInteger seq = new AtomicInteger();
    private final List<Entry> entries = new ArrayList<>();

    public synchronized void add(String prettyBody) {
        entries.add(0, new Entry(seq.incrementAndGet(), Instant.now().toString(), prettyBody));
        while (entries.size() > MAX) {
            entries.remove(entries.size() - 1);
        }
    }

    public synchronized List<Entry> list() {
        return new ArrayList<>(entries);
    }

    public static final class Entry {
        private final int id;
        private final String receivedAt;
        private final String body;

        Entry(int id, String receivedAt, String body) {
            this.id = id;
            this.receivedAt = receivedAt;
            this.body = body;
        }

        public int getId() {
            return id;
        }

        public String getReceivedAt() {
            return receivedAt;
        }

        public String getBody() {
            return body;
        }
    }
}
