package api.console.web.servlets;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.models.Lection;
import com.geekhub.mylogger.LoggerType;
import com.geekhub.mylogger.MyLogger;
import com.geekhub.services.LectionService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.util.Objects.isNull;

@WebServlet(name = "LectionMenuServlet", urlPatterns = {"/lecture"})
public class LectionMenuServlet extends HttpServlet {
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
        out.println("<h1> <p align=\"center\">Welcome to Lecture menu!</p><br>");
        out.close();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            super.service(req,res);
        } catch (AccessDeniedException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            return;
        } catch (IllegalArgumentException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            logger.log(LoggerType.ERROR, IllegalArgumentException.class, "Wrong argument input");
            return;
        } catch (ServletException | IOException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong!");
            logger.log(LoggerType.ERROR, AccessDeniedException.class, "Something went wrong!");
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var lectionService = applicationContext.getBean(LectionService.class);
        int index = extractLectureIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + lectionService.getLection(index).getName() + "</h2>");
        }
        return;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var lectionService = applicationContext.getBean(LectionService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        String lectureName = extractLectureName(req, "lecture-name");
        String description = extractLectureName(req, "description");
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String pattern = "MM-dd-yyyy HH:mm:ss:SS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String format = zonedDateTime.format(formatter);
        Lection lection = lectionService.createLection(lectureName, description);
        lection.setCreationDate(format);
        printLectures(resp, lectionService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var lectionService = applicationContext.getBean(LectionService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractLectureIndex(req, "index");
        lectionService.deleteLection(index);
        printLectures(resp, lectionService);
        return;
    }

    private void printLectures(HttpServletResponse resp, LectionService lectionService) throws IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter()) {
            out.println("<h2>" + lectionService.showLections() + "</h2>");
        }
        return;
    }

    private int extractLectureIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private String extractLectureName(HttpServletRequest request, String parameter) throws IllegalArgumentException, FileNotFoundException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            logger.log(LoggerType.ERROR, IllegalArgumentException.class, "The field is empty!");
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
