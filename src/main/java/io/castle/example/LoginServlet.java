package io.castle.example;

import io.castle.client.api.CastleApiImpl;
import io.castle.client.model.AuthenticateAction;
import io.castle.example.utils.UsersModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private CastleApiImpl castleApi = new CastleApiImpl() {

        // While there is no CastleApiImpl implementation, uncomment to test redirect to deny and challenge endpoints

//        @Override
//        public AuthenticateAction authenticate(String event, String userId) {
//            return AuthenticateAction.DENY;
//        }

        @Override
        public AuthenticateAction authenticate(String event, String userId) {
            return AuthenticateAction.CHALLENGE;
        }
    };

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UsersModel model = new UsersModel();
        String user = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession(true);

        if (model.users.containsKey(user) && model.users.get(user).equals(password)) {
            AuthenticateAction authenticateAction = castleApi.authenticate("$login.succeeded", "1234");

            switch (authenticateAction) {
                case DENY: {
                    castleApi.track("$login.failed", "1234", "{\"email\": \"johan@castle.io\"}");
                    session.invalidate();
                    resp.sendRedirect("authentication_error.jsp");
                }
                break;
                case ALLOW: {
                    castleApi.track("$login.succeeded", "1234");
                    session.setAttribute("currentSessionUser", user);
                    resp.sendRedirect("authenticated.jsp");
                }
                break;
                case CHALLENGE: {
                    castleApi.track("$challenge.requested", "1234", "{\"email\": \"johan@castle.io\"}");
                    session.setAttribute("challengedUser", user);
                    resp.sendRedirect("challenge.jsp");
                }
                break;
            }
        } else {
            session.invalidate();
            resp.sendRedirect("login_error");
        }
    }
}