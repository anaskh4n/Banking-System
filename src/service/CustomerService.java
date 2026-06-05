package service;
import dao.CustomerDAO;
import model.Customer;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private final CustomerDAO dao = new CustomerDAO();

    public boolean addCustomer(Customer c) throws SQLException {
        if (c.getFullName().isEmpty() || c.getCnic().isEmpty() || c.getPhone().isEmpty())
            throw new IllegalArgumentException("Name, CNIC, and Phone are required.");
        return dao.add(c);
    }

    public boolean updateCustomer(Customer c) throws SQLException {
        if (c.getCustomerId() <= 0) throw new IllegalArgumentException("Invalid customer ID.");
        return dao.update(c);
    }

    public boolean deleteCustomer(int id) throws SQLException { return dao.delete(id); }
    public Customer getCustomer(int id) throws SQLException { return dao.getById(id); }
    public List<Customer> search(String term) throws SQLException { return dao.search(term); }
    public List<Customer> getAllCustomers() throws SQLException { return dao.getAll(); }
}
