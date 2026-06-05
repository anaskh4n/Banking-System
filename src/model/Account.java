package model;
import java.sql.Timestamp;

public class Account {
    private String accountNumber;
    private int customerId, branchId;
    private String accountType, status;
    private double balance;
    private Timestamp createdAt;

    public Account() {}
    public Account(String accountNumber, int customerId, String accountType, double balance, int branchId, String status) {
        this.accountNumber = accountNumber; this.customerId = customerId;
        this.accountType = accountType; this.balance = balance;
        this.branchId = branchId; this.status = status;
    }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String v) { accountNumber = v; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int v) { customerId = v; }
    public int getBranchId() { return branchId; }
    public void setBranchId(int v) { branchId = v; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String v) { accountType = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public double getBalance() { return balance; }
    public void setBalance(double v) { balance = v; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp v) { createdAt = v; }
}
