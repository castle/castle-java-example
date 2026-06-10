package io.castle.example;

import io.castle.client.Castle;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Receives Castle webhooks and verifies the {@code X-Castle-Signature} header
 * against the raw request body before trusting the payload.
 */
@WebServlet("/webhooks")
public class WebhookServlet extends HttpServlet {

    private static final List<String> RECEIVED = new CopyOnWriteArrayList<String>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] body = readBody(req);

        boolean valid = Castle.instance().verifyWebhookSignature(req, body);

        if (!valid) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("invalid signature");
            return;
        }

        RECEIVED.add(0, new String(body, "UTF-8"));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("ok");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><head><title>Webhooks</title></head><body>");
        out.println("<h2>Received webhooks</h2><p><a href=\"/\">&larr; Home</a></p>");
        out.println("<p>POST a Castle webhook to <code>/webhooks</code> with a valid "
                + "<code>X-Castle-Signature</code> header to see it verified and listed here.</p>");
        if (RECEIVED.isEmpty()) {
            out.println("<p><em>No verified webhooks received yet.</em></p>");
        }
        for (String payload : RECEIVED) {
            out.println("<pre>" + payload + "</pre>");
        }
        out.println("</body></html>");
    }

    private byte[] readBody(HttpServletRequest req) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ServletInputStream in = req.getInputStream();
        byte[] chunk = new byte[4096];
        int read;
        while ((read = in.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toByteArray();
    }
}
