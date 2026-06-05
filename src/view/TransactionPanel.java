package view;
import service.AccountService;
import service.TransactionService;
import model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionPanel extends JPanel {
    private final AccountService accSvc = new AccountService();
    private final TransactionService txSvc = new TransactionService();

    public TransactionPanel() {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabs.add("Deposit",         buildDepositPanel());
        tabs.add("Withdrawal",      buildWithdrawPanel());
        tabs.add("Transfer",        buildTransferPanel());
        tabs.add("History",         buildHistoryPanel());
        tabs.add("Balance Inquiry", buildBalancePanel());
        add(tabs, BorderLayout.CENTER);
    }

    // ── Deposit ──────────────────────────────────────────────────────────────
    private JPanel buildDepositPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = gbc();

        JTextField accField = new JTextField(20);
        JTextField amtField = new JTextField(20);

        g.gridy = 0; g.gridx = 0; p.add(new JLabel("Account Number:"), g);
        g.gridx = 1; p.add(accField, g);
        g.gridy = 1; g.gridx = 0; p.add(new JLabel("Amount (PKR):"), g);
        g.gridx = 1; p.add(amtField, g);
        g.gridy = 2; g.gridx = 0; g.gridwidth = 2;

        JButton btn = btn("✔  Deposit", new Color(165, 214, 167));   // pastel green
        p.add(btn, g);

        btn.addActionListener(e -> {
            try {
                accSvc.deposit(accField.getText().trim(),
                               Double.parseDouble(amtField.getText().trim()));
                JOptionPane.showMessageDialog(p, "Deposit successful!");
                accField.setText(""); amtField.setText("");
            } catch (Exception ex) { showError(p, ex.getMessage()); }
        });
        return p;
    }

    // ── Withdrawal ───────────────────────────────────────────────────────────
    private JPanel buildWithdrawPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = gbc();

        JTextField accField = new JTextField(20);
        JTextField amtField = new JTextField(20);

        g.gridy = 0; g.gridx = 0; p.add(new JLabel("Account Number:"), g);
        g.gridx = 1; p.add(accField, g);
        g.gridy = 1; g.gridx = 0; p.add(new JLabel("Amount (PKR):"), g);
        g.gridx = 1; p.add(amtField, g);
        g.gridy = 2; g.gridx = 0; g.gridwidth = 2;

        JButton btn = btn("✖  Withdraw", new Color(239, 154, 154));  // pastel red
        p.add(btn, g);

        btn.addActionListener(e -> {
            try {
                accSvc.withdraw(accField.getText().trim(),
                                Double.parseDouble(amtField.getText().trim()));
                JOptionPane.showMessageDialog(p, "Withdrawal successful!");
                accField.setText(""); amtField.setText("");
            } catch (Exception ex) { showError(p, ex.getMessage()); }
        });
        return p;
    }

    // ── Transfer ─────────────────────────────────────────────────────────────
    private JPanel buildTransferPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = gbc();

        JTextField fromField = new JTextField(20);
        JTextField toField   = new JTextField(20);
        JTextField amtField  = new JTextField(20);

        g.gridy = 0; g.gridx = 0; p.add(new JLabel("From Account:"), g);
        g.gridx = 1; p.add(fromField, g);
        g.gridy = 1; g.gridx = 0; p.add(new JLabel("To Account:"), g);
        g.gridx = 1; p.add(toField, g);
        g.gridy = 2; g.gridx = 0; p.add(new JLabel("Amount (PKR):"), g);
        g.gridx = 1; p.add(amtField, g);
        g.gridy = 3; g.gridx = 0; g.gridwidth = 2;

        JButton btn = btn("⇄  Transfer", new Color(144, 202, 249));  // pastel blue
        p.add(btn, g);

        btn.addActionListener(e -> {
            try {
                accSvc.transfer(fromField.getText().trim(),
                                toField.getText().trim(),
                                Double.parseDouble(amtField.getText().trim()));
                JOptionPane.showMessageDialog(p, "Transfer successful!");
                fromField.setText(""); toField.setText(""); amtField.setText("");
            } catch (Exception ex) { showError(p, ex.getMessage()); }
        });
        return p;
    }

    // ── History ──────────────────────────────────────────────────────────────
    private JPanel buildHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JTextField accField  = new JTextField(15);
        JButton searchBtn    = btn("Search",           new Color(178, 235, 242)); // pastel cyan
        JButton allBtn       = btn("All Transactions", new Color(225, 190, 231)); // pastel purple

        top.add(new JLabel("Account #:"));
        top.add(accField);
        top.add(searchBtn);
        top.add(allBtn);
        p.add(top, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "From", "To", "Amount", "Type", "Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            try {
                fillTxTable(model, txSvc.getByAccount(accField.getText().trim()));
            } catch (Exception ex) { showError(p, ex.getMessage()); }
        });
        allBtn.addActionListener(e -> {
            try { fillTxTable(model, txSvc.getAll()); }
            catch (Exception ex) { showError(p, ex.getMessage()); }
        });
        return p;
    }

    // ── Balance Inquiry ──────────────────────────────────────────────────────
    private JPanel buildBalancePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = gbc();

        JTextField accField = new JTextField(20);
        JLabel balLabel = new JLabel("---");
        balLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        balLabel.setForeground(new Color(21, 101, 192));

        g.gridy = 0; g.gridx = 0; p.add(new JLabel("Account Number:"), g);
        g.gridx = 1; p.add(accField, g);
        g.gridy = 1; g.gridx = 0; g.gridwidth = 2;

        JButton btn = btn("🔍  Check Balance", new Color(255, 224, 130)); // pastel yellow
        p.add(btn, g);
        g.gridy = 2; p.add(new JLabel("Balance:"), g);
        g.gridy = 3; p.add(balLabel, g);

        btn.addActionListener(e -> {
            try {
                model.Account a = accSvc.getAccount(accField.getText().trim());
                if (a == null) { showError(p, "Account not found."); return; }
                balLabel.setText("PKR " + String.format("%.2f", a.getBalance())
                        + "  [" + a.getStatus() + "]");
            } catch (Exception ex) { showError(p, ex.getMessage()); }
        });
        return p;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private void fillTxTable(DefaultTableModel model, List<Transaction> list) {
        model.setRowCount(0);
        for (Transaction t : list)
            model.addRow(new Object[]{
                t.getTransactionId(), t.getFromAccount(), t.getToAccount(),
                String.format("%.2f", t.getAmount()), t.getType(), t.getDateTime()
            });
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;
        return g;
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

    private void showError(JPanel p, String msg) {
        JOptionPane.showMessageDialog(p, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
