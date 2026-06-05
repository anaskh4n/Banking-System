package dao;
import model.Account;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class AccountDAO {
    public boolean create(Account a) throws SQLException {
        String sql = "INSERT INTO accounts (account_number,customer_id,account_type,balance,branch_id,status) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1,a.getAccountNumber()); ps.setInt(2,a.getCustomerId());
            ps.setString(3,a.getAccountType()); ps.setDouble(4,a.getBalance());
            ps.setInt(5,a.getBranchId()); ps.setString(6,a.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Account a) throws SQLException {
        String sql = "UPDATE accounts SET customer_id=?,account_type=?,branch_id=?,status=? WHERE account_number=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1,a.getCustomerId()); ps.setString(2,a.getAccountType());
            ps.setInt(3,a.getBranchId()); ps.setString(4,a.getStatus());
            ps.setString(5,a.getAccountNumber());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String accNum) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM accounts WHERE account_number=?")) {
            ps.setString(1,accNum); return ps.executeUpdate() > 0;
        }
    }

    public Account getByNumber(String accNum) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM accounts WHERE account_number=?")) {
            ps.setString(1,accNum);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    public List<Account> search(String term) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number LIKE ? OR customer_id LIKE ? OR account_type LIKE ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            String t = "%" + term + "%";
            ps.setString(1,t); ps.setString(2,t); ps.setString(3,t);
            return list(ps.executeQuery());
        }
    }

    public List<Account> getByCustomer(int custId) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM accounts WHERE customer_id=?")) {
            ps.setInt(1,custId); return list(ps.executeQuery());
        }
    }

    public List<Account> getAll() throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM accounts ORDER BY created_at DESC")) {
            return list(ps.executeQuery());
        }
    }

    public List<Account> getByBranch(int branchId) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM accounts WHERE branch_id=?")) {
            ps.setInt(1,branchId); return list(ps.executeQuery());
        }
    }

    public boolean updateBalance(String accNum, double balance) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("UPDATE accounts SET balance=? WHERE account_number=?")) {
            ps.setDouble(1,balance); ps.setString(2,accNum);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(String accNum, String status) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("UPDATE accounts SET status=? WHERE account_number=?")) {
            ps.setString(1,status); ps.setString(2,accNum);
            return ps.executeUpdate() > 0;
        }
    }

    private Account map(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.setAccountNumber(rs.getString("account_number"));
        a.setCustomerId(rs.getInt("customer_id"));
        a.setAccountType(rs.getString("account_type"));
        a.setBalance(rs.getDouble("balance"));
        a.setBranchId(rs.getInt("branch_id"));
        a.setStatus(rs.getString("status"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        return a;
    }

    private List<Account> list(ResultSet rs) throws SQLException {
        List<Account> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }
}
