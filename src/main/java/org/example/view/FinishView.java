package org.example.view;

import javax.swing.*;
import java.awt.*;

public class FinishView extends JPanel {
    private MainFrameView mainFrame;
    private JButton btnLogin;
    private JLabel background;

    public FinishView(MainFrameView mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        initComponents();
    }

    private void initComponents() {
        background = new JLabel(new ImageIcon("src/main/java/org/example/image/registerfinish.png"));
        background.setLayout(null);
        background.setBounds(0, 0, 1200, 800);
        add(background);

        btnLogin = new JButton("Đăng nhập ngay");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(new Color(240, 240, 240));
        btnLogin.setBounds(475, 685, 250, 33);
        background.add(btnLogin);

        btnLogin.addActionListener(e -> openLogin());
    }

    private void openLogin() {
        mainFrame.showLoginPanel();
    }
}