package api.console.web.servlets;

import com.geekhub.models.Course;
import com.geekhub.services.CourseService;
import filters.AuthorizationFilter;

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
import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;

@WebServlet(name = "CourseMenuServlet", urlPatterns = {"/course"})
public class CourseMenuServlet extends HttpServlet {
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.courseService = new CourseService();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            super.service(req,res);
        } catch (AccessDeniedException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        } catch (ServletException | IOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong!");
            return;
        } catch (IllegalArgumentException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = extractCourseIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.println(courseService.getCourse(index).getName());
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
        String courseName = extractCourseName(req, "course-name");
        courseService.createCourse(courseName);
        printCourses(resp, courseService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractCourseIndex(req, "index");
        courseService.deleteCourse(index);
        printCourses(resp, courseService);
        return;
    }

    private void printCourses(HttpServletResponse resp, CourseService courseService) throws IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter()) {
            out.println(courseService.showCourses());
        }
        return;
    }

    private static int extractCourseIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private static String extractCourseName(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
