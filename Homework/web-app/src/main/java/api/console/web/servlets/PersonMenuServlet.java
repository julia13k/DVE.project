package api.console.web.servlets;

import com.geekhub.exceptions.InvalidArgumentException;
import com.geekhub.models.Role;
import com.geekhub.services.PersonService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static java.util.Objects.isNull;

@WebServlet(name = "PersonMenuServlet", urlPatterns = {"/person"})
public class PersonMenuServlet extends HttpServlet {
    private PersonService personService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.personService = new PersonService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to Person menu!</p><br>");
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = extractPersonIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + personService.getPerson(index) + "</h2>");
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
        String firstname = extractPersonParameter(req, "firstname");
        String lastname = extractPersonParameter(req, "lastname");
        String contacts = extractPersonParameter(req, "contacts");
        String email = extractPersonParameter(req, "email");
        String role = extractPersonParameter(req, "role");
        if(!role.equals(String.valueOf(Role.TEACHER)) || !role.equals(String.valueOf(Role.STUDENT))) {
            throw new InvalidArgumentException("The role should be TEACHER or STUDENT!");
        }
        personService.createPerson(firstname, lastname, contacts, email, role);
        printPeople(resp, personService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractPersonIndex(req, "index");
        personService.deletePerson(index);
        printPeople(resp, personService);
        return;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            super.service(req,res);
        } catch (AccessDeniedException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        } catch (InvalidArgumentException | IllegalArgumentException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        } catch (ServletException | IOException e){
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong!");
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

    private static int extractPersonIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private static String extractPersonParameter(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
