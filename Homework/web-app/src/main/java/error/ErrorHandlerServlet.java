package error;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;

@WebServlet(name = "ErrorHandlerServlet", urlPatterns = {"/error-handler"})
public class ErrorHandlerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        try (PrintWriter writer = resp.getWriter()){
            writer.write("<html><head><title>Error</title></head><body>");
            writer.write("<h2>Something went wrong! Your request to this page failed</h2>");
            writer.write("<url>");
            writer.write("<li>The reason is " + req.getAttribute(ERROR_MESSAGE) + " </li>");
            writer.write("<li>Status code: " + req.getAttribute(ERROR_STATUS_CODE) + " </li>");
            writer.write("</url>");
            writer.write("</body></html>");
            writer.flush();
        }
    }
}
