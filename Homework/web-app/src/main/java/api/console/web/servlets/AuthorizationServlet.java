package api.console.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/login")
public class AuthorizationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName;
        try {
            userName = req.getParameter("user-name");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }
        HttpSession session = req.getSession();
        session.setAttribute("user-name", userName);

        try (PrintWriter writer = resp.getWriter()) {
            String sessionId = session.getId();
            writer.println("Welcome " + userName + ". Your session id is " + sessionId);
        }
    }
}
