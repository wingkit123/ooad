package gui;

import facade.RentalSystemFacade;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * (Code Explanation - Presentation Layer / GUI):
 * The Entry Point UI for the system. Connects ONLY to the RentalSystemFacade.
 * Handles user input and displays visual components, but contains zero business logic.
 * Demonstrates clean Separation of Concerns.
 */
public class LoginFrame extends JFrame {
    private RentalSystemFacade facade;
    
    // Cards & Layout
    private JPanel cardContainer;
    private CardLayout cardLayout;

    // Login Form Fields
    private JTextField loginIdField;
    private JTextField loginNameField;

    // Register Form Fields
    private JTextField regIdField;
    private JTextField regNameField;
    private JComboBox<String> regTypeCombo;

    // Color Palette (Professional Blue Theme)
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185); // #2980b9
    private static final Color BG_LIGHT = new Color(245, 245, 245);
    private static final Color TEXT_DARK = new Color(51, 51, 51);

    public LoginFrame(RentalSystemFacade facade) {
        super("Campus Smart Equipment Rental - Sign In");
        this.facade = facade;
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(580, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main outer panel with Professional Blue background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(PRIMARY_BLUE);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Center card with white background
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(25, 30, 25, 30)
        ));

        // Title at the top of the card
        JLabel logoLabel = new JLabel("CAMPUS SMART RENTAL KIOSK", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(PRIMARY_BLUE);
        logoLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        card.add(logoLabel, BorderLayout.NORTH);

        // Card Container in the middle
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(Color.WHITE);

        cardContainer.add(createLoginPanel(), "LOGIN");
        cardContainer.add(createRegisterPanel(), "REGISTER");

        card.add(cardContainer, BorderLayout.CENTER);

        // Footer inside card (Admin Link)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel adminLink = new JLabel("<html><font color='#999999' size='2'>Admin Panel</font></html>");
        adminLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAdminLoginDialog();
            }
        });
        footerPanel.add(adminLink);
        card.add(footerPanel, BorderLayout.SOUTH);

        // Add card to main panel with GridBagConstraints to keep it centered
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(card, gbc);

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0); // Reduced vertical gaps
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title: Log In
        JLabel loginTitle = new JLabel("Log In");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginTitle.setForeground(TEXT_DARK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(loginTitle, gbc);

        // User ID Field
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 4, 0);
        loginIdField = new JTextField();
        loginIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginIdField.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "User ID / Student ID"
        ));
        panel.add(loginIdField, gbc);

        // Name Field
        gbc.gridy = 2;
        loginNameField = new JTextField();
        loginNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginNameField.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Your Full Name"
        ));
        panel.add(loginNameField, gbc);

        // Login Button
        gbc.gridy = 3;
        gbc.insets = new Insets(12, 0, 5, 0); // Brought closer
        JButton loginBtn = createPrimaryButton("Enter Self-Service Kiosk");
        loginBtn.addActionListener(e -> handleUserLogin());
        panel.add(loginBtn, gbc);

        // Sign Up Toggle Link
        gbc.gridy = 4;
        gbc.insets = new Insets(2, 0, 2, 0);
        JLabel signupLink = new JLabel("<html><span style='color:#777; font-size:11px;'>New to Kiosk? </span><font color='#1976D2' size='3'>Sign Up</font></html>", SwingConstants.CENTER);
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "REGISTER");
            }
        });
        panel.add(signupLink, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title: Sign Up
        JLabel regTitle = new JLabel("Sign Up");
        regTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        regTitle.setForeground(TEXT_DARK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(regTitle, gbc);

        // User ID Field
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 4, 0);
        regIdField = new JTextField();
        regIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        regIdField.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Create User ID"
        ));
        panel.add(regIdField, gbc);

        // Name Field
        gbc.gridy = 2;
        regNameField = new JTextField();
        regNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        regNameField.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Your Full Name"
        ));
        panel.add(regNameField, gbc);

        // Role Category Dropdown
        gbc.gridy = 3;
        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.setBackground(Color.WHITE);
        typePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Role Category"
        ));
        regTypeCombo = new JComboBox<>(new String[]{"Student", "Staff", "Final Year Student"});
        regTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typePanel.add(regTypeCombo, BorderLayout.CENTER);
        panel.add(typePanel, gbc);

        // Register Button
        gbc.gridy = 4;
        gbc.insets = new Insets(12, 0, 5, 0);
        JButton registerBtn = createPrimaryButton("Register & Enter Kiosk");
        registerBtn.addActionListener(e -> handleUserRegister());
        panel.add(registerBtn, gbc);

        // Log In Toggle Link
        gbc.gridy = 5;
        gbc.insets = new Insets(2, 0, 2, 0);
        JLabel loginLink = new JLabel("<html><span style='color:#777; font-size:11px;'>Already have an account? </span><font color='#1976D2' size='3'>Log In</font></html>", SwingConstants.CENTER);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "LOGIN");
            }
        });
        panel.add(loginLink, gbc);

        return panel;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(new Color(25, 118, 210)); // #1976D2
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleUserLogin() {
        String id = loginIdField.getText().trim();
        String name = loginNameField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both User ID and Name.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the user is registered first
        if (!facade.isUserRegistered(id)) {
            JOptionPane.showMessageDialog(this, "User ID not found. Please click 'Sign Up' below to register first.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = facade.userLogin(id, name, "Student");
        if ("Success".equals(result)) {
            // Open user dashboard
            RentalAppGUI dashboard = new RentalAppGUI(facade, false);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUserRegister() {
        String id = regIdField.getText().trim();
        String name = regNameField.getText().trim();
        String type = (String) regTypeCombo.getSelectedItem();

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all registration fields.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verify if ID already exists
        if (facade.isUserRegistered(id)) {
            JOptionPane.showMessageDialog(this, "User ID is already registered. Please login instead.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = facade.userLogin(id, name, type);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Registration Successful! Entering Kiosk...");
            // Open user dashboard
            RentalAppGUI dashboard = new RentalAppGUI(facade, false);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAdminLoginDialog() {
        JDialog dialog = new JDialog(this, "Admin Verification", true);
        dialog.setSize(320, 220);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        dialog.setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Admin ID:"), gbc);
        
        JTextField adminIdField = new JTextField(12);
        gbc.gridx = 1;
        dialog.add(adminIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Password:"), gbc);
        
        JPasswordField adminPassField = new JPasswordField(12);
        gbc.gridx = 1;
        dialog.add(adminPassField, gbc);

        JButton verifyBtn = new JButton("Login as Admin") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        verifyBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        verifyBtn.setBackground(PRIMARY_BLUE);
        verifyBtn.setForeground(Color.WHITE);
        verifyBtn.setContentAreaFilled(false);
        verifyBtn.setFocusPainted(false);
        verifyBtn.addActionListener(e -> {
            String adminId = adminIdField.getText().trim();
            String password = new String(adminPassField.getPassword());
            
            if (facade.adminLogin(adminId, password)) {
                dialog.dispose();
                // Open admin dashboard
                RentalAppGUI dashboard = new RentalAppGUI(facade, true);
                dashboard.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid Admin credentials.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(verifyBtn, gbc);

        dialog.setVisible(true);
    }
}
