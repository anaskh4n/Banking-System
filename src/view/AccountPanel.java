package view;

import model.Account;
import model.Branch;
import service.AccountService;
import service.BranchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccountPanel extends JPanel {
    private final AccountService svc = new AccountService();
    private final BranchService branchSvc = new BranchService();
    private JTextField accNumField, custIdField, balanceField, searchField;
    private JComboBox<String> typeCombo, statusCombo;
    private JComboBox<Branch> branchCombo;
    private DefaultTableModel tableModel;
    private JTable table;

    public AccountPanel() {
        setLayout(new BorderLayout(10, 10)); // Slightly increased spacing
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(buildForm(), BorderLayout.WEST);
        add(buildTable(), BorderLayout.CENTER);
        loadAll();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setPreferredSize(new Dimension(320, 0)); // Slightly wider for better text fit
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Account Details"));
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5); // Better spacing between form elements
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridy = 0; g.gridx = 0; p.add(new JLabel("Account No:"), g);
        g.gridx = 1; accNumField = new JTextField(15); p.add(accNumField, g);

        g.gridy = 1; g.gridx = 0; p.add(new JLabel("Customer ID:"), g);
        g.gridx = 1; custIdField = new JTextField(15); p.add(custIdField, g);

        g.gridy = 2; g.gridx = 0; p.add(new JLabel("Account Type:"), g);
        g.gridx = 1; typeCombo = new JComboBox<>(new String[]{"Saving", "Current"}); p.add(typeCombo, g);

        g.gridy = 3; g.gridx = 0; p.add(new JLabel("Branch:"), g);
        g.gridx = 1; branchCombo = new JComboBox<>(); loadBranches(); p.add(branchCombo, g);

        g.gridy = 4; g.gridx = 0; p.add(new JLabel("Status:"), g);
        g.gridx = 1; statusCombo = new JComboBox<>(new String[]{"Active", "Frozen", "Closed"}); p.add(statusCombo, g);

        g.gridy = 5; g.gridx = 0; p.add(new JLabel("Balance:"), g);
        g.gridx = 1; balanceField = new JTextField("0.00"); p.add(balanceField, g);

        g.gridy = 6; g.gridx = 0; g.gridwidth = 2;
        // Lighter gray for readability with black text
        JButton genBtn = btn("Generate Acc#", new Color(224, 224, 224));
        genBtn.addActionListener(e -> accNumField.setText(svc.generateAccountNumber()));
        p.add(genBtn, g);

        g.gridy = 7; JPanel btns = new JPanel(new GridLayout(2, 2, 5, 5));
        // Lighter shades of Green, Blue, Red, and Gray
        JButton createBtn = btn("Create", new Color(165, 214, 167)); 
        JButton updateBtn = btn("Update", new Color(144, 202, 249)); 
        JButton deleteBtn = btn("Delete", new Color(239, 154, 154)); 
        JButton clearBtn = btn("Clear", new Color(245, 245, 245));  
        btns.add(createBtn); btns.add(updateBtn);
        btns.add(deleteBtn); btns.add(clearBtn);
        p.add(btns, g);

        g.gridy = 8;
        JPanel opBtns = new JPanel(new GridLayout(2, 2, 5, 5));
        // Lighter shades for Operations
        JButton openBtn = btn("Open", new Color(200, 230, 201));
        JButton closeBtn = btn("Close Acc", new Color(248, 187, 208));
        JButton freezeBtn = btn("Freeze", new Color(255, 224, 130));
        JButton reactBtn = btn("Reactivate", new Color(178, 235, 242));
        opBtns.add(openBtn); opBtns.add(closeBtn);
        opBtns.add(freezeBtn); opBtns.add(reactBtn);
        opBtns.setBorder(BorderFactory.createTitledBorder("Operations"));
        p.add(opBtns, g);

        g.gridy = 9; g.gridwidth = 1; g.gridx = 0; p.add(new JLabel("Search:"), g);
        g.gridx = 1; searchField = new JTextField(); p.add(searchField, g);
        g.gridy = 10; g.gridx = 0; g.gridwidth = 2;
        
        JButton searchBtn = btn("Search", new Color(178, 235, 242));
        JButton listBtn = btn("List All", new Color(225, 190, 231));
        JPanel sbtns = new JPanel(new GridLayout(1, 2, 5, 0));
        sbtns.add(searchBtn); sbtns.add(listBtn);
        p.add(sbtns, g);

        // Action Listeners
        createBtn.addActionListener(e -> createAccount());
        updateBtn.addActionListener(e -> updateAccount());
        deleteBtn.addActionListener(e -> deleteAccount());
        clearBtn.addActionListener(e -> clearFields());
        openBtn.addActionListener(e -> statusOp("Open"));
        closeBtn.addActionListener(e -> statusOp("Close"));
        freezeBtn.addActionListener(e -> statusOp("Freeze"));
        reactBtn.addActionListener(e -> statusOp("Reactivate"));
        searchBtn.addActionListener(e -> search());
        listBtn.addActionListener(e -> loadAll());
        
        return p;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(new String[]{"Account#", "Cust ID", "Type", "Balance", "Branch ID", "Status", "Created"}, 0) {
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

    private void createAccount() {
        try {
            Account a = getInput();
            double bal = Double.parseDouble(balanceField.getText().trim());
            a.setBalance(bal);
            if (svc.createAccount(a)) { JOptionPane.showMessageDialog(this, "Account created!"); loadAll(); clearFields(); }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void updateAccount() {
        try {
            if (accNumField.getText().isEmpty()) { showError("Select account first."); return; }
            Account a = getInput();
            if (svc.updateAccount(a)) { JOptionPane.showMessageDialog(this, "Updated!"); loadAll(); clearFields(); }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void deleteAccount() {
        try {
            String n = accNumField.getText().trim();
            if (n.isEmpty()) { showError("Select account first."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete account " + n + "?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                if (svc.deleteAccount(n)) { JOptionPane.showMessageDialog(this, "Deleted!"); loadAll(); clearFields(); }
            }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void statusOp(String op) {
        try {
            String n = accNumField.getText().trim();
            if (n.isEmpty()) { showError("Select account first."); return; }
            boolean res = switch (op) {
                case "Open" -> svc.openAccount(n);
                case "Close" -> svc.closeAccount(n);
                case "Freeze" -> svc.freezeAccount(n);
                case "Reactivate" -> svc.reactivateAccount(n);
                default -> false;
            };
            if (res) { JOptionPane.showMessageDialog(this, op + " successful!"); loadAll(); }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void search() {
        try { fillTable(svc.search(searchField.getText().trim())); } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void loadAll() {
        try { fillTable(svc.getAllAccounts()); } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void fillTable(List<Account> list) {
        tableModel.setRowCount(0);
        for (Account a : list)
            tableModel.addRow(new Object[]{a.getAccountNumber(), a.getCustomerId(), a.getAccountType(),
                String.format("%.2f", a.getBalance()), a.getBranchId(), a.getStatus(), a.getCreatedAt()});
    }

    private void populateFromRow(int row) {
        accNumField.setText(str(tableModel.getValueAt(row, 0)));
        custIdField.setText(str(tableModel.getValueAt(row, 1)));
        typeCombo.setSelectedItem(str(tableModel.getValueAt(row, 2)));
        balanceField.setText(str(tableModel.getValueAt(row, 3)));
        statusCombo.setSelectedItem(str(tableModel.getValueAt(row, 5)));
        int brId = Integer.parseInt(str(tableModel.getValueAt(row, 4)));
        for (int i = 0; i < branchCombo.getItemCount(); i++) {
            if (((Branch) branchCombo.getItemAt(i)).getBranchId() == brId) { branchCombo.setSelectedIndex(i); break; }
        }
    }

    private Account getInput() {
        Account a = new Account();
        a.setAccountNumber(accNumField.getText().trim());
        a.setCustomerId(Integer.parseInt(custIdField.getText().trim()));
        a.setAccountType((String) typeCombo.getSelectedItem());
        a.setBranchId(((Branch) branchCombo.getSelectedItem()).getBranchId());
        a.setStatus((String) statusCombo.getSelectedItem());
        return a;
    }

    private void loadBranches() {
        try {
            branchCombo.removeAllItems();
            branchSvc.getAllBranches().forEach(branchCombo::addItem);
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void clearFields() {
        accNumField.setText(""); custIdField.setText(""); balanceField.setText("0.00");
        typeCombo.setSelectedIndex(0); statusCombo.setSelectedIndex(0); table.clearSelection();
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