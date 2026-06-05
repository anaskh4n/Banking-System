package dao;
import model.Customer;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class CustomerDAO {
    public boolean add(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (full_name,cnic,phone,email,address) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getFullName()); ps.setString(2, c.getCnic());
            ps.setString(3, c.getPhone()); ps.setString(4, c.getEmail());
            ps.setString(5, c.getAddress());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Customer c) throws SQLException {
        String sql = "UPDATE customers SET full_name=?,cnic=?,phone=?,email=?,address=? WHERE customer_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getFullName()); ps.setString(2, c.getCnic());
            ps.setString(3, c.getPhone()); ps.setString(4, c.getEmail());
            ps.setString(5, c.getAddress()); ps.setInt(6, c.getCustomerId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM customers WHERE customer_id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        }
    }

    public Customer getById(int id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM customers WHERE customer_id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    public List<Customer> search(String term) throws SQLException {
        String sql = "SELECT * FROM customers WHERE full_name LIKE ? OR cnic LIKE ? OR phone LIKE ? OR email LIKE ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            String t = "%" + term + "%";
            ps.setString(1,t); ps.setString(2,t); ps.setString(3,t); ps.setString(4,t);
            return list(ps.executeQuery());
        }
    }

    public List<Customer> getAll() throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM customers ORDER BY customer_id")) {
            return list(ps.executeQuery());
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setFullName(rs.getString("full_name"));
        c.setCnic(rs.getString("cnic"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setDateCreated(rs.getTimestamp("date_created"));
        return c;
    }

    private List<Customer> list(ResultSet rs) throws SQLException {
        List<Customer> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }
}
