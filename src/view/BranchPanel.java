package view;

import model.Branch;
import service.BranchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BranchPanel extends JPanel {
    private final BranchService svc = new BranchService();
    private JTextField idField, nameField, cityField, managerField, contactField;
    private DefaultTableModel tableModel;
    private JTable table;

    public BranchPanel() {
        setLayout(new BorderLayout(10, 10)); // Increased spacing
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Better padding
        add(buildForm(), BorderLayout.WEST);
        add(buildTable(), BorderLayout.CENTER);
        loadAll();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setPreferredSize(new Dimension(320, 0)); // Slightly wider for better text fit
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Branch Details"));
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5); // Better spacing between form elements
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridy = 0; g.gridx = 0; p.add(new JLabel("Branch ID:"), g);
        g.gridx = 1; idField = new JTextField(15); idField.setEditable(false); idField.setBackground(new Color(230, 230, 230)); p.add(idField, g);
        
        g.gridy = 1; g.gridx = 0; p.add(new JLabel("Branch Name:"), g);
        g.gridx = 1; nameField = new JTextField(15); p.add(nameField, g);
        
        g.gridy = 2; g.gridx = 0; p.add(new JLabel("City:"), g);
        g.gridx = 1; cityField = new JTextField(15); p.add(cityField, g);
        
        g.gridy = 3; g.gridx = 0; p.add(new JLabel("Manager:"), g);
        g.gridx = 1; managerField = new JTextField(15); p.add(managerField, g);
        
        g.gridy = 4; g.gridx = 0; p.add(new JLabel("Contact:"), g);
        g.gridx = 1; contactField = new JTextField(15); p.add(contactField, g);

        g.gridy = 5; g.gridx = 0; g.gridwidth = 2;
        // Updated to 3 rows to properly accommodate 5 buttons
        JPanel btns = new JPanel(new GridLayout(3, 2, 5, 5)); 
        
        // Applying lighter shades for readability with black text
        JButton addBtn = btn("Add", new Color(165, 214, 167));
        JButton updBtn = btn("Update", new Color(144, 202, 249));
        JButton delBtn = btn("Delete", new Color(239, 154, 154));
        JButton clrBtn = btn("Clear", new Color(245, 245, 245));
        JButton listBtn = btn("List All", new Color(225, 190, 231));
        
        btns.add(addBtn); btns.add(updBtn); 
        btns.add(delBtn); btns.add(clrBtn); 
        btns.add(listBtn);
        p.add(btns, g);

        // Action Listeners
        addBtn.addActionListener(e -> addBranch());
        updBtn.addActionListener(e -> updateBranch());
        delBtn.addActionListener(e -> deleteBranch());
        clrBtn.addActionListener(e -> clearFields());
        listBtn.addActionListener(e -> loadAll());
        
        return p;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "City", "Manager", "Contact"}, 0) {
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

    private void addBranch() {
        try {
            Branch b = getInput();
            if (svc.addBranch(b)) { JOptionPane.showMessageDialog(this, "Branch added!"); loadAll(); clearFields(); }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void updateBranch() {
        try {
            if (idField.getText().isEmpty()) { showError("Select a branch first."); return; }
            Branch b = getInput(); 
            b.setBranchId(Integer.parseInt(idField.getText()));
            if (svc.updateBranch(b)) { JOptionPane.showMessageDialog(this, "Updated!"); loadAll(); clearFields(); }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void deleteBranch() {
        try {
            if (idField.getText().isEmpty()) { showError("Select a branch first."); return; }
            int id = Integer.parseInt(idField.getText());
            if (JOptionPane.showConfirmDialog(this, "Delete branch #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                if (svc.deleteBranch(id)) { JOptionPane.showMessageDialog(this, "Deleted!"); loadAll(); clearFields(); }
            }
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void loadAll() {
        try {
            List<Branch> list = svc.getAllBranches(); 
            tableModel.setRowCount(0);
            for (Branch b : list) 
                tableModel.addRow(new Object[]{b.getBranchId(), b.getBranchName(), b.getCity(), b.getManagerName(), b.getContact()});
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void populateFromRow(int row) {
        idField.setText(str(tableModel.getValueAt(row, 0)));
        nameField.setText(str(tableModel.getValueAt(row, 1)));
        cityField.setText(str(tableModel.getValueAt(row, 2)));
        managerField.setText(str(tableModel.getValueAt(row, 3)));
        contactField.setText(str(tableModel.getValueAt(row, 4)));
    }

    private Branch getInput() {
        return new Branch(nameField.getText().trim(), cityField.getText().trim(),
            managerField.getText().trim(), contactField.getText().trim());
    }

    private void clearFields() {
        idField.setText(""); nameField.setText(""); cityField.setText("");
        managerField.setText(""); contactField.setText(""); table.clearSelection();
    }

    // UPDATED: Black text, custom background, borders, and hover cursor
    private JButton btn(String t, Color c) {
        JButton b = new JButton(t); 
        b.setBackground(c); 
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
    private void showError(String m) { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }
}