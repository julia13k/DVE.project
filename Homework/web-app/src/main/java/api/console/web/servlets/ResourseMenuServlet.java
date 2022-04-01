package api.console.web.servlets;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.InvalidArgumentException;
import com.geekhub.models.ResourseType;
import com.geekhub.mylogger.LoggerType;
import com.geekhub.mylogger.MyLogger;
import com.geekhub.services.ResourseService;
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
import java.util.Objects;

import static java.util.Objects.isNull;

@WebServlet(name = "ResourseMenuServlet", urlPatterns = {"/additional_material"})
public class ResourseMenuServlet extends HttpServlet {
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
        out.println("<h1> <p align=\"center\">Welcome to Additional material menu!</p><br>");
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var resourseService = applicationContext.getBean(ResourseService.class);
        int index = extractResourceIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + resourseService.getResourse(index) + "</h2>");
        }
        return;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var resourseService = applicationContext.getBean(ResourseService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        String name = extractResourceTask(req, "resource-name");
        String data = extractResourceTask(req, "data");
        String type = extractResourceTask(req, "type");
        if(!type.equals(String.valueOf(ResourseType.BOOK)) && !type.equals(String.valueOf(ResourseType.URL))
                && !type.equals(String.valueOf(ResourseType.VIDEO))) {
            logger.log(LoggerType.ERROR, InvalidArgumentException.class,
                    "The type should be BOOK, URL or VIDEO!");
            throw new IllegalArgumentException("The type should be BOOK, URL or VIDEO!");
        }
        resourseService.createResourse(name, data, type);
        printPeople(resp, resourseService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var resourseService = applicationContext.getBean(ResourseService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractResourceIndex(req, "index");
        resourseService.deleteResourse(index);
        printPeople(resp, resourseService);
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

    private void printPeople(HttpServletResponse resp, ResourseService resourseService) throws IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter()) {
            out.println("<h2>" + resourseService.showResources() + "</h2>");
        }
        return;
    }

    private int extractResourceIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private String extractResourceTask(HttpServletRequest request, String parameter) throws IllegalArgumentException, FileNotFoundException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            logger.log(LoggerType.ERROR, IllegalArgumentException.class, "The field is empty!");
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
