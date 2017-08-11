package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


@WebServlet("/challenge_succeeded")
public class ChallengeSuccessServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.sdk().onRequest(req);
        HttpSession session = req.getSession(true);

        if (session.isNew()) {
            resp.sendRedirect("authentication_error.jsp");
        } else {
            castleApi.track("$challenge.succeeded", "1234", "{\"email\": \"johan@castle.io\"}");
            Object user = session.getAttribute("challengedUser");
            session.setAttribute("currentSessionUser", user);
            session.removeAttribute("challengedUser");
            resp.sendRedirect("/");
        }


    }
}
