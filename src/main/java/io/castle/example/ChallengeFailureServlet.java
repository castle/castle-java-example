package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/challenge_failed")
public class ChallengeFailureServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.sdk().onRequest(req);
        castleApi.track("$challenge.failed", "1234", "{\"email\": \"johan@castle.io\"}");
        HttpSession session = req.getSession();
        session.invalidate();
        resp.sendRedirect("authentication_error.jsp");
    }
}
