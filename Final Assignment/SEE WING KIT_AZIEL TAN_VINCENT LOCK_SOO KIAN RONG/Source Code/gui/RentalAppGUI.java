package gui;

import facade.RentalSystemFacade;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import model.Equipment;
import model.RentalRecord;

/**
 * (Code Explanation - Presentation Layer / GUI):
 * The Main Application GUI dashboard. Connects ONLY to the RentalSystemFacade.
 * It renders different panels (Admin vs User) dynamically based on session data 
 * passed from the Facade. Contains zero business logic or calculations.
 */
public class RentalAppGUI extends JFrame {
    private RentalSystemFacade facade;
    private boolean isAdmin;
    
    // UI Elements - Admin Mode
    private JTable adminInventoryTable;
    private DefaultTableModel adminInventoryModel;
    private JTextField addIdField, addNameField, addRateField;
    private JComboBox<String> addCategoryCombo;
    
    private JTextField updateNameField, updateRateField;
    private JComboBox<String> updateStatusCombo;
    
    private JTable adminRentalsTable;
    private DefaultTableModel adminRentalsModel;

    // UI Elements - User Mode
    private JTable userCatalogTable;
    private DefaultTableModel userCatalogModel;
    private JTextField rentDurationField;
    private JLabel cartSummaryLabel;
    
    private JTable userRentalsTable;
    private DefaultTableModel userRentalsModel;
    private JTextField returnDaysField;
    private JCheckBox damageCheck;
    private JTextArea receiptArea;

    public RentalAppGUI(RentalSystemFacade facade, boolean isAdmin) {
        this.facade = facade;
        this.isAdmin = isAdmin;
        initializeUI();
        refreshAllData();
    }

    private void initializeUI() {
        setTitle(isAdmin ? "Smart Rental System - ADMIN PANEL" : "Smart Rental System - SELF-SERVICE KIOSK");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Colors & Fonts
        Color headerColor = isAdmin ? new Color(192, 57, 43) : new Color(41, 128, 185); // Red for admin, Blue for user
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        String welcomeText = isAdmin 
                ? "ADMINISTRATIVE CONTROL CONSOLE" 
                : "WELCOME, " + facade.getCurrentUser().getName().toUpperCase() + " (" + facade.getCurrentUser().getType() + ")";
        
        JLabel titleLabel = new JLabel(welcomeText);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> handleLogout());
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        if (isAdmin) {
            tabbedPane.addTab("Manage Inventory", createAdminInventoryTab());
            tabbedPane.addTab("System Rental Logs", createAdminRentalsTab());
        } else {
            tabbedPane.addTab("Rent Equipment", createUserRentTab());
            tabbedPane.addTab("My Active Rentals", createUserReturnTab());
        }
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void handleLogout() {
        facade.logout();
        LoginFrame loginFrame = new LoginFrame(facade);
        loginFrame.setVisible(true);
        this.dispose();
    }

    // ==========================================
    // ADMIN TABS CREATION
    // ==========================================

    private JPanel createAdminInventoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        String[] columns = {"ID", "Name", "Category", "Rate ($/day)", "Status"};
        adminInventoryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        adminInventoryTable = new JTable(adminInventoryModel);
        adminInventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminInventoryTable.getSelectionModel().addListSelectionListener(e -> handleInventorySelect());
        panel.add(new JScrollPane(adminInventoryTable), BorderLayout.CENTER);
        
        // Operations panel (Add and Edit forms side by side)
        JPanel opsPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        
        // 1. Add Form
        JPanel addForm = new JPanel(new GridLayout(5, 2, 5, 5));
        addForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Add Equipment", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        addForm.add(new JLabel("ID:"));
        addIdField = new JTextField();
        addForm.add(addIdField);
        
        addForm.add(new JLabel("Name:"));
        addNameField = new JTextField();
        addForm.add(addNameField);
        
        addForm.add(new JLabel("Category:"));
        addCategoryCombo = new JComboBox<>(new String[]{"Electronics", "Media Equipment", "Laboratory Equipment"});
        addForm.add(addCategoryCombo);
        
        addForm.add(new JLabel("Daily Rate ($):"));
        addRateField = new JTextField();
        addForm.add(addRateField);
        
        JButton addBtn = new JButton("Add Equipment");
        addBtn.addActionListener(e -> handleAddEquipment());
        addForm.add(addBtn);
        opsPanel.add(addForm);
        
        // 2. Edit/Update Form
        JPanel editForm = new JPanel(new GridLayout(5, 2, 5, 5));
        editForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Edit Selected Equipment", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        editForm.add(new JLabel("Name:"));
        updateNameField = new JTextField();
        editForm.add(updateNameField);
        
        editForm.add(new JLabel("Daily Rate ($):"));
        updateRateField = new JTextField();
        editForm.add(updateRateField);
        
        editForm.add(new JLabel("Status:"));
        updateStatusCombo = new JComboBox<>(new String[]{"AVAILABLE", "MAINTENANCE", "DAMAGED"});
        editForm.add(updateStatusCombo);
        
        JButton updateBtn = new JButton("Update Info");
        updateBtn.addActionListener(e -> handleUpdateEquipment());
        editForm.add(updateBtn);
        
        JButton removeBtn = new RoundedButton(
            "Remove Item",
            new Color(231, 76, 60), // Red
            new Color(235, 100, 85), // Hover
            new Color(192, 57, 43)   // Pressed
        );
        removeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        removeBtn.addActionListener(e -> handleRemoveEquipment());
        editForm.add(removeBtn);
        
        opsPanel.add(editForm);
        
        panel.add(opsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createAdminRentalsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Record ID", "Renter ID", "Renter Name", "Equipment ID", "Equipment Name", "Duration (Days)", "Status", "Deposit ($)"};
        adminRentalsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        adminRentalsTable = new JTable(adminRentalsModel);
        panel.add(new JScrollPane(adminRentalsTable), BorderLayout.CENTER);
        
        return panel;
    }

    // ==========================================
    // USER TABS CREATION (Self-Service)
    // ==========================================

    private JPanel createUserRentTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Available Catalog Table
        String[] columns = {"Select", "ID", "Name", "Category", "Daily Rate ($)"};
        userCatalogModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int r, int c) { return c == 0; }
        };
        userCatalogModel.addTableModelListener(e -> updateCartSummary());
        userCatalogTable = new JTable(userCatalogModel);
        userCatalogTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(new JLabel("Select items to rent using checkboxes:"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(userCatalogTable), BorderLayout.CENTER);
        panel.add(leftPanel, BorderLayout.CENTER);
        
        // Checkout details (Simple Receipt Style)
        JPanel checkoutPanel = new JPanel(new BorderLayout(10, 10));
        checkoutPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 1), 
                "Rental Checkout", 
                TitledBorder.LEFT, 
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), 
                new Color(41, 128, 185)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Rental duration input panel (Rental Information)
        JPanel durPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        durPanel.setBackground(new Color(245, 247, 250));
        durPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JLabel durLabel = new JLabel("Rental Duration (Days): ");
        durLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        durPanel.add(durLabel);
        rentDurationField = new JTextField("3", 5);
        rentDurationField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rentDurationField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCartSummary(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCartSummary(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCartSummary(); }
        });
        durPanel.add(rentDurationField);
        
        checkoutPanel.add(durPanel, BorderLayout.NORTH);
        
        // Order Summary & Billing Details (Billing & Settlement)
        cartSummaryLabel = new JLabel();
        cartSummaryLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        JScrollPane summaryScroll = new JScrollPane(cartSummaryLabel);
        summaryScroll.setBorder(BorderFactory.createEmptyBorder());
        summaryScroll.setOpaque(false);
        summaryScroll.getViewport().setOpaque(false);
        
        checkoutPanel.add(summaryScroll, BorderLayout.CENTER);
        
        // Action Buttons Panel
        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        btnPanel.setOpaque(false);

        JButton checkoutBtn = new RoundedButton(
            "Confirm Pay & Checkout", 
            new Color(39, 174, 96), // Action Green
            new Color(46, 204, 113), // Hover light green
            new Color(30, 132, 73)   // Pressed dark green
        );
        checkoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checkoutBtn.addActionListener(e -> handleCheckout());
        btnPanel.add(checkoutBtn, BorderLayout.CENTER);
        
        checkoutPanel.add(btnPanel, BorderLayout.SOUTH);
        
        panel.add(checkoutPanel, BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createUserReturnTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Record ID", "Equipment Name", "Planned Duration (Days)", "Rent Date", "Deposit ($)"};
        userRentalsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        userRentalsTable = new JTable(userRentalsModel);
        userRentalsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(new JLabel("Your Active Rentals:"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(userRentalsTable), BorderLayout.CENTER);
        
        // Return details
        JPanel returnForm = new JPanel(new GridLayout(4, 2, 5, 5));
        returnForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Return Settlement Form", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        returnForm.add(new JLabel("Actual Duration (Days):"));
        returnDaysField = new JTextField();
        returnForm.add(returnDaysField);
        
        returnForm.add(new JLabel("Is Item Damaged?"));
        damageCheck = new JCheckBox("Yes");
        returnForm.add(damageCheck);
        
        JButton returnBtn = new RoundedButton(
            "Proceed Settle & Refund",
            new Color(39, 174, 96), // Action Green
            new Color(46, 204, 113), // Hover light green
            new Color(30, 132, 73)   // Pressed dark green
        );
        returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        returnBtn.addActionListener(e -> handleReturn());
        returnForm.add(returnBtn);
        
        leftPanel.add(returnForm, BorderLayout.SOUTH);
        panel.add(leftPanel, BorderLayout.CENTER);
        
        // Receipt display
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Billing Settlement Receipt", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        receiptArea = new JTextArea(25, 45);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        rightPanel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }

    // ==========================================
    // EVENT & DATA ACTIONS
    // ==========================================

    private void handleInventorySelect() {
        int selected = adminInventoryTable.getSelectedRow();
        if (selected != -1) {
            String name = (String) adminInventoryModel.getValueAt(selected, 1);
            String rateStr = (String) adminInventoryModel.getValueAt(selected, 3);
            String status = (String) adminInventoryModel.getValueAt(selected, 4);
            
            updateNameField.setText(name);
            updateRateField.setText(rateStr);
            updateStatusCombo.setSelectedItem(status);
        }
    }

    private void handleAddEquipment() {
        String id = addIdField.getText().trim();
        String name = addNameField.getText().trim();
        String category = (String) addCategoryCombo.getSelectedItem();
        String rateStr = addRateField.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || rateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double rate = Double.parseDouble(rateStr);
            facade.addEquipment(id, name, category, rate);
            JOptionPane.showMessageDialog(this, "Equipment added successfully!");
            addIdField.setText("");
            addNameField.setText("");
            addRateField.setText("");
            refreshAllData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Daily rate must be a valid decimal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateEquipment() {
        int selected = adminInventoryTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) adminInventoryModel.getValueAt(selected, 0);
        String name = updateNameField.getText().trim();
        String rateStr = updateRateField.getText().trim();
        String status = (String) updateStatusCombo.getSelectedItem();
        
        if (name.isEmpty() || rateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double rate = Double.parseDouble(rateStr);
            facade.updateEquipment(id, name, rate, status);
            JOptionPane.showMessageDialog(this, "Equipment updated!");
            refreshAllData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rate.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveEquipment() {
        int selected = adminInventoryTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) adminInventoryModel.getValueAt(selected, 0);
        facade.removeEquipment(id);
        JOptionPane.showMessageDialog(this, "Equipment removed.");
        refreshAllData();
    }

    private void updateCartSummary() {
        List<Integer> selectedRows = new ArrayList<>();
        if (userCatalogModel != null) {
            for (int i = 0; i < userCatalogModel.getRowCount(); i++) {
                Boolean selected = (Boolean) userCatalogModel.getValueAt(i, 0);
                if (selected != null && selected) {
                    selectedRows.add(i);
                }
            }
        }
        
        String durStr = rentDurationField.getText().trim();
        int days = 3;
        try {
            days = Integer.parseInt(durStr);
        } catch (NumberFormatException e) {
            // keep 3 if invalid
        }
        
        int rowCount = selectedRows.size();
        double totalBaseFee = 0.0;
        double deposit = rowCount * 50.00;
        
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:\"Segoe UI\", sans-serif; font-size:12px; margin:5px; color:#333;'>");
        
        if (rowCount == 0) {
            html.append("<p style='color:#777;'><i>No items selected. Select items using checkboxes.</i></p></body></html>");
            if (cartSummaryLabel != null) cartSummaryLabel.setText(html.toString());
            return;
        }

        html.append("<b>Selected Equipment:</b><ul style='margin-top:2px; margin-bottom:8px;'>");
        for (int row : selectedRows) {
            String id = (String) userCatalogModel.getValueAt(row, 1);
            Equipment eq = facade.getAllEquipment().stream()
                    .filter(e -> e.getEquipmentId().equals(id))
                    .findFirst().orElse(null);
            if (eq != null) {
                html.append("<li>").append(eq.getName()).append("</li>");
                double base = eq.calculateBaseFee(days);
                totalBaseFee += base;
            }
        }
        html.append("</ul>");

        // Discounts calculations
        double discountPct = 0.0;
        String discountType = "Student Discount";
        if (facade.getCurrentUser().getType() == model.User.UserType.STAFF) {
            discountPct = 20.0;
            discountType = "Staff Discount";
        } else if (facade.getCurrentUser().getType() == model.User.UserType.FINAL_YEAR_STUDENT) {
            discountPct = 10.0;
            discountType = "Final Year Student Discount";
        }
        
        double discountVal = totalBaseFee * (discountPct / 100.0);
        double finalRent = totalBaseFee - discountVal;
        double grandTotal = finalRent + deposit;

        html.append("<b>Rental Duration:</b><ul style='margin-top:2px; margin-bottom:8px;'>")
            .append("<li>").append(days).append(" Days</li></ul>");
        
        html.append("<b>Deposit:</b><ul style='margin-top:2px; margin-bottom:8px;'>")
            .append("<li>").append(rowCount).append(" × $50.00 = $").append(String.format("%.2f", deposit)).append("</li></ul>");
        
        html.append("<b>Base Rental Fee:</b><ul style='margin-top:2px; margin-bottom:8px;'>")
            .append("<li>$").append(String.format("%.2f", totalBaseFee)).append("</li></ul>");

        html.append("<b>Discounts Applied:</b><ul style='margin-top:2px; margin-bottom:8px;'>");
        if (discountPct > 0) {
            html.append("<li>").append(discountType).append(" (").append(String.format("%.0f", discountPct)).append("%): -$")
                .append(String.format("%.2f", discountVal)).append("</li>");
        } else {
            html.append("<li>None</li>");
        }
        html.append("</ul>");

        html.append("<hr style='border-top:1px solid #ccc; margin-top:10px; margin-bottom:10px;'/>");
        html.append("<div style='font-size:16px;'><b>Grand Total:</b> <span style='color:#27AE60;'>$")
            .append(String.format("%.2f", grandTotal)).append("</span></div>");

        html.append("</body></html>");
        cartSummaryLabel.setText(html.toString());
    }

    private void handleCheckout() {
        List<Integer> selectedRows = new ArrayList<>();
        for (int i = 0; i < userCatalogModel.getRowCount(); i++) {
            Boolean selected = (Boolean) userCatalogModel.getValueAt(i, 0);
            if (selected != null && selected) {
                selectedRows.add(i);
            }
        }
        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least 1 item to rent.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String durStr = rentDurationField.getText().trim();
        try {
            int duration = Integer.parseInt(durStr);
            List<String> ids = new ArrayList<>();
            for (int r : selectedRows) {
                ids.add((String) userCatalogModel.getValueAt(r, 1));
            }
            
            String response = facade.rentEquipmentList(ids, duration);
            JOptionPane.showMessageDialog(this, response);
            refreshAllData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid duration value.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReturn() {
        int selected = userRentalsTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record from the table.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String recordId = (String) userRentalsModel.getValueAt(selected, 0);
        String actDaysStr = returnDaysField.getText().trim();
        boolean damaged = damageCheck.isSelected();
        
        try {
            int days = Integer.parseInt(actDaysStr);
            String result = facade.returnEquipment(recordId, days, damaged);
            receiptArea.setText(result);
            returnDaysField.setText("");
            damageCheck.setSelected(false);
            refreshAllData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid duration days.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAllData() {
        if (isAdmin) {
            // Admin Table
            adminInventoryModel.setRowCount(0);
            for (Equipment eq : facade.getAllEquipment()) {
                adminInventoryModel.addRow(new Object[]{
                    eq.getEquipmentId(),
                    eq.getName(),
                    eq.getCategory(),
                    String.format("%.2f", eq.getDailyRentalRate()),
                    eq.getStatus().toString()
                });
            }
            
            // Logs
            adminRentalsModel.setRowCount(0);
            for (RentalRecord r : facade.getAllRentals()) {
                adminRentalsModel.addRow(new Object[]{
                    r.getRecordId(),
                    r.getUser().getUserId(),
                    r.getUser().getName(),
                    r.getEquipment().getEquipmentId(),
                    r.getEquipment().getName(),
                    r.getPlannedDurationDays(),
                    r.getStatus().toString(),
                    String.format("%.2f", r.getDepositPaid())
                });
            }
        } else {
            // User Catalog
            userCatalogModel.setRowCount(0);
            for (Equipment eq : facade.getAvailableEquipment()) {
                userCatalogModel.addRow(new Object[]{
                    Boolean.FALSE,
                    eq.getEquipmentId(),
                    eq.getName(),
                    eq.getCategory(),
                    String.format("%.2f", eq.getDailyRentalRate())
                });
            }
            updateCartSummary();
            
            // User Rentals
            userRentalsModel.setRowCount(0);
            for (RentalRecord r : facade.getCurrentUserActiveRentals()) {
                userRentalsModel.addRow(new Object[]{
                    r.getRecordId(),
                    r.getEquipment().getName(),
                    r.getPlannedDurationDays(),
                    r.getRentDate().toString(),
                    String.format("%.2f", r.getDepositPaid())
                });
            }
        }
    }

    // ==========================================
    // CUSTOM BUTTON STYLING COMPONENTS (Swing UI/UX)
    // ==========================================

    private static class RoundedButton extends JButton {
        private Color hoverColor;
        private Color normalColor;
        private Color pressedColor;
        
        public RoundedButton(String text, Color normal, Color hover, Color pressed) {
            super(text);
            this.normalColor = normal;
            this.hoverColor = hover;
            this.pressedColor = pressed;
            
            setBackground(normalColor);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(normalColor);
                }
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    setBackground(pressedColor);
                }
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                }
            });
        }
        
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class SecondaryButton extends JButton {
        public SecondaryButton(String text) {
            super(text);
            setBackground(new Color(240, 244, 248));
            setForeground(new Color(41, 128, 185));
            setFocusPainted(false);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(224, 235, 245));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(240, 244, 248));
                }
            });
        }
    }
}

