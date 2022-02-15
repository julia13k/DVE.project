package api.console.web.servlets;

import com.geekhub.models.Lection;
import com.geekhub.services.LectionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private LectionService lectionService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.lectionService = new LectionService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to Lecture menu!</p><br>");
        out.close();
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
        int index = extractLectureIndex(req, "index");
        try(PrintWriter out = resp.getWriter()) {
            out.print("<h2>" + lectionService.getLection(index).getName() + "</h2>");
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
        String lectureName = extractLectureName(req, "lecture-name");
        String description = extractLectureName(req, "description");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss:SS");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+2"));
        ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.of("UTC+2"));
        Lection lection = lectionService.createLection(lectureName, description);
        lection.setCreationDate(zonedDateTime);
        printLectures(resp, lectionService);
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String currentUserLogin = (String) session.getAttribute("user-name");
        if(!Objects.equals(currentUserLogin, "admin")) {
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

    private static int extractLectureIndex(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        int value = Integer.parseInt(request.getParameter(parameter));
        return value;
    }

    private static String extractLectureName(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String value = request.getParameter(parameter);
        if(isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
