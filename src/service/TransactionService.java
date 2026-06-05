package service;
import dao.TransactionDAO;
import model.Transaction;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDAO dao = new TransactionDAO();
    public List<Transaction> getByAccount(String accNum) throws SQLException { return dao.getByAccount(accNum); }
    public List<Transaction> getAll() throws SQLException { return dao.getAll(); }
}
