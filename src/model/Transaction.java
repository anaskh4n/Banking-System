package model;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private String fromAccount, toAccount, type;
    private double amount;
    private Timestamp dateTime;

    public Transaction() {}
    public Transaction(String fromAccount, String toAccount, double amount, String type) {
        this.fromAccount = fromAccount; this.toAccount = toAccount;
        this.amount = amount; this.type = type;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int v) { transactionId = v; }
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String v) { fromAccount = v; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String v) { toAccount = v; }
    public String getType() { return type; }
    public void setType(String v) { type = v; }
    public double getAmount() { return amount; }
    public void setAmount(double v) { amount = v; }
    public Timestamp getDateTime() { return dateTime; }
    public void setDateTime(Timestamp v) { dateTime = v; }
}
