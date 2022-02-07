package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebFilter(urlPatterns = "/login")
public class AuthorizationFilter extends GenericFilter {
    private static final String DELETE_METHOD = "DELETE";
    private static final String PUT_METHOD = "PUT";
    private static final List<String> ADMIN_PROTECTED_METHODS = List.of(DELETE_METHOD, PUT_METHOD);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        String method = req.getMethod();
        if(ADMIN_PROTECTED_METHODS.contains(method)) {
            HttpSession session = req.getSession();
            String userName = (String) session.getAttribute("user-name");
            if(!Objects.equals(userName, "admin")) {
                ((HttpServletResponse) response).sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, "Access denied. Required access rights: 'admin'");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
