package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.api.CastleApi;
import io.castle.example.model.TestUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.instance().onRequest(req);
        HttpSession session = req.getSession(true);
        Object userObject = session.getAttribute("currentSessionUser");
        TestUser user = (TestUser) userObject;


        castleApi.track("$logout.succeeded", user.getId().toString());
        session.invalidate();
        resp.sendRedirect("logout_success.jsp");
    }
}