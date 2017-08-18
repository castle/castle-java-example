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


@WebServlet("/password_reset")
public class PasswordResetServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object passwordResetUserObject = session.getAttribute("passwordResetUser");
        if (!session.isNew() && passwordResetUserObject != null) {
            CastleApi castleApi = Castle.sdk().onRequest(req);
            TestUser passwordResetUser = (TestUser) passwordResetUserObject;
            Boolean shouldPasswordReset = Boolean.valueOf(req.getParameter("password_reset"));
            String newPassword = req.getParameter("new_password");
            String newPasswordConfirm = req.getParameter("new_password_confirm");
            String userId = passwordResetUser.getId().toString();
            if (shouldPasswordReset
                    && newPassword != null
                    && newPasswordConfirm != null
                    && newPassword.equals(newPasswordConfirm)) {
                try {
                    UserAuthenticationBackend.updateUserPassword(passwordResetUser.getLogin(), newPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                castleApi.track("$password_reset.succeeded", userId);
                session.invalidate();
                resp.sendRedirect("password_reset_success.jsp");
            } else {
                castleApi.track("$password_reset.failed", userId);
                session.invalidate();
                resp.sendRedirect("/");
            }
        } else {
            session.invalidate();
            resp.sendRedirect("/");
        }
    }
}
