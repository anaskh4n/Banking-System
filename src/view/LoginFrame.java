package view;
import util.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("Banking System - Login");
        setSize(380, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(30, 60, 114));

        JLabel title = new JLabel("BANKING MANAGEMENT SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(15,10,10,10));
        main.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,5,5,5);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0; form.add(new JLabel("Username:"), g);
        g.gridx=1; userField = new JTextField(15); form.add(userField, g);
        g.gridx=0; g.gridy=1; form.add(new JLabel("Password:"), g);
        g.gridx=1; passField = new JPasswordField(15); form.add(passField, g);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(30,60,114));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        g.gridx=0; g.gridy=2; g.gridwidth=2;
        form.add(loginBtn, g);
        main.add(form, BorderLayout.CENTER);
        add(main);

        loginBtn.addActionListener(e -> doLogin());
        passField.addActionListener(e -> doLogin());
        setVisible(true);
    }

    private void doLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                "SELECT * FROM admins WHERE username=? AND password=?");
            ps.setString(1, user); ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dispose();
                new MainFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
