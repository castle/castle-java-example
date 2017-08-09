package io.castle.example;

import io.castle.client.api.CastleApiImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/challenge_failed")
public class ChallengeFailureServlet extends HttpServlet {

    private CastleApiImpl castleApi = new CastleApiImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        castleApi.track("$challenge.failed", "1234", "{\"email\": \"johan@castle.io\"}");
        HttpSession session = req.getSession();
        session.invalidate();
        resp.sendRedirect("authentication_error.jsp");
    }
}
