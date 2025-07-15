package org.example.view;

import org.example.controller.ForgotPasswordController;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordView extends JPanel {
    private final MainFrameView mainFrame;

    private JTextField userNameField;
    private JTextField emailField;
    private JLabel lblInfo;
    private JButton btnBack;
    private JButton btnConfirm;
    private JLabel background;

    public ForgotPasswordView(MainFrameView mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        initComponents();

        btnBack.addActionListener(e -> mainFrame.showLoginPanel());
        btnConfirm.addActionListener(e -> ForgotPasswordController.handleForgotPassword(this));
    }

    private void initComponents() {
        background = new JLabel(new ImageIcon("src/main/java/org/example/image/account.png"));
        background.setLayout(null);
        background.setBounds(0, 0, 1200, 800);
        add(background);

        JLabel lblTitle = new JLabel("Xác minh tài khoản");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(135, 206, 235));
        lblTitle.setBounds(500, 380, 220, 30);
        background.add(lblTitle);

        userNameField = new JTextField();
        userNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        userNameField.setBounds(440, 440, 320, 33);
        userNameField.putClientProperty("JTextField.placeholderText", "Tên đăng nhập");
        background.add(userNameField);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(440, 500, 320, 33);
        emailField.putClientProperty("JTextField.placeholderText", "Email");
        background.add(emailField);

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        background.add(lblInfo);

        btnBack = new JButton("Quay lại");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBack.setBackground(new Color(0, 102, 204));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBounds(610, 570, 150, 33);
        background.add(btnBack);

        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setFont(new Font("Arial", Font.PLAIN, 12));
        btnConfirm.setBackground(new Color(0, 102, 204));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setBounds(440, 570, 150, 33);
        background.add(btnConfirm);
    }

    public String getUsername() {
        return userNameField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public void showError(String message) {
        lblInfo.setText(message);
        int width = lblInfo.getFontMetrics(lblInfo.getFont()).stringWidth(message);
        lblInfo.setBounds(600 - width / 2, 630, width, 30);
    }

    public void clearError() {
        lblInfo.setText("");
    }

    public void resetForm() {
        userNameField.setText("");
        emailField.setText("");
        lblInfo.setText("");
    }

    public MainFrameView getMainFrame() {
        return mainFrame;
    }
}
