package dao;
import model.Transaction;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class TransactionDAO {
    public boolean add(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (from_account,to_account,amount,type) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1,t.getFromAccount()); ps.setString(2,t.getToAccount());
            ps.setDouble(3,t.getAmount()); ps.setString(4,t.getType());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Transaction> getByAccount(String accNum) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE from_account=? OR to_account=? ORDER BY date_time DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1,accNum); ps.setString(2,accNum);
            return list(ps.executeQuery());
        }
    }

    public List<Transaction> getAll() throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM transactions ORDER BY date_time DESC")) {
            return list(ps.executeQuery());
        }
    }

    private Transaction map(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setFromAccount(rs.getString("from_account"));
        t.setToAccount(rs.getString("to_account"));
        t.setAmount(rs.getDouble("amount"));
        t.setType(rs.getString("type"));
        t.setDateTime(rs.getTimestamp("date_time"));
        return t;
    }

    private List<Transaction> list(ResultSet rs) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }
}
