package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDAO;
import entities.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// Используем шаблон URL с подстановкой
@WebServlet(urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO dao = new CurrencyDAO();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // достаём часть после /currency/
            String pathInfo = req.getPathInfo(); // например, "/AUD"
            if (pathInfo == null || pathInfo.length() <= 1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Currency code is required\"}");
                return;
            }

            String currencyCode = pathInfo.substring(1).toUpperCase(); // AUD

            Currency currency = dao.getCurrencyByCode(currencyCode);
            if (currency == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Currency not found\"}");
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(currency);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);

        } catch (SQLException | ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Internal server error\"}");
        }
    }
}
