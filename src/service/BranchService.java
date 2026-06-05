package service;
import dao.BranchDAO;
import model.Branch;
import java.sql.SQLException;
import java.util.List;

public class BranchService {
    private final BranchDAO dao = new BranchDAO();
    public boolean addBranch(Branch b) throws SQLException {
        if (b.getBranchName().isEmpty() || b.getCity().isEmpty())
            throw new IllegalArgumentException("Branch name and city required.");
        return dao.add(b);
    }
    public boolean updateBranch(Branch b) throws SQLException { return dao.update(b); }
    public boolean deleteBranch(int id) throws SQLException { return dao.delete(id); }
    public Branch getBranch(int id) throws SQLException { return dao.getById(id); }
    public List<Branch> getAllBranches() throws SQLException { return dao.getAll(); }
}
