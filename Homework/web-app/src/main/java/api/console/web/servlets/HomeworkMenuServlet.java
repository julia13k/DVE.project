package api.console.web.servlets;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.mylogger.LoggerType;
import com.geekhub.mylogger.MyLogger;
import com.geekhub.services.HomeworkService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.util.Objects.isNull;

@WebServlet(name = "HomeworkMenuServlet", urlPatterns = {"/homework"})
public class HomeworkMenuServlet extends HttpServlet {
    private MyLogger logger;

    @Override
    public void init() throws ServletException {
        super.init();
        this.logger = new MyLogger();
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to Homework menu!</p><br>");
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var homeworkService = applicationContext.getBean(HomeworkService.class);
        int index = extractHomeworkIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + homeworkService.getHomework(index).getTask() + "</h2>");
        }
        return;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var homeworkService = applicationContext.getBean(HomeworkService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var homeworkService = applicationContext.getBean(HomeworkService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractHomeworkIndex(req, "index");
        homeworkService.deleteHomework(index);
        printHomeworks(resp, homeworkService);
        return;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            super.service(req,res);
        } catch (AccessDeniedException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            return;
        }  catch (IllegalArgumentException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            logger.log(LoggerType.ERROR, IllegalArgumentException.class, "Wrong argument input");
            return;
        }   catch (ServletException | IOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong!");
            logger.log(LoggerType.ERROR, AccessDeniedException.class, "Something went wrong!");
            return;
        }
    }

    private void printHomeworks(HttpServletResponse resp, HomeworkService homeworkService) throws IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter()) {
            out.println("<h2>" + homeworkService.showHomeworks() + "</h2>");
        }
        return;
    }

    private int extractHomeworkIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private String extractHomeworkTask(HttpServletRequest request, String parameter) throws IllegalArgumentException, FileNotFoundException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            logger.log(LoggerType.ERROR, IllegalArgumentException.class, "The field is empty!");
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
