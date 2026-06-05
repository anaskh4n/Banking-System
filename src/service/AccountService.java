package service;
import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.Transaction;
import util.DBConnection;
import java.sql.*;
import java.util.List;

public class AccountService {
    private final AccountDAO dao = new AccountDAO();
    private final TransactionDAO txDao = new TransactionDAO();

    public String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis() % 10000000;
    }

    public boolean createAccount(Account a) throws SQLException {
        if (a.getAccountNumber().isEmpty()) throw new IllegalArgumentException("Account number required.");
        if (dao.getByNumber(a.getAccountNumber()) != null) throw new IllegalArgumentException("Account number already exists.");
        return dao.create(a);
    }

    public boolean updateAccount(Account a) throws SQLException { return dao.update(a); }
    public boolean deleteAccount(String n) throws SQLException { return dao.delete(n); }
    public Account getAccount(String n) throws SQLException { return dao.getByNumber(n); }
    public List<Account> search(String t) throws SQLException { return dao.search(t); }
    public List<Account> getAllAccounts() throws SQLException { return dao.getAll(); }
    public List<Account> getByBranch(int branchId) throws SQLException { return dao.getByBranch(branchId); }
    public List<Account> getByCustomer(int custId) throws SQLException { return dao.getByCustomer(custId); }

    public void deposit(String accNum, double amount) throws SQLException {
        Account a = dao.getByNumber(accNum);
        if (a == null) throw new IllegalArgumentException("Account not found.");
        if (!a.getStatus().equals("Active")) throw new IllegalArgumentException("Account is not active.");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        dao.updateBalance(accNum, a.getBalance() + amount);
        txDao.add(new Transaction(null, accNum, amount, "Deposit"));
    }

    public void withdraw(String accNum, double amount) throws SQLException {
        Account a = dao.getByNumber(accNum);
        if (a == null) throw new IllegalArgumentException("Account not found.");
        if (!a.getStatus().equals("Active")) throw new IllegalArgumentException("Account is not active.");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (a.getBalance() < amount) throw new IllegalArgumentException("Insufficient balance.");
        dao.updateBalance(accNum, a.getBalance() - amount);
        txDao.add(new Transaction(accNum, null, amount, "Withdrawal"));
    }

    public void transfer(String from, String to, double amount) throws SQLException {
        Connection conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            Account fa = dao.getByNumber(from);
            Account ta = dao.getByNumber(to);
            if (fa == null || ta == null) throw new IllegalArgumentException("Account not found.");
            if (!fa.getStatus().equals("Active") || !ta.getStatus().equals("Active"))
                throw new IllegalArgumentException("Both accounts must be active.");
            if (fa.getBalance() < amount) throw new IllegalArgumentException("Insufficient balance.");
            if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
            dao.updateBalance(from, fa.getBalance() - amount);
            dao.updateBalance(to, ta.getBalance() + amount);
            txDao.add(new Transaction(from, to, amount, "Transfer"));
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean openAccount(String n) throws SQLException { return dao.updateStatus(n,"Active"); }
    public boolean closeAccount(String n) throws SQLException { return dao.updateStatus(n,"Closed"); }
    public boolean freezeAccount(String n) throws SQLException { return dao.updateStatus(n,"Frozen"); }
    public boolean reactivateAccount(String n) throws SQLException { return dao.updateStatus(n,"Active"); }
}
