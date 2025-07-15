package org.example.view;

import org.example.action.CheckAccount;
import org.example.service.UserInfoService;

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

        btnConfirm.addActionListener(e -> confirmAction());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void confirmAction() {
        String password = String.valueOf(passwordField.getPassword()).trim();
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword()).trim();

        String error = CheckAccount.validateNewPassword(password, confirmPassword);
        if (error != null) {
            showError(error);
        } else {
            UserInfoService.updatePassword("userinfos.xml", username, password);
            clearFields();
            clearError();
            mainFrame.showFinishPanel();
        }
    }

    private void showError(String message) {
        lblInfo.setText(message);
        int width = lblInfo.getFontMetrics(lblInfo.getFont()).stringWidth(message);
        lblInfo.setBounds(600 - width / 2, 630, width, 30);
    }

    private void clearError() {
        lblInfo.setText("");
    }

    private void clearFields() {
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    public void resetForm() {
        passwordField.setText("");
        confirmPasswordField.setText("");
        lblInfo.setText("");
    }
}