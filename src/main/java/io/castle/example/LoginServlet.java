package io.castle.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Castle.setAPISecret(Config.apiKey());
//
//        Event event = new Event();
//        event.setUserId("1234");
//        event.setName("$login.succeeded");
//
//        UserInfoHeader userInfoHeader = UserInfoHeader.fromRequest(req);
//
//        Event result = Event.setUserInfoHeader(userInfoHeader).authenticate(event);
//
//        if (result.getAction().equals("challenge")) {
//            // challenge
//        } else {
//            // 'allow' or 'deny'
//        }

        resp.sendRedirect("authenticated.jsp");
    }
}
