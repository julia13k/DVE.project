package api.console.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "ApplicationStarterServlet", urlPatterns = {"/"})
public class ApplicationStarterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to application menu!</p><br>" +
            "<p align=\"center\">1 - Course menu</p><br>" +
            "            <p align=\"center\">2 - Lections menu</p><br>" +
            "            <p align=\"center\">3 - Person menu</p><br>" +
            "            <p align=\"center\">4 - Additional material menu</p><br>" +
            "            <p align=\"center\">5 - Homework menu</p><br>" +
            "            <p align=\"center\">6 - Logger menu</p><br>" +
            "            <p align=\"center\">7 - Exit</p><br></h>");
    }
}
