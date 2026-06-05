package model;
import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private String fullName, cnic, phone, email, address;
    private Timestamp dateCreated;

    public Customer() {}
    public Customer(String fullName, String cnic, String phone, String email, String address) {
        this.fullName = fullName; this.cnic = cnic; this.phone = phone;
        this.email = email; this.address = address;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int v) { customerId = v; }
    public String getFullName() { return fullName; }
    public void setFullName(String v) { fullName = v; }
    public String getCnic() { return cnic; }
    public void setCnic(String v) { cnic = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { phone = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { address = v; }
    public Timestamp getDateCreated() { return dateCreated; }
    public void setDateCreated(Timestamp v) { dateCreated = v; }
}
