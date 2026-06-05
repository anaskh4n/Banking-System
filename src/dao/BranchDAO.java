package dao;
import model.Branch;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class BranchDAO {
    public boolean add(Branch b) throws SQLException {
        String sql = "INSERT INTO branches (branch_name,city,manager_name,contact) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1,b.getBranchName()); ps.setString(2,b.getCity());
            ps.setString(3,b.getManagerName()); ps.setString(4,b.getContact());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Branch b) throws SQLException {
        String sql = "UPDATE branches SET branch_name=?,city=?,manager_name=?,contact=? WHERE branch_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1,b.getBranchName()); ps.setString(2,b.getCity());
            ps.setString(3,b.getManagerName()); ps.setString(4,b.getContact());
            ps.setInt(5,b.getBranchId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM branches WHERE branch_id=?")) {
            ps.setInt(1,id); return ps.executeUpdate() > 0;
        }
    }

    public Branch getById(int id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM branches WHERE branch_id=?")) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        }
    }

    public List<Branch> getAll() throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM branches ORDER BY branch_id")) {
            return list(ps.executeQuery());
        }
    }

    private Branch map(ResultSet rs) throws SQLException {
        Branch b = new Branch();
        b.setBranchId(rs.getInt("branch_id"));
        b.setBranchName(rs.getString("branch_name"));
        b.setCity(rs.getString("city"));
        b.setManagerName(rs.getString("manager_name"));
        b.setContact(rs.getString("contact"));
        return b;
    }

    private List<Branch> list(ResultSet rs) throws SQLException {
        List<Branch> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }
}
