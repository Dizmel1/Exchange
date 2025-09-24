package servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDAO;
import dao.ExchangeRateDAO;
import entities.Currency;
import entities.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/exchangeRates")
public class ExCurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO dao = new CurrencyDAO();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            List<ExchangeRate> exchangeRates = dao.exchangeRates();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(exchangeRates);
            resp.setStatus(200);
            resp.getWriter().write(json);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ExchangeRateDAO dao = new ExchangeRateDAO();
        CurrencyDAO dao2 = new CurrencyDAO();
        try{
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            float rate = Float.parseFloat(req.getParameter("rate"));
            Currency base = dao2.getCurrencyByCode(baseCurrencyCode);
            Currency target = dao2.getCurrencyByCode(targetCurrencyCode);
            ExchangeRate exchangeRate = new ExchangeRate(base, target, rate);
            dao.postExchange(exchangeRate);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

