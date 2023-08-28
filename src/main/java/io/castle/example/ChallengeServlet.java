package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.example.model.TestUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/challenge")
public class ChallengeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.instance().onRequest(req);
        HttpSession session = req.getSession(true);
        if (session.isNew()) {
            resp.sendRedirect("authentication_error.jsp");
        } else {
            Boolean isChallengeSucceeded = Boolean.valueOf(req.getParameter("is_challenge_succeeded"));
            Object challengedUserObject = session.getAttribute("challengedUser");
            TestUser challengedUser = (TestUser) challengedUserObject;
            String userId = challengedUser.getId().toString();
            if (isChallengeSucceeded) {
                castleApi.track("$challenge.succeeded", userId);
                session.setAttribute("currentSessionUser", challengedUserObject);
                session.removeAttribute("challengedUser");
                resp.sendRedirect("/");
            } else {
                castleApi.track("$challenge.failed", userId);
                session.invalidate();
                resp.sendRedirect("authentication_error.jsp");
            }
        }


    }
}
