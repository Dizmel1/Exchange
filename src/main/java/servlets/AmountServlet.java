package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDAO;
import dao.ExchangeRateDAO;
import dto.AmountDTO;
import entities.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/exchange")
public class AmountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateDAO dao = new ExchangeRateDAO();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try{
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            float amount = Float.parseFloat(req.getParameter("amount"));
            AmountDTO dto = dao.convertedAmount(amount, from, to);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(dto);
            resp.setStatus(200);
            resp.getWriter().write(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
