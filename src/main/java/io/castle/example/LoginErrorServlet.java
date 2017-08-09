package io.castle.example;

import io.castle.client.api.CastleApiImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/login_error")
public class LoginErrorServlet extends HttpServlet {

    private CastleApiImpl castleApi = new CastleApiImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //TODO: Add proper treatment of properties object.
        castleApi.track("$login.failed", "1234", "{\"email\": \"johan@castle.io\"}");
        resp.sendRedirect("authentication_error.jsp");
    }
}
