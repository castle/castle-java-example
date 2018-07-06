package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.example.model.TestUser;
import io.castle.example.model.UserAuthenticationBackend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/email_change")
public class EmailChangeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Object currentSessionUserObject = session.getAttribute("currentSessionUser");

        if (!session.isNew() && currentSessionUserObject != null) {
            CastleApi castleApi = Castle.instance().onRequest(req);
            TestUser currentSessionUser = (TestUser) currentSessionUserObject;
            Boolean shouldEmailChange = Boolean.valueOf(req.getParameter("should_email_change"));
            Object requestedEmailObject = session.getAttribute("requestedEmail");
            String requestedEmail = (String) requestedEmailObject;
            String userId = currentSessionUser.getId().toString();
            if (shouldEmailChange && requestedEmail != null) {
                try {
                    UserAuthenticationBackend.updateUserLogin(currentSessionUser.getLogin(), requestedEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                castleApi.track("$email_change.succeeded", userId);
                session.invalidate();
                resp.sendRedirect("/email_update_success.jsp");
            } else {
                castleApi.track("$email_change.failed", userId);
                session.invalidate();
                resp.sendRedirect("/authentication_error.jsp");
            }
        } else {
            session.invalidate();
            resp.sendRedirect("/");
        }
    }
}
