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

import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.instance().onRequest(req);

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession(true);

        TestUser user = UserAuthenticationBackend.findUser(username);

        if (user != null && user.getPassword().compareTo(password) == 0) {
            String id = user.getId().toString();
            ImmutableMap properties = ImmutableMap.builder()
                    .put("premium", true)
                    .put("balance", 500)
                    .build();
            
            ImmutableMap traits = ImmutableMap.builder()
                    .put("email", user.getLogin())
                    .put("name", user.getLastname())
                    .build();

            Verdict verdict = castleApi.authenticate(
                    "$login.succeeded",
                    id,
                    properties,
                    traits
            );

            switch (verdict.getAction()) {
                case DENY: {
                    session.invalidate();
                    resp.sendRedirect("authentication_error.jsp");
                }
                break;
                case ALLOW: {
                    session.setAttribute("currentSessionUser", user);
                    resp.sendRedirect("/");
                }
                break;
                case CHALLENGE: {
                    castleApi.track("$challenge.requested", id);
                    session.setAttribute("challengedUser", user);
                    resp.sendRedirect("challenge.jsp");
                }
                break;
            }
        } else {
            if (user != null) {
                ImmutableMap properties = ImmutableMap.builder()
                        .put("email", user.getLogin())
                        .build();
                
                castleApi.track(
                        "$login.failed",
                        user.getId().toString(),
                        null,
                        properties
                );
            } else {
                ImmutableMap properties = ImmutableMap.builder()
                        .put("email", username)
                        .build();
                castleApi.track(
                        "$login.failed",
                        null,
                        null,
                        properties
                );
            }
            session.invalidate();
            resp.sendRedirect("authentication_error.jsp");
        }
    }
}