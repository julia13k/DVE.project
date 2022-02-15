package api.console.web.servlets;

import com.geekhub.services.HomeworkService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.util.Objects.isNull;

@WebServlet(name = "HomeworkMenuServlet", urlPatterns = {"/homework"})
public class HomeworkMenuServlet extends HttpServlet {
    private HomeworkService homeworkService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.homeworkService = new HomeworkService();
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to Homework menu!</p><br>");
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = extractHomeworkIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + homeworkService.getHomework(index).getTask() + "</h2>");
        }
        return;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        String homeworkTask = extractHomeworkTask(req, "task");
        String deadline = extractHomeworkTask(req, "deadline");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss:SS");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(deadline, formatter);
        homeworkService.createHomework(homeworkTask, String.valueOf(zonedDateTime));
        printHomeworks(resp, homeworkService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractHomeworkIndex(req, "index");
        homeworkService.deleteHomework(index);
        printHomeworks(resp, homeworkService);
        return;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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

    private void printHomeworks(HttpServletResponse resp, HomeworkService homeworkService) throws IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter()) {
            out.println("<h2>" + homeworkService.showHomeworks() + "</h2>");
        }
        return;
    }

    private static int extractHomeworkIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private static String extractHomeworkTask(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
