package dao;

import entities.Currency;
import entities.ExchangeRate;

import java.io.BufferedReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrencyDAO {
    static private final String jdbcUrl = "jdbc:mysql://localhost:3306/exchange";
    static private final String username = "root";
    static private final String password = "niwryb-cowvyx-Temny3";
    public List<Currency> getAllCurrencies() throws SQLException, ClassNotFoundException {
        String selectSQL = "select * from currencies";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            List<Currency> list = new ArrayList<>();
            try (PreparedStatement pstm = connection.prepareStatement(selectSQL)){
                ResultSet rs = pstm.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("id");
                    String code = rs.getString("code");
                    String full_name = rs.getString("full_name");
                    String sign = rs.getString("sign");
                    Currency currency = new Currency(id, code, full_name, sign);
                    list.add(currency);
                }
            }
            return list;
        }
    }
    public Currency getCurrencyByCode(String codeR) throws ClassNotFoundException, SQLException {
        String selectSQL = "select * from currencies where code = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            Currency currency = null;
            try(PreparedStatement pstm = connection.prepareStatement(selectSQL)){
                pstm.setString(1, codeR);
                ResultSet rs = pstm.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("id");
                    String code = rs.getString("code");
                    String full_name = rs.getString("full_name");
                    String sign = rs.getString("sign");
                    currency = new Currency(id, code, full_name, sign);
                }
            }
            return currency;
        }
    }
    public ExchangeRate getExchangeRateByCodes(String baseCode, String targetCode) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM exchangerates " +
                "JOIN currencies base ON exchangerates.BaseCurrencyId = base.id " +
                "JOIN currencies target ON exchangerates.TargetCurrencyId = target.id " +
                "WHERE base.code = ? AND target.code = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, baseCode);
            pstm.setString(2, targetCode);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("base.id");
                    String code1 = rs.getString("base.code");
                    String fullName = rs.getString("base.full_name");
                    String sign = rs.getString("base.sign");
                    Currency baseCurrency = new Currency(id, code1, fullName, sign);
                    int id1 = rs.getInt("target.id");
                    String code0 = rs.getString("target.code");
                    String fullName1 = rs.getString("target.full_name");
                    String sign1 = rs.getString("target.sign");
                    Currency targetCurrency = new Currency(id1, code0, fullName1, sign1);
                    int exId = rs.getInt("exchangerates.id");
                    float rate = rs.getFloat("exchangerates.rate");
                    return new ExchangeRate(exId, baseCurrency, targetCurrency, rate);
                }
            }
        }
        return null;
    }

    public List<ExchangeRate> exchangeRates() throws SQLException, ClassNotFoundException {
        List<ExchangeRate> list = new ArrayList<>();
        String selectSQL = "select * from exchangerates join currencies base on ExchangeRates.BaseCurrencyId = base.id join currencies target on ExchangeRates.TargetCurrencyId = target.id";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            try (PreparedStatement pstm = connection.prepareStatement(selectSQL)){
                ResultSet rs = pstm.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("base.id");
                    String code = rs.getString("base.code");
                    String full_name = rs.getString("base.full_name");
                    String sign = rs.getString("base.sign");
                    Currency baseCurrency = new Currency(id, code, full_name, sign);
                    int id1 = rs.getInt("target.id");
                    String code1 = rs.getString("target.code");
                    String full_name1 = rs.getString("target.full_name");
                    String sign1 = rs.getString("target.sign");
                    Currency targetCurrency = new Currency(id1, code1, full_name1, sign1);
                    int exId = rs.getInt("exchangerates.id");
                    float rate = rs.getFloat("exchangerates.rate");
                    ExchangeRate exchangeRate = new ExchangeRate(exId, baseCurrency, targetCurrency, rate);
                    list.add(exchangeRate);
                }
            }
        }
        return list;
    }
    public void postCurrency(Currency currency) throws ClassNotFoundException, SQLException {
        String insertSQL = "insert into currencies (code, full_name, sign) values (?, ?, ?)";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            try (PreparedStatement pstm = connection.prepareStatement(insertSQL)){
                pstm.setString(1, currency.getCode());
                pstm.setString(2, currency.getName());
                pstm.setString(3, currency.getSign());
                pstm.executeUpdate();
            }
        }
    }
}

/*"ExchangeRate{" +
        "id=" + currency2.getId() +
        "baseCurrency=" + '{' +
        "id=" + currency.getId() +
        "name=" + currency.getFullName() +
        "code=" + currency.getCode() +
        "sign=" + currency.getSign() +
        '}' +
        "targetCurrency=" + '{' +
        "id=" + currency2.getId() +
        "name=" + currency2.getFullName() +
        "code=" + currency2.getCode() +
        "sign=" + currency2.getSign() +
        '}' +
        "rate=" + currency2.getRate() +
        '}';
 */