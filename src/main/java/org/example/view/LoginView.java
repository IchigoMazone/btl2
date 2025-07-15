package org.example.view;

import org.example.entity.User;
import org.example.controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JPanel {
    private final MainFrameView mainFrame;

    private JLabel lblTitle;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnForgotPassword;
    private JLabel lblInfor;
    private JLabel lblSpace;

    public LoginView(MainFrameView mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        initComponents();

        // Gắn controller vào các nút
        btnLogin.addActionListener(e -> LoginController.handleLogin(this));
        btnRegister.addActionListener(e -> LoginController.openRegister(this));
        btnForgotPassword.addActionListener(e -> LoginController.openForgotPassword(this));
    }

    private void initComponents() {
        JLabel imgBackground = new JLabel(new ImageIcon("src/main/java/org/example/image/login.png"));
        imgBackground.setLayout(null);
        imgBackground.setBounds(0, 0, 1200, 800);
        this.add(imgBackground);

        lblTitle = new JLabel("Đăng nhập hệ thống");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(135, 206, 235));
        lblTitle.setBounds(495, 380, 250, 30);
        imgBackground.add(lblTitle);

        lblInfor = new JLabel("");
        lblInfor.setFont(new Font("Arial", Font.PLAIN, 13));
        lblInfor.setForeground(Color.WHITE);
        imgBackground.add(lblInfor);

        userNameField = new JTextField();
        userNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        userNameField.setBounds(440, 440, 320, 33);
        userNameField.putClientProperty("JTextField.placeholderText", "Nhập tên đăng nhập");
        imgBackground.add(userNameField);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(440, 500, 320, 33);
        passwordField.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");
        imgBackground.add(passwordField);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(440, 570, 320, 33);
        imgBackground.add(btnLogin);

        btnRegister = new JButton("Đăng ký tài khoản");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setBounds(600, 628, 150, 28);
        addHoverEffect(btnRegister);
        imgBackground.add(btnRegister);

        btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        btnForgotPassword.setForeground(Color.WHITE);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setFocusPainted(false);
        btnForgotPassword.setBounds(450, 628, 150, 28);
        addHoverEffect(btnForgotPassword);
        imgBackground.add(btnForgotPassword);

        lblSpace = new JLabel("||");
        lblSpace.setFont(new Font("Arial", Font.BOLD, 20));
        lblSpace.setForeground(new Color(240, 240, 240));
        lblSpace.setBounds(590, 620, 20, 30);
        imgBackground.add(lblSpace);
    }

    private void addHoverEffect(JButton button) {
        Color normalColor = button.getForeground();
        Color hoverColor = new Color(52, 152, 219);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(normalColor);
            }
        });
    }

    public User getUser() {
        return new User(
                userNameField.getText().trim(),
                String.valueOf(passwordField.getPassword()).trim()
        );
    }

    public void showError(String message) {
        lblInfor.setText(message);
        int width = lblInfor.getFontMetrics(lblInfor.getFont()).stringWidth(message);
        lblInfor.setBounds(600 - width / 2, 670, width, 30);
    }

    public MainFrameView getMainFrame() {
        return mainFrame;
    }

    public void resetForm() {
        userNameField.setText("");
        passwordField.setText("");
        lblInfor.setText("");
    }
}
