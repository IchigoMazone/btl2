package org.example.view;

import org.example.action.CheckAccount;
import javax.swing.*;
import java.awt.*;

public class ForgotPasswordView extends JPanel {
    private MainFrameView mainFrame;

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

        // Action: Quay lại
        btnBack.addActionListener(e -> mainFrame.showLoginPanel());

        // Action: Xác nhận
        btnConfirm.addActionListener(e -> {
            String username = userNameField.getText().trim();
            String email = emailField.getText().trim();

            String error = CheckAccount.checkForgotPassword("userinfos.xml", username, email);
            if (error != null) {
                showError(error);
            } else {
                clearError();
                mainFrame.showPasswordResetPanel(username);
            }
        });
    }

    private void showError(String message) {
        lblInfo.setText(message);
        int width = lblInfo.getFontMetrics(lblInfo.getFont()).stringWidth(message);
        lblInfo.setBounds(600 - width / 2, 630, width, 30);
    }

    private void clearError() {
        lblInfo.setText("");
    }

    public void resetForm() {
        userNameField.setText("");
        emailField.setText("");
        lblInfo.setText("");
    }
}