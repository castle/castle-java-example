package io.castle.example;

import io.castle.client.api.CastleApiImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


@WebServlet("/challenge_succeeded")
public class ChallengeSuccessServlet extends HttpServlet {

    private CastleApiImpl castleApi = new CastleApiImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(true);

        if (session.isNew()) {
            resp.sendRedirect("authentication_error.jsp");
        } else {
            castleApi.track("$challenge.succeeded", "1234", "{\"email\": \"johan@castle.io\"}");
            Object user = session.getAttribute("challengedUser");
            session.setAttribute("currentSessionUser", user);
            session.removeAttribute("challengedUser");
            resp.sendRedirect("authenticated.jsp");
        }


    }
}
