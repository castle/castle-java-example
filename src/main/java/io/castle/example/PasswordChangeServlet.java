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


@WebServlet("/password_change")
public class PasswordChangeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object currentSessionUserObject = session.getAttribute("currentSessionUser");
        if (!session.isNew() && currentSessionUserObject != null) {
            CastleApi castleApi = Castle.instance().onRequest(req);
            TestUser currentSessionUser = (TestUser) currentSessionUserObject;
            String currentPassword = req.getParameter("password");
            String newPassword = req.getParameter("new_password");
            String newPasswordConfirm = req.getParameter("new_password_confirm");
            String userId = currentSessionUser.getId().toString();
            if (currentPassword != null
                    && currentPassword.equals(currentSessionUser.getPassword())
                    && newPassword != null
                    && newPasswordConfirm != null
                    && newPassword.equals(newPasswordConfirm)) {
                try {
                    UserAuthenticationBackend.updateUserPassword(currentSessionUser.getLogin(), newPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                castleApi.track("$password_change.succeeded", userId);
                session.invalidate();
                resp.sendRedirect("password_reset_success.jsp");
            } else {
                castleApi.track("$password_change.failed", userId);
                session.invalidate();
                resp.sendRedirect("/authentication_error.jsp");
            }
        } else {
            session.invalidate();
            resp.sendRedirect("/");
        }
    }
}
