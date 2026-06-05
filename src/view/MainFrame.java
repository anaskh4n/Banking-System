package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Banking Management System");
        setSize(1100, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Make tabs look a bit more spacious natively
        UIManager.put("TabbedPane.tabInsets", new Insets(8, 15, 8, 15));

        // Wrap everything in a container to add universal padding
        JPanel mainContainer = new JPanel(new BorderLayout(0, 10)); // 10px vertical gap
        mainContainer.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // --- Top Header with Styled Buttons ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("🏦 Banking Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        // Applying the black text / pastel background style
        JButton logoutBtn = btn("Logout", new Color(255, 224, 130)); // Pastel Orange/Yellow
        JButton exitBtn = btn("Exit", new Color(239, 154, 154));   // Pastel Red
        
        buttonPanel.add(logoutBtn);
        buttonPanel.add(exitBtn);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // --- Tabs ---
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13)); 
        
        tabs.add("👤 Customers", new CustomerPanel());
        tabs.add("🏦 Accounts", new AccountPanel());
        tabs.add("🏢 Branches", new BranchPanel());
        tabs.add("💳 Transactions", new TransactionPanel()); // Assuming you have this
        tabs.add("📊 Reports", new ReportPanel());         // Assuming you have this
        
        mainContainer.add(tabs, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);

        // --- Footer ---
        JLabel footer = new JLabel("  Banking Management System | Admin Logged In", SwingConstants.LEFT);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(Color.DARK_GRAY);
        // Added a subtle top line border to separate the footer
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
        add(footer, BorderLayout.SOUTH);

        // --- Action Listeners ---
        logoutBtn.addActionListener(e -> { 
            dispose(); 
            new LoginFrame(); // Assuming you have a LoginFrame class
        });
        
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // THE SAME BUTTON HELPER METHOD (Black Text, Custom BG, Hover Cursor)
    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); 
        b.setForeground(Color.BLACK); // Set text to black
        b.setFocusPainted(false); 
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(6, 15, 6, 15) // Slightly wider padding for top bar
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Adds a pointer hand on hover
        return b;
    }
}