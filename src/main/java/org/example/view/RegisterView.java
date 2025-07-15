//package org.example.view;
//
//import org.example.entity.UserInfo;
//import javax.swing.*;
//import java.awt.*;
//
//public class RegisterView extends JPanel {
//    private final MainFrameView mainFrame;
//
//    private JTextField fullNameField;
//    private JTextField userNameField;
//    private JPasswordField passwordField;
//    private JPasswordField confirmPasswordField;
//    private JTextField emailField;
//    private JTextField phoneField;
//    private JLabel lblInfor;
//    private JButton btnRegister;
//    private JButton btnBack;
//
//    public RegisterView(MainFrameView mainFrame) {
//        this.mainFrame = mainFrame;
//        setLayout(null);
//        initComponents();
//
//        btnRegister.addActionListener(e -> RegisterController.handleRegister(this));
//        btnBack.addActionListener(e -> mainFrame.showLoginPanel());
//    }
//
//    private void initComponents() {
//        JLabel imgBackground = new JLabel(new ImageIcon("src/main/java/org/example/image/register.png"));
//        imgBackground.setLayout(null);
//        imgBackground.setBounds(0, 0, 1200, 800);
//        this.add(imgBackground);
//
//        JLabel lblTitle = new JLabel("Đăng ký tài khoản");
//        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
//        lblTitle.setForeground(new Color(135, 206, 235));
//        int widthTitle = lblTitle.getFontMetrics(lblTitle.getFont()).stringWidth(lblTitle.getText());
//        lblTitle.setBounds(600 - widthTitle / 2, 170, widthTitle, 30);
//        imgBackground.add(lblTitle);
//
//        fullNameField = new JTextField();
//        fullNameField.setFont(new Font("Arial", Font.PLAIN, 12));
//        fullNameField.setBounds(440, 255, 320, 33);
//        fullNameField.putClientProperty("JTextField.placeholderText", "Nhập tên khách hàng");
//        imgBackground.add(fullNameField);
//
//        userNameField = new JTextField();
//        userNameField.setFont(new Font("Arial", Font.PLAIN, 12));
//        userNameField.setBounds(440, 315, 320, 33);
//        userNameField.putClientProperty("JTextField.placeholderText", "Nhập tên tài khoản");
//        imgBackground.add(userNameField);
//
//        passwordField = new JPasswordField();
//        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
//        passwordField.setBounds(440, 375, 320, 33);
//        passwordField.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");
//        imgBackground.add(passwordField);
//
//        emailField = new JTextField();
//        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
//        emailField.setBounds(440, 435, 320, 33);
//        emailField.putClientProperty("JTextField.placeholderText", "Nhập email");
//        imgBackground.add(emailField);
//
//        phoneField = new JTextField();
//        phoneField.setFont(new Font("Arial", Font.PLAIN, 12));
//        phoneField.setBounds(440, 495, 320, 33);
//        phoneField.putClientProperty("JTextField.placeholderText", "Nhập số điện thoại");
//        imgBackground.add(phoneField);
//
//        confirmPasswordField = new JPasswordField();
//        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
//        confirmPasswordField.setBounds(440, 555, 320, 33);
//        confirmPasswordField.putClientProperty("JTextField.placeholderText", "Nhập lại mật khẩu");
//        imgBackground.add(confirmPasswordField);
//
//        lblInfor = new JLabel("");
//        lblInfor.setFont(new Font("Arial", Font.PLAIN, 12));
//        lblInfor.setForeground(new Color(255, 112, 67));
//        imgBackground.add(lblInfor);
//
//        btnRegister = new JButton("Đăng ký");
//        btnRegister.setFont(new Font("Arial", Font.PLAIN, 12));
//        btnRegister.setForeground(Color.WHITE);
//        btnRegister.setBackground(new Color(131, 198, 159));
//        btnRegister.setBounds(440, 655, 320, 33);
//        imgBackground.add(btnRegister);
//
//        btnBack = new JButton("Quay lại");
//        btnBack.setFont(new Font("Arial", Font.PLAIN, 12));
//        btnBack.setBounds(440, 695, 320, 30);
//
//        btnBack.setContentAreaFilled(false);
//        btnBack.setBorderPainted(false);
//        btnBack.setFocusPainted(false);
//
//        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                btnBack.setForeground(new Color(169, 169, 169)); // màu hồng khi hover
//            }
//
//            @Override
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                btnBack.setForeground(new Color(0, 102, 204)); // màu xanh lại khi rời chuột
//            }
//        });
//
//        imgBackground.add(btnBack);
//    }
//
//    // Lấy thông tin đăng ký
//    public UserInfo getUserInfo() {
//        return new UserInfo(
//                fullNameField.getText().trim(),
//                userNameField.getText().trim(),
//                String.valueOf(passwordField.getPassword()).trim(),
//                emailField.getText().trim(),
//                phoneField.getText().trim()
//        );
//    }
//
//    public String getConfirmPassword() {
//        return String.valueOf(confirmPasswordField.getPassword()).trim();
//    }
//
//    public void showError(String message) {
//        lblInfor.setText(message);
//        int width = lblInfor.getFontMetrics(lblInfor.getFont()).stringWidth(message);
//        lblInfor.setBounds(600 - width / 2, 605, width, 28);
//    }
//
//    public void resetForm() {
//        fullNameField.setText("");
//        userNameField.setText("");
//        passwordField.setText("");
//        emailField.setText("");
//        phoneField.setText("");
//        confirmPasswordField.setText("");
//        lblInfor.setText("");
//    }
//
//    // -------------------------------
//    // Controller nội bộ
//    // -------------------------------
//    public static class RegisterController {
//
//        public static void handleRegister(RegisterView view) {
//            UserInfo info = view.getUserInfo();
//            String confirmPass = view.getConfirmPassword();
//
//            if (info.getFullName().isEmpty() || info.getUserName().isEmpty() ||
//                    info.getPassword().isEmpty() || confirmPass.isEmpty() ||
//                    info.getEmail().isEmpty() || info.getPhoneNumber().isEmpty()) {
//
//                view.showError("Vui lòng điền đầy đủ thông tin!");
//                return;
//            }
//
//            if (!info.getPassword().equals(confirmPass)) {
//                view.showError("Mật khẩu xác nhận không khớp!");
//                return;
//            }
//
//            // TODO: xử lý lưu thông tin người dùng (DB, file, v.v.)
//
//            view.showError(""); // Xóa lỗi nếu có
//            view.mainFrame.showFinishPanel();
//        }
//    }
//}

package org.example.view;

import org.example.entity.UserInfo;
import org.example.controller.RegisterController;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JPanel {
    private final MainFrameView mainFrame;

    private JTextField fullNameField;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField phoneField;
    private JLabel lblInfor;
    private JButton btnRegister;
    private JButton btnBack;

    public RegisterView(MainFrameView mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        initComponents();

        btnRegister.addActionListener(e -> RegisterController.handleRegister(this));
        btnBack.addActionListener(e -> mainFrame.showLoginPanel());
    }

    private void initComponents() {
        JLabel imgBackground = new JLabel(new ImageIcon("src/main/java/org/example/image/register.png"));
        imgBackground.setLayout(null);
        imgBackground.setBounds(0, 0, 1200, 800);
        this.add(imgBackground);

        JLabel lblTitle = new JLabel("Đăng ký tài khoản");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(135, 206, 235));
        int widthTitle = lblTitle.getFontMetrics(lblTitle.getFont()).stringWidth(lblTitle.getText());
        lblTitle.setBounds(600 - widthTitle / 2, 170, widthTitle, 30);
        imgBackground.add(lblTitle);

        fullNameField = new JTextField();
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        fullNameField.setBounds(440, 255, 320, 33);
        fullNameField.putClientProperty("JTextField.placeholderText", "Nhập tên khách hàng");
        imgBackground.add(fullNameField);

        userNameField = new JTextField();
        userNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        userNameField.setBounds(440, 315, 320, 33);
        userNameField.putClientProperty("JTextField.placeholderText", "Nhập tên tài khoản");
        imgBackground.add(userNameField);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBounds(440, 375, 320, 33);
        passwordField.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");
        imgBackground.add(passwordField);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
        emailField.setBounds(440, 435, 320, 33);
        emailField.putClientProperty("JTextField.placeholderText", "Nhập email");
        imgBackground.add(emailField);

        phoneField = new JTextField();
        phoneField.setFont(new Font("Arial", Font.PLAIN, 12));
        phoneField.setBounds(440, 495, 320, 33);
        phoneField.putClientProperty("JTextField.placeholderText", "Nhập số điện thoại");
        imgBackground.add(phoneField);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        confirmPasswordField.setBounds(440, 555, 320, 33);
        confirmPasswordField.putClientProperty("JTextField.placeholderText", "Nhập lại mật khẩu");
        imgBackground.add(confirmPasswordField);

        lblInfor = new JLabel("");
        lblInfor.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfor.setForeground(new Color(255, 112, 67));
        imgBackground.add(lblInfor);

        btnRegister = new JButton("Đăng ký");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(131, 198, 159));
        btnRegister.setBounds(440, 655, 320, 33);
        imgBackground.add(btnRegister);

        btnBack = new JButton("Quay lại");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBack.setBounds(440, 695, 320, 30);

        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);

        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBack.setForeground(new Color(169, 169, 169));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBack.setForeground(new Color(0, 102, 204));
            }
        });

        imgBackground.add(btnBack);
    }

    public UserInfo getUserInfo() {
        return new UserInfo(
                fullNameField.getText().trim(),
                userNameField.getText().trim(),
                String.valueOf(passwordField.getPassword()).trim(),
                emailField.getText().trim(),
                phoneField.getText().trim()
        );
    }

    public String getConfirmPassword() {
        return String.valueOf(confirmPasswordField.getPassword()).trim();
    }

    public void showError(String message) {
        lblInfor.setText(message);
        int width = lblInfor.getFontMetrics(lblInfor.getFont()).stringWidth(message);
        lblInfor.setBounds(600 - width / 2, 605, width, 28);
    }

    public void resetForm() {
        fullNameField.setText("");
        userNameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        phoneField.setText("");
        confirmPasswordField.setText("");
        lblInfor.setText("");
    }

    public MainFrameView getMainFrame() {
        return mainFrame;
    }
}
