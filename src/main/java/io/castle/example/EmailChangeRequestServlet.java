package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.example.model.TestUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/email_change_request")
public class EmailChangeRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Object currentSessionUserObject = session.getAttribute("currentSessionUser");
        if (!session.isNew() && currentSessionUserObject != null) {
            CastleApi castleApi = Castle.instance().onRequest(req);
            TestUser currentSessionUser = (TestUser) currentSessionUserObject;
            String userId = currentSessionUser.getId().toString();
            castleApi.track("$email_change.requested", userId);
            String password = req.getParameter("password");
            if (password != null
                    && password.equals(currentSessionUser.getPassword())) {

                session.setAttribute("requestedEmail", req.getParameter("username"));
                resp.sendRedirect("email_change_request_succeeded.jsp");
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
