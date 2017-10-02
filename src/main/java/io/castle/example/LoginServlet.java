package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.Verdict;
import io.castle.example.model.EmailProperties;
import io.castle.example.model.FullNameTraits;
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

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession(true);

        TestUser user = UserAuthenticationBackend.findUser(username);
        EmailProperties email = new EmailProperties();

        if (user != null && user.getPassword().compareTo(password) == 0) {
            String id = user.getId().toString();
            email.setEmail(user.getLogin());

            FullNameTraits fullName = new FullNameTraits();
            fullName.setUsername(user.getUsername());
            fullName.setLastname(user.getLastname());

            Verdict verdict = castleApi.authenticate(
                    "$login.succeeded",
                    id,
                    email,
                    fullName
            );

            switch (verdict.getAction()) {
                case DENY: {
                    castleApi.track("$login.failed", id, null,email);
                    session.invalidate();
                    resp.sendRedirect("authentication_error.jsp");
                }
                break;
                case ALLOW: {
                    castleApi.track("$login.succeeded", id, null,email);
                    session.setAttribute("currentSessionUser", user);
                    resp.sendRedirect("/");
                }
                break;
                case CHALLENGE: {
                    castleApi.track("$challenge.requested", id, null,email);
                    session.setAttribute("challengedUser", user);
                    resp.sendRedirect("challenge.jsp");
                }
                break;
            }
        } else {
            if (user != null) {
                email.setEmail(user.getLogin());
                castleApi.track(
                        "$login.failed",
                        user.getId().toString(),
                        null,
                        email
                );
            } else {
                email.setEmail(username);
                castleApi.track(
                        "$login.failed",
                        null,
                        null,
                        email
                );
            }
            session.invalidate();
            resp.sendRedirect("authentication_error.jsp");
        }
    }
}