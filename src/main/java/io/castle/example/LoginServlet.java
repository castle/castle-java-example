package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.Verdict;
import io.castle.example.model.TestUser;
import io.castle.example.model.UserAuthenticationBackend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.sdk().onRequest(req);

        String overrideString = req.getParameter("override");
        AuthenticateAction override = null;

        if (overrideString != null && overrideString.equals("challenge")) {
            override = AuthenticateAction.CHALLENGE;
        } else if (overrideString != null && overrideString.equals("deny")) {
            override = AuthenticateAction.DENY;
        }

        //TODO check params

        castleApi.track("adf", "asdf");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession(true);

        TestUser user = UserAuthenticationBackend.findUser(username);
        if (user != null && user.getPassword().compareTo(password) == 0) {
            String id = user.getId().toString();
            Verdict verdict = castleApi.authenticate("$login.succeeded", id);
            if (override != null) {
                verdict.setAction(override);
                LOGGER.info("The result of the Castle API authenticate call has been overridden to: "
                        + override.toString());
            }
            switch (verdict.getAction()) {
                case DENY: {
                    //TODO not sure if we should track anything here.
                    castleApi.track("$login.failed", id, user);
                    session.invalidate();
                    resp.sendRedirect("authentication_error.jsp");
                }
                break;
                case ALLOW: {
                    castleApi.track("$login.succeeded", id);
                    session.setAttribute("currentSessionUser", user);
                    resp.sendRedirect("/");
                }
                break;
                case CHALLENGE: {
                    castleApi.track("$challenge.requested", id, user);
                    session.setAttribute("challengedUser", user);
                    resp.sendRedirect("challenge.jsp");
                }
                break;
            }
        } else

        {
            if (user != null) {
                castleApi.track("$login.failed", user.getId().toString());
            } else {
                castleApi.track("$login.failed");
            }
            session.invalidate();
            resp.sendRedirect("authentication_error.jsp");
        }
    }
}