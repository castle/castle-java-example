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


@WebServlet("/password_reset_required")
public class PasswordResetRequiredServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        CastleApi castleApi = Castle.sdk().onRequest(req);
        String login = req.getParameter("login");
        TestUser user = UserAuthenticationBackend.findUser(login);
        if (user != null) {
            castleApi.track("$password_reset_request.succeeded", user.getLogin());
            session.setAttribute("passwordResetUser", user);
            resp.sendRedirect("password_reset_request_succeeded.jsp");
        } else {
            //Todo: NullPointerException in OKHttp backend
            castleApi.track("$password_reset_request.failed");
            session.invalidate();
            resp.sendRedirect("password_reset_request_error.jsp");
        }
    }
}
