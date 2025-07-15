package org.example.view;

import org.example.controller.PasswordResetController;

import javax.swing.*;
import java.awt.*;

public class PasswordResetView extends JPanel {
    private MainFrameView mainFrame;
    private String username;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel lblInfo;
    private JButton btnConfirm;
    private JLabel background;

    public PasswordResetView(MainFrameView mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        initComponents();

        btnConfirm.addActionListener(e -> PasswordResetController.handlePasswordReset(this));
    }

    private void initComponents() {
        background = new JLabel(new ImageIcon("src/main/java/org/example/image/password.png"));
        background.setLayout(null);
        background.setBounds(0, 0, 1200, 800);
        add(background);

        JLabel lblTitle = new JLabel("Tạo mật khẩu mới");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(135, 206, 235));
        lblTitle.setBounds(500, 380, 250, 30);
        background.add(lblTitle);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(440, 440, 320, 33);
        passwordField.putClientProperty("JTextField.placeholderText", "Mật khẩu mới");
        background.add(passwordField);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBounds(440, 500, 320, 33);
        confirmPasswordField.putClientProperty("JTextField.placeholderText", "Xác nhận mật khẩu");
        background.add(confirmPasswordField);

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        background.add(lblInfo);

        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setFont(new Font("Arial", Font.PLAIN, 12));
        btnConfirm.setBackground(new Color(255, 105, 180));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setBounds(440, 570, 320, 33);
        background.add(btnConfirm);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return String.valueOf(passwordField.getPassword()).trim();
    }

    public String getConfirmPassword() {
        return String.valueOf(confirmPasswordField.getPassword()).trim();
    }

    public String getUsername() {
        return username;
    }

    public void showError(String message) {
        lblInfo.setText(message);
        int width = lblInfo.getFontMetrics(lblInfo.getFont()).stringWidth(message);
        lblInfo.setBounds(600 - width / 2, 630, width, 30);
    }

    public void clearError() {
        lblInfo.setText("");
    }

    public void clearFields() {
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    public void resetForm() {
        passwordField.setText("");
        confirmPasswordField.setText("");
        lblInfo.setText("");
    }

    public MainFrameView getMainFrame() {
        return mainFrame;
    }
}
