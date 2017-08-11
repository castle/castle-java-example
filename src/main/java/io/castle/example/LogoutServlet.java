package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.sdk().onRequest(req);
        HttpSession session = req.getSession(true);
        //Notice that this is a custom event.
        castleApi.track("logout","1234", "{\"my_critical_property\": \"52\"}");
        session.invalidate();
        resp.sendRedirect("logout_success.jsp");
    }
}