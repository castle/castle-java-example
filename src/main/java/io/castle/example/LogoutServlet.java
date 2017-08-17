package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.example.model.CustomProperties;
import io.castle.example.model.TestUser;

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
        Object userObject = session.getAttribute("currentSessionUser");
        TestUser user = (TestUser) userObject;

        //Notice that this is a custom event.
        CustomProperties customExample = new CustomProperties();
        customExample.setExampleValue("valueToSend");
        castleApi.track("logout",user.getLogin(), customExample);
        session.invalidate();
        resp.sendRedirect("logout_success.jsp");
    }
}