package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.client.api.CastleApiImpl;
import io.castle.client.model.AuthenticateAction;
import io.castle.example.model.TestUser;
import io.castle.example.model.UserAuthenticationBackend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserAuthenticationBackend authenticationBackend = new UserAuthenticationBackend();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.sdk().onRequest(req);

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession(true);

        TestUser user = authenticationBackend.findUser(username);
        if (user != null && user.getPassword().compareTo(password) == 0) {
            String login = user.getLogin();
            AuthenticateAction authenticateAction = castleApi.authenticate("$login.succeeded", login);
            switch (authenticateAction) {
                case DENY: {
                    //TODO not sure if we should track anything here.
                    castleApi.track("$login.failed", login, user);
                    session.invalidate();
                    resp.sendRedirect("authentication_error.jsp");
                }
                break;
                case ALLOW: {
                    castleApi.track("$login.succeeded", login);
                    session.setAttribute("currentSessionUser", user);
                    resp.sendRedirect("/");
                }
                break;
                case CHALLENGE: {
                    castleApi.track("$challenge.requested", login, user);
                    session.setAttribute("challengedUser", user);
                    resp.sendRedirect("challenge.jsp");
                }
                break;
            }
        } else {
            castleApi.track("$login.failed", username);
            session.invalidate();
            resp.sendRedirect("authentication_error.jsp");
        }
    }
}