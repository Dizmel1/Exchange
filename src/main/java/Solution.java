import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:3306/exchange";
        String username = "root";
        String password = "niwryb-cowvyx-Temny3";
        String selectSQL = "select * from currencies";
        String insertCurSQL = "insert into currencies (code, full_name, sign) values (?, ?, ?)";
        String insertExcSQL = "insert into exchangerates (BaseCurrencyId, TargetCurrencyId, Rate) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            try (PreparedStatement pstm = connection.prepareStatement(selectSQL)){
                ResultSet rs = pstm.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("id");
                    String code = rs.getString("code");
                    String full_name = rs.getString("full_name");
                    String sign = rs.getString("sign");
                    Currencies currencies = new Currencies(id, code, full_name, sign);
                    System.out.println(currencies);
                }
            }
//            try (PreparedStatement pstm = connection.prepareStatement(insertExcSQL)){
//                pstm.setInt(1, 15);
//                pstm.setInt(2, 2);
//                pstm.setDouble(3, 1.97);
//                pstm.executeUpdate();
//            }
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }
    public static class Currencies {
        private int id;
        private String code;
        private String full_name;
        private String sign;

        public Currencies(int id, String code, String full_name, String sign) {
            this.id = id;
            this.code = code;
            this.full_name = full_name;
            this.sign = sign;
        }
        public Currencies(String code, String full_name, String sign){
            this.code = code;
            this.full_name = full_name;
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "Currencies{" +
                    "id=" + id +
                    ", code='" + code + '\'' +
                    ", full_name='" + full_name + '\'' +
                    ", sign='" + sign + '\'' +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFullName() {
            return full_name;
        }

        public void setFullName(String full_name) {
            this.full_name = full_name;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}

