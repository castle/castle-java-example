package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.example.model.TestUser;
import io.castle.example.model.UserAuthenticationBackend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;


@WebServlet("/password_reset_required")
public class PasswordResetRequiredServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        CastleApi castleApi = Castle.instance().onRequest(req);
        String login = req.getParameter("login");
        TestUser user = UserAuthenticationBackend.findUser(login);

        if (user != null) {
            castleApi.track(
                    "$password_reset_request.succeeded",
                    user.getId().toString()
            );
            session.setAttribute("passwordResetUser", user);
            resp.sendRedirect("password_reset_request_succeeded.jsp");
        } else {
            ImmutableMap properties = ImmutableMap.builder()
                    .put("email", login)
                    .build();
            
            castleApi.track(
                    "$password_reset_request.failed",
                    null,
                    null,
                    properties
                    );
            session.invalidate();
            resp.sendRedirect("password_reset_request_error.jsp");
        }
    }
}
