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
 * Demonstrates the privacy data-request API.
 */
@WebServlet("/privacy-demo")
public class PrivacyDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><head><title>Privacy API demo</title></head><body>");
        out.println("<h2>Privacy API demo</h2><p><a href=\"/\">&larr; Home</a></p>");

        String userId = req.getParameter("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "1";
        }

        CastleApi client = Castle.instance().client();

        try {
            CastleResponse response = client.requestUserData(ImmutableMap.builder()
                    .put("user_id", userId)
                    .build());
            ListsDemoServlet.render(out, "requestUserData(" + userId + ")", response);
        } catch (Exception e) {
            out.println("<pre style=\"color:red\">Error: " + e.getMessage() + "</pre>");
        }

        out.println("</body></html>");
    }
}
