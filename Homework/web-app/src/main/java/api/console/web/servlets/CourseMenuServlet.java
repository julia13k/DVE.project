package api.console.web.servlets;

import com.geekhub.services.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@WebServlet(name = "CourseMenuServlet", urlPatterns = {"/course"})
public class CourseMenuServlet extends HttpServlet {
      private List<String> courses;

    @Override
    public void init() throws ServletException {
        super.init();
        this.courses = new ArrayList<>();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            super.service(req,res);
        } catch (AccessDeniedException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (ServletException | IOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong!");
        } catch (IllegalArgumentException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String courseName = extractCourseName(req, "course-name");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + courses.get(courses.indexOf(courseName)) + "</h2>");
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to Course menu!</p><br>");
        out.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        String courseName = extractCourseName(req, "title");
        if(courses.contains(courseName)) {
            throw new IllegalArgumentException("There is a course with such a name already!");
        }
        courses.add(courseName);
        printCourses(resp, courses);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("login");
        if(!Objects.equals(currentUserLogin, "admin")) {
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        String courseName = extractCourseName(req, "title");
        if(!courses.contains(courseName)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no such course!");
            return;
        }
        courses.remove(courseName);
        printCourses(resp, courses);
        return;
    }

    private void printCourses(HttpServletResponse resp, List<String> courses) throws IOException, IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter();) {
            for (String cours : courses) {
                out.print("<h2>" + cours + "</h2>");
            }
        }
    }

    private static String extractCourseName(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
