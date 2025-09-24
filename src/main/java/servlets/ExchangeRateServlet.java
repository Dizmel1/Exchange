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

@WebServlet(urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO dao = new CurrencyDAO();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() < 7) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Path should be /exchangeRate/{base}{target}, e.g. /exchangeRate/AUDUSD\"}");
                return;
            }
            String pair = pathInfo.substring(1).toUpperCase();
            String baseCode = pair.substring(0, 3);
            String targetCode = pair.substring(3);

            ExchangeRate exchangeRate = dao.getExchangeRateByCodes(baseCode, targetCode);
            if (exchangeRate == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Exchange rate not found\"}");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(exchangeRate);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);

        } catch (SQLException | ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Internal server error\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getPathInfo());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ExchangeRateDAO dao = new ExchangeRateDAO();
        CurrencyDAO dao2 = new CurrencyDAO();
        String baseCurrencyCode = req.getPathInfo().substring(1).toUpperCase().substring(0,3);
        String targetCurrencyCode = req.getPathInfo().substring(1).toUpperCase().substring(3);
        try{
            float rate = Float.parseFloat(req.getParameter("rate"));
            Currency base = dao2.getCurrencyByCode(baseCurrencyCode);
            Currency target = dao2.getCurrencyByCode(targetCurrencyCode);
            ExchangeRate exchangeRate = new ExchangeRate(rate, base, target);
            dao.patchExchange(exchangeRate);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
//package servlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dao.CurrencyDAO;
//import entities.ExchangeRate;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//@WebServlet(urlPatterns = "/exchangeRate/RUBUAH")
//public class GetRUBUAH extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        CurrencyDAO dao = new CurrencyDAO();
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        try{
//            ExchangeRate exchangeRate = dao.exchangeRateCode(2);
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(exchangeRate);
//            resp.setStatus(200);
//            resp.getWriter().write(json);
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
