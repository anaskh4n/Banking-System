package view;

import model.Customer;
import service.CustomerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private final CustomerService svc = new CustomerService();
    private JTextField idField, nameField, cnicField, phoneField, emailField, addressField, searchField;
    private DefaultTableModel tableModel;
    private JTable table;

    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10)); // Increased spacing
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Better padding
        add(buildForm(), BorderLayout.WEST);
        add(buildTable(), BorderLayout.CENTER);
        loadAll();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setPreferredSize(new Dimension(320, 0)); // Slightly wider for better text fit
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Customer Details"));
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5); // Better spacing between form elements
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        idField = addRow(p, g, 0, "Customer ID:", true);
        nameField = addRow(p, g, 1, "Full Name:", false);
        cnicField = addRow(p, g, 2, "CNIC:", false);
        phoneField = addRow(p, g, 3, "Phone:", false);
        emailField = addRow(p, g, 4, "Email:", false);
        addressField = addRow(p, g, 5, "Address:", false);

        g.gridy = 6; g.gridx = 0; g.gridwidth = 2;
        JPanel btns = new JPanel(new GridLayout(3, 2, 5, 5));
        
        // Applying lighter pastel shades for readability with black text
        JButton addBtn = btn("Add", new Color(165, 214, 167));
        JButton updateBtn = btn("Update", new Color(144, 202, 249));
        JButton deleteBtn = btn("Delete", new Color(239, 154, 154));
        JButton clearBtn = btn("Clear", new Color(245, 245, 245));
        JButton listBtn = btn("List All", new Color(225, 190, 231));
        
        btns.add(addBtn); btns.add(updateBtn);
        btns.add(deleteBtn); btns.add(clearBtn);
        btns.add(listBtn);
        p.add(btns, g);

        g.gridy = 7; g.gridwidth = 1; g.gridx = 0; p.add(new JLabel("Search:"), g);
        g.gridx = 1; searchField = new JTextField(); p.add(searchField, g);
        g.gridy = 8; g.gridx = 0; g.gridwidth = 2;
        
        JButton searchBtn = btn("Search", new Color(178, 235, 242));
        p.add(searchBtn, g);

        // Action Listeners
        addBtn.addActionListener(e -> addCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        clearBtn.addActionListener(e -> clearFields());
        listBtn.addActionListener(e -> loadAll());
        searchBtn.addActionListener(e -> search());
        
        return p;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "CNIC", "Phone", "Email", "Address", "Created"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25); // Better table row spacing
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12)); // Distinct table header
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) populateFromRow(table.getSelectedRow());
        });
        return new JScrollPane(table);
    }

    private void addCustomer() {
        try {
            Customer c = getInput();
            if (svc.addCustomer(c)) { JOptionPane.showMessageDialog(this, "Customer added!"); loadAll(); clearFields(); }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void updateCustomer() {
        try {
            if (idField.getText().isEmpty()) { showError("Select a customer first."); return; }
            Customer c = getInput();
            c.setCustomerId(Integer.parseInt(idField.getText()));
            if (svc.updateCustomer(c)) { JOptionPane.showMessageDialog(this, "Customer updated!"); loadAll(); clearFields(); }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void deleteCustomer() {
        try {
            if (idField.getText().isEmpty()) { showError("Select a customer first."); return; }
            int id = Integer.parseInt(idField.getText());
            if (JOptionPane.showConfirmDialog(this, "Delete customer #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                if (svc.deleteCustomer(id)) { JOptionPane.showMessageDialog(this, "Deleted!"); loadAll(); clearFields(); }
            }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void search() {
        try {
            List<Customer> list = svc.search(searchField.getText().trim());
            fillTable(list);
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void loadAll() {
        try { fillTable(svc.getAllCustomers()); } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void fillTable(List<Customer> list) {
        tableModel.setRowCount(0);
        for (Customer c : list)
            tableModel.addRow(new Object[]{c.getCustomerId(), c.getFullName(), c.getCnic(), c.getPhone(), c.getEmail(), c.getAddress(), c.getDateCreated()});
    }

    private void populateFromRow(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(str(tableModel.getValueAt(row, 1)));
        cnicField.setText(str(tableModel.getValueAt(row, 2)));
        phoneField.setText(str(tableModel.getValueAt(row, 3)));
        emailField.setText(str(tableModel.getValueAt(row, 4)));
        addressField.setText(str(tableModel.getValueAt(row, 5)));
    }

    private Customer getInput() {
        Customer c = new Customer();
        c.setFullName(nameField.getText().trim());
        c.setCnic(cnicField.getText().trim());
        c.setPhone(phoneField.getText().trim());
        c.setEmail(emailField.getText().trim());
        c.setAddress(addressField.getText().trim());
        return c;
    }

    private void clearFields() {
        idField.setText(""); nameField.setText(""); cnicField.setText("");
        phoneField.setText(""); emailField.setText(""); addressField.setText("");
        table.clearSelection();
    }

    private JTextField addRow(JPanel p, GridBagConstraints g, int row, String label, boolean readOnly) {
        g.gridy = row; g.gridx = 0; g.gridwidth = 1; p.add(new JLabel(label), g);
        g.gridx = 1; JTextField f = new JTextField(15);
        if (readOnly) { f.setEditable(false); f.setBackground(new Color(230, 230, 230)); }
        p.add(f, g); return f;
    }

    // UPDATED: Black text, custom background, borders, and hover cursor
    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); 
        b.setForeground(Color.BLACK); // Set text to black
        b.setFocusPainted(false); 
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Adds a pointer hand on hover
        return b;
    }

    private String str(Object o) { return o == null ? "" : o.toString(); }
    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}