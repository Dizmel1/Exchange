package servlets;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/*"})
public class ServletsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
                HttpServletResponse res = (HttpServletResponse) servletResponse;
                res.setHeader("Access-Control-Allow-Origin", "http://localhost:63342");
                res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
                res.setHeader("Access-Control-Allow-Credentials", "true");
                servletRequest.setCharacterEncoding("UTF-8");
                servletResponse.setContentType("application/json; charset=UTF-8");
                filterChain.doFilter(servletRequest, servletResponse);
            }
}