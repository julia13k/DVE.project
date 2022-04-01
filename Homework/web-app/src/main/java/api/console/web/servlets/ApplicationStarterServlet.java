package api.console.web.servlets;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.mylogger.MyLogger;
import com.geekhub.services.CourseService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Properties;

import static java.util.Objects.isNull;

@WebServlet(name = "ApplicationStarterServlet", urlPatterns = {"/"})
public class ApplicationStarterServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1> <p align=\"center\">Welcome to application menu!</p><br>" +
                "<p align=\"center\">1 - Course menu</p><br>" +
                "            <p align=\"center\">2 - Lections menu</p><br>" +
                "            <p align=\"center\">3 - Person menu</p><br>" +
                "            <p align=\"center\">4 - Additional material menu</p><br>" +
                "            <p align=\"center\">5 - Homework menu</p><br>" +
                "            <p align=\"center\">6 - Logger menu</p><br>" +
                "            <p align=\"center\">7 - Exit</p><br></h>");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var logger = applicationContext.getBean(MyLogger.class);
        HttpSession session = req.getSession();
        String saveLogsIn = extractParameter(req, "save-in");
        logger.setStorageType(saveLogsIn);
//        FileInputStream fileInputStream = new FileInputStream("Homework/domain/src/main/resources/application.properties");
//        Properties property = new Properties();
//        property.load(fileInputStream);
//        property.setProperty("logger.storage.type", saveLogsIn);
        return;
    }

    private static String extractParameter(HttpServletRequest request, String parameter) throws IllegalArgumentException {
        String value = request.getParameter(parameter);
        if (isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("The field is empty!");
        }
        return value;
    }
}
