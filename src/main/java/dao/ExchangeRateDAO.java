package dao;

import dto.AmountDTO;
import entities.ExchangeRate;

import java.sql.*;

public class ExchangeRateDAO {
    static private final String jdbcUrl = "jdbc:mysql://localhost:3306/exchange";
    static private final String username = "root";
    static private final String password = "niwryb-cowvyx-Temny3";
    public void postExchange(ExchangeRate exchangeRate) throws ClassNotFoundException, SQLException {
        String insertSQl = "insert into exchangerates (BaseCurrencyId, TargetCurrencyId, Rate) value (?, ?, ?)";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            try (PreparedStatement pstm = connection.prepareStatement(insertSQl)){
                pstm.setInt(1, exchangeRate.getBaseCurrency().getId());
                pstm.setInt(2, exchangeRate.getTargetCurrency().getId());
                pstm.setFloat(3, exchangeRate.getRate());
                pstm.executeUpdate();
            }
        }
    }
    public void patchExchange(ExchangeRate exchangeRate) throws ClassNotFoundException, SQLException {
        String updateSQL = "update exchangerates er join currencies base ON er.BaseCurrencyId = base.id join currencies target ON er.TargetCurrencyId = target.id set er.Rate = ? where base.code = ? and target.code = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            try(PreparedStatement pstm = connection.prepareStatement(updateSQL)){
                pstm.setFloat(1, exchangeRate.getRate());
                pstm.setString(2, exchangeRate.getBaseCurrency().getCode());
                pstm.setString(3, exchangeRate.getTargetCurrency().getCode());
                pstm.executeUpdate();
            }
        }
    }
    public AmountDTO convertedAmount (float amount, String from, String to) throws ClassNotFoundException, SQLException {
        String selectSQL = "select er.Rate * ? AS convertedAmount\n" +
                "    from exchangerates er\n" +
                "    join currencies base on er.BaseCurrencyId = base.id\n" +
                "    join currencies target on er.TargetCurrencyId = target.id\n" +
                "    where base.code = ? and target.code = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            try (PreparedStatement pstm = connection.prepareStatement(selectSQL)){
                pstm.setFloat(1, amount);
                pstm.setString(2, from);
                pstm.setString(3, to);
                try(ResultSet rs = pstm.executeQuery()){
                    if (rs.next()){
                        float convertedAmount = rs.getFloat( "convertedAmount");
                        return new AmountDTO(convertedAmount);
                    }
                }
            }
        }
        return null;
    }
}
