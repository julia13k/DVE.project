package api.console.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "HomeworkMenuServlet", urlPatterns = {"/homework"})
public class HomeworkMenuServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to Homework menu!</p><br>" +
            "            <p align=\"center\">1 - Show all homeworks</p><br>" +
            "            <p align=\"center\">2 - Add a homework</p><br>" +
            "            <p align=\"center\">3 - Delete a homework</p><br>" +
            "            <p align=\"center\">4 - Get a homework</p><br>" +
            "            <p align=\"center\">5 - Exit</p><br></h>");
        out.close();

    }
}
