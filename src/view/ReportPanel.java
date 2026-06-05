package view;
import service.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportPanel extends JPanel {
    private final CustomerService    custSvc = new CustomerService();
    private final AccountService     accSvc  = new AccountService();
    private final TransactionService txSvc   = new TransactionService();

    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public ReportPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── toolbar ──────────────────────────────────────────────────────────
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBorder(BorderFactory.createTitledBorder("Reports"));

        JTextField branchIdField = new JTextField(5);

        JButton allCustBtn   = btn("👤 All Customers",    new Color(165, 214, 167)); // green
        JButton allAccBtn    = btn("🏦 All Accounts",     new Color(144, 202, 249)); // blue
        JButton branchAccBtn = btn("🏢 Branch Accounts",  new Color(225, 190, 231)); // purple
        JButton txBtn        = btn("💳 All Transactions", new Color(178, 235, 242)); // cyan

        top.add(allCustBtn);
        top.add(allAccBtn);
        top.add(new JLabel("  Branch ID:"));
        top.add(branchIdField);
        top.add(branchAccBtn);
        top.add(txBtn);

        // ── status bar ───────────────────────────────────────────────────────
        statusLabel = new JLabel("Select a report.");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 0));

        JPanel north = new JPanel(new BorderLayout());
        north.add(top,         BorderLayout.CENTER);
        north.add(statusLabel, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        // ── table ────────────────────────────────────────────────────────────
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ── listeners ────────────────────────────────────────────────────────
        allCustBtn.addActionListener(e -> {
            try {
                List<Customer> list = custSvc.getAllCustomers();
                tableModel.setDataVector(null,
                    new String[]{"ID","Name","CNIC","Phone","Email","Address","Created"});
                for (Customer c : list)
                    tableModel.addRow(new Object[]{
                        c.getCustomerId(), c.getFullName(), c.getCnic(),
                        c.getPhone(), c.getEmail(), c.getAddress(), c.getDateCreated()
                    });
                statusLabel.setText("Total customers: " + list.size());
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        allAccBtn.addActionListener(e -> {
            try {
                List<Account> list = accSvc.getAllAccounts();
                tableModel.setDataVector(null,
                    new String[]{"Account#","Cust ID","Type","Balance","Branch","Status","Created"});
                for (Account a : list)
                    tableModel.addRow(new Object[]{
                        a.getAccountNumber(), a.getCustomerId(), a.getAccountType(),
                        String.format("%.2f", a.getBalance()),
                        a.getBranchId(), a.getStatus(), a.getCreatedAt()
                    });
                statusLabel.setText("Total accounts: " + list.size());
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        branchAccBtn.addActionListener(e -> {
            try {
                String bId = branchIdField.getText().trim();
                if (bId.isEmpty()) { showError("Enter Branch ID."); return; }
                List<Account> list = accSvc.getByBranch(Integer.parseInt(bId));
                tableModel.setDataVector(null,
                    new String[]{"Account#","Cust ID","Type","Balance","Branch","Status","Created"});
                for (Account a : list)
                    tableModel.addRow(new Object[]{
                        a.getAccountNumber(), a.getCustomerId(), a.getAccountType(),
                        String.format("%.2f", a.getBalance()),
                        a.getBranchId(), a.getStatus(), a.getCreatedAt()
                    });
                statusLabel.setText("Accounts in branch " + bId + ": " + list.size());
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        txBtn.addActionListener(e -> {
            try {
                List<Transaction> list = txSvc.getAll();
                tableModel.setDataVector(null,
                    new String[]{"ID","From","To","Amount","Type","DateTime"});
                for (Transaction t : list)
                    tableModel.addRow(new Object[]{
                        t.getTransactionId(), t.getFromAccount(), t.getToAccount(),
                        String.format("%.2f", t.getAmount()), t.getType(), t.getDateTime()
                    });
                statusLabel.setText("Total transactions: " + list.size());
            } catch (Exception ex) { showError(ex.getMessage()); }
        });
    }

    /** Pastel background + BLACK text — works on all platforms / LAFs */
    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.BLACK);       // ← the actual fix
        b.setOpaque(true);
        b.setBorderPainted(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void showError(String m) {
        JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
