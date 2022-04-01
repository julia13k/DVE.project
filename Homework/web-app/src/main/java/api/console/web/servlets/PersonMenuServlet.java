package api.console.web.servlets;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.InvalidArgumentException;
import com.geekhub.models.Role;
import com.geekhub.mylogger.LoggerType;
import com.geekhub.mylogger.MyLogger;
import com.geekhub.services.PersonService;
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

@WebServlet(name = "PersonMenuServlet", urlPatterns = {"/person"})
public class PersonMenuServlet extends HttpServlet {
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
        out.println("<h1> <p align=\"center\">Welcome to Person menu!</p><br>");
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var personService = applicationContext.getBean(PersonService.class);
        int index = extractPersonIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + personService.getPerson(index) + "</h2>");
        }
        return;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var personService = applicationContext.getBean(PersonService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        String firstname = extractPersonParameter(req, "firstname");
        String lastname = extractPersonParameter(req, "lastname");
        String contacts = extractPersonParameter(req, "contacts");
        String email = extractPersonParameter(req, "email");
        String role = extractPersonParameter(req, "role");
        if(!role.equals(String.valueOf(Role.TEACHER)) && !role.equals(String.valueOf(Role.STUDENT))) {
            logger.log(LoggerType.ERROR, InvalidArgumentException.class,
                    "The role should be TEACHER or STUDENT!");
            throw new InvalidArgumentException("The role should be TEACHER or STUDENT!");
        }
        personService.createPerson(firstname, lastname, contacts, email, role);
        printPeople(resp, personService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var personService = applicationContext.getBean(PersonService.class);
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            logger.log(LoggerType.ERROR, AccessDeniedException.class,
                    "Access denied. Required access rights: 'admin'");
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractPersonIndex(req, "index");
        personService.deletePerson(index);
        printPeople(resp, personService);
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

    private void printPeople(HttpServletResponse resp, PersonService personService) throws IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter()) {
            out.println("<h2>" + personService.showPeople() + "</h2>");
        }
        return;
    }

    private int extractPersonIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private String extractPersonParameter(HttpServletRequest request, String parameter) throws IllegalArgumentException, FileNotFoundException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            logger.log(LoggerType.ERROR, IllegalArgumentException.class, "The field is empty!");
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
