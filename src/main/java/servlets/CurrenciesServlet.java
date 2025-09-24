package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import entities.Currency;
import util.ConnectionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO dao = new CurrencyDAO();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            List<Currency> currencies = dao.getAllCurrencies();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(currencies);
            resp.setStatus(200);
            resp.getWriter().write(json);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO dao = new CurrencyDAO();
        try {
            String code = req.getParameter("code");
            String name = req.getParameter("name");
            String sign = req.getParameter("sign");
            Currency currency = new Currency(code, name, sign);
            dao.postCurrency(currency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
