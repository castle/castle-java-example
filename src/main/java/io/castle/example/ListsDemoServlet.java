package io.castle.example;

import com.google.common.collect.ImmutableMap;
import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.client.model.CastleResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Demonstrates the Lists and List items API end to end:
 * create a list, add an item, query the items, list all lists and delete the list.
 */
@WebServlet("/lists-demo")
public class ListsDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><head><title>Lists API demo</title></head><body>");
        out.println("<h2>Lists API demo</h2><p><a href=\"/\">&larr; Home</a></p>");

        CastleApi client = Castle.instance().client();

        try {
            CastleResponse created = client.createList(ImmutableMap.builder()
                    .put("name", "Example trusted IPs")
                    .put("description", "Created by the castle-java example app")
                    .put("color", "$green")
                    .put("primary_field", "context.ip")
                    .build());
            render(out, "createList", created);

            String listId = created.json().getAsJsonObject().get("id").getAsString();

            CastleResponse item = client.createListItem(listId, ImmutableMap.builder()
                    .put("primary_value", "1.2.3.4")
                    .put("comment", "added from the example app")
                    .build());
            render(out, "createListItem", item);

            CastleResponse items = client.queryListItems(listId, ImmutableMap.builder()
                    .put("filters", ImmutableMap.of())
                    .build());
            render(out, "queryListItems", items);

            CastleResponse all = client.getAllLists();
            render(out, "getAllLists", all);

            CastleResponse deleted = client.deleteList(listId);
            render(out, "deleteList", deleted);
        } catch (Exception e) {
            out.println("<pre style=\"color:red\">Error: " + e.getMessage() + "</pre>");
        }

        out.println("</body></html>");
    }

    static void render(PrintWriter out, String label, CastleResponse response) {
        out.println("<h3>" + label + " (HTTP " + (response.isSuccessful() ? "2xx" : "error") + ")</h3>");
        out.println("<pre>" + response.json() + "</pre>");
    }
}
