package api.console.web.servlets;

import com.geekhub.services.ResourseService;

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

@WebServlet(name = "ResourseMenuServlet", urlPatterns = {"/additional_material"})
public class ResourseMenuServlet extends HttpServlet {
    private ResourseService resourseService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.resourseService = new ResourseService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to Additional material menu!</p><br>");
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = extractResourceIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + resourseService.getResourse(index) + "</h2>");
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
        String name = extractResourceTask(req, "resource-name");
        String data = extractResourceTask(req, "data");
        String type = extractResourceTask(req, "type");
        if(type.equals("BOOK") || type.equals("URL") || type.equals("VIDEO")) {
            throw new IllegalArgumentException("The role should be BOOK, URL or VIDEO!");
        }
        resourseService.createResourse(name, data, type);
        printPeople(resp, resourseService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
            throw new AccessDeniedException("Access denied. Required access rights: 'admin'");
        }
        int index = extractResourceIndex(req, "index");
        resourseService.deleteResourse(index);
        printPeople(resp, resourseService);
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

    private void printPeople(HttpServletResponse resp, ResourseService resourseService) throws IOException {
        resp.setContentType("text/html");
        try(PrintWriter out = resp.getWriter()) {
            out.println("<h2>" + resourseService.showResources() + "</h2>");
        }
        return;
    }

    private static int extractResourceIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private static String extractResourceTask(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
