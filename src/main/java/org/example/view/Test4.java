//import com.formdev.flatlaf.FlatLightLaf;
//import org.kordamp.ikonli.materialdesign.MaterialDesign;
//import org.kordamp.ikonli.swing.FontIcon;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.Random;
//
//public class Test4 extends JFrame {
//
//    private Color backgroundColor;
//    private Color hoverColor;
//    private Color pressedColor;
//    private Color selectedTextColor;
//    private Color normalTextColor;
//    private JButton selectedButton = null;
//
//    public Test4() {
//        setTitle("Thông tin người dùng");
//        setSize(1200, 800);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
//        setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
//        backgroundColor = UIManager.getColor("Panel.background");
//        hoverColor = backgroundColor.brighter();
//        pressedColor = new Color(220, 230, 255);
//        selectedTextColor = new Color(0, 120, 215);
//        normalTextColor = Color.DARK_GRAY;
//
//        JPanel drawer = createDrawerMenu();
//        JPanel rightPanel = createRightPanel();
//
//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().add(drawer, BorderLayout.WEST);
//        getContentPane().add(rightPanel, BorderLayout.CENTER);
//    }
//
//    private JPanel createDrawerMenu() {
//        JPanel drawer = new JPanel();
//        drawer.setLayout(new BoxLayout(drawer, BoxLayout.Y_AXIS));
//        drawer.setBackground(backgroundColor);
//        drawer.setPreferredSize(new Dimension(250, 800));
//        drawer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//
//        JButton accountBtn = createNavButton("account-circle", "Admin", true);
//        accountBtn.setEnabled(false);
//        drawer.add(accountBtn);
//        drawer.add(Box.createVerticalStrut(20));
//
//        drawer.add(createNavButton("home", "Trang chủ", false));
//        drawer.add(Box.createVerticalStrut(8));
//        drawer.add(createNavButton("magnify", "Tìm kiếm", false));
//        drawer.add(Box.createVerticalStrut(8));
//        drawer.add(createNavButton("clipboard-check", "Thông báo", false));
//        drawer.add(Box.createVerticalStrut(8));
//        drawer.add(createNavButton("calendar-check", "Đặt phòng", false));
//        drawer.add(Box.createVerticalGlue());
//        drawer.add(createNavButton("logout", "Đăng xuất", false));
//
//        selectButton((JButton) drawer.getComponent(2));
//        return drawer;
//    }
//
//    private JButton createNavButton(String iconName, String text, boolean centerAligned) {
//        JButton button = new JButton(text);
//        button.setFocusPainted(false);
//        button.setBackground(backgroundColor);
//        button.setForeground(normalTextColor);
//        button.setFont(new Font("Arial", Font.PLAIN, 15));
//        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        button.setOpaque(true);
//        button.setBorderPainted(false);
//        button.setContentAreaFilled(true);
//        button.setMaximumSize(new Dimension(250, 40));
//        button.setPreferredSize(new Dimension(250, 40));
//
//        MaterialDesign mdiIcon;
//        try {
//            mdiIcon = MaterialDesign.valueOf("MDI_" + iconName.toUpperCase().replace("-", "_"));
//        } catch (IllegalArgumentException e) {
//            mdiIcon = MaterialDesign.MDI_HELP_CIRCLE_OUTLINE;
//        }
//        FontIcon icon = FontIcon.of(mdiIcon, 20);
//        icon.setIconColor(normalTextColor);
//        button.setIcon(icon);
//        button.setIconTextGap(10);
//
//        if (centerAligned) {
//            button.setHorizontalAlignment(SwingConstants.CENTER);
//            button.setFont(new Font("Arial", Font.BOLD, 15));
//            button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//        } else {
//            button.setHorizontalAlignment(SwingConstants.LEFT);
//            button.setBorder(BorderFactory.createEmptyBorder(10, 65, 10, 0));
//        }
//
//        if (button.isEnabled()) {
//            button.addMouseListener(new MouseAdapter() {
//                @Override public void mouseEntered(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(hoverColor);
//                }
//                @Override public void mouseExited(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(backgroundColor);
//                }
//                @Override public void mousePressed(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(pressedColor);
//                }
//                @Override public void mouseReleased(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(hoverColor);
//                }
//            });
//
//            button.addActionListener(e -> selectButton(button));
//        }
//
//        return button;
//    }
//
//    private void selectButton(JButton button) {
//        if (selectedButton != null) {
//            selectedButton.setBackground(backgroundColor);
//            selectedButton.setForeground(normalTextColor);
//            ((FontIcon) selectedButton.getIcon()).setIconColor(normalTextColor);
//            selectedButton.repaint();
//        }
//        button.setBackground(backgroundColor);
//        button.setForeground(selectedTextColor);
//        ((FontIcon) button.getIcon()).setIconColor(selectedTextColor);
//        button.repaint();
//        selectedButton = button;
//    }
//
//    private JPanel createRightPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setPreferredSize(new Dimension(950, 800));
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
//
//        panel.add(createFormPanel());
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(createButtonPanel());
//
//        return panel;
//    }
//
//    private JPanel createFormPanel() {
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBackground(backgroundColor);
//        formPanel.setBorder(BorderFactory.createTitledBorder(
//                BorderFactory.createLineBorder(new Color(180, 180, 180)),
//                "Thông tin người dùng",
//                TitledBorder.LEFT, TitledBorder.TOP));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(6, 10, 6, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 1.0;
//
//        gbc.gridy = 0;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Số người:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        Integer[] soNguoiOptions = {1, 2, 3, 4};
//        JComboBox<Integer> cbSoNguoi = new JComboBox<>(soNguoiOptions);
//        formPanel.add(cbSoNguoi, gbc);
//        gbc.gridwidth = 1;
//
//        gbc.gridy = 1;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Tài khoản:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 2;
//        JTextField tfTaiKhoan = new JTextField();
//        formPanel.add(tfTaiKhoan, gbc);
//        gbc.gridx = 3;
//        gbc.gridwidth = 1;
//        JButton btnRandom = new JButton("Sinh ngẫu nhiên");
//        formPanel.add(btnRandom, gbc);
//
//        btnRandom.addActionListener(e -> {
//            String randomStr = "user" + String.format("%08d", new Random().nextInt(100_000_000));
//            tfTaiKhoan.setText(randomStr);
//        });
//
//        gbc.gridy = 2;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("SĐT:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        JTextField tfSdt = new JTextField();
//        formPanel.add(tfSdt, gbc);
//
//        gbc.gridy = 3;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Gmail:"), gbc);
//        gbc.gridx = 1;
//        JTextField tfGmail = new JTextField();
//        formPanel.add(tfGmail, gbc);
//
//        String[] loaiOptions = {"Không có", "Mã định danh"};
//        JTextField[] hoTenFields = new JTextField[4];
//        JComboBox<String>[] loaiCombos = new JComboBox[4];
//        JTextField[] maFields = new JTextField[4];
//
//        for (int i = 0; i < 4; i++) {
//            int row = 4 + i * 2;
//            gbc.gridy = row;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
//            gbc.gridx = 1;
//            gbc.gridwidth = 3;
//            hoTenFields[i] = new JTextField();
//            formPanel.add(hoTenFields[i], gbc);
//
//            gbc.gridy = row + 1;
//            gbc.gridwidth = 1;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
//            gbc.gridx = 1;
//            loaiCombos[i] = new JComboBox<>(loaiOptions);
//            formPanel.add(loaiCombos[i], gbc);
//            gbc.gridx = 2;
//            formPanel.add(new JLabel("Mã:"), gbc);
//            gbc.gridx = 3;
//            maFields[i] = new JTextField();
//            formPanel.add(maFields[i], gbc);
//
//            int index = i;
//            loaiCombos[i].addActionListener(e -> {
//                String selected = (String) loaiCombos[index].getSelectedItem();
//                if ("Không có".equals(selected)) {
//                    maFields[index].setText("Trẻ em");
//                    maFields[index].setEditable(false);
//                } else {
//                    maFields[index].setText("");
//                    maFields[index].setEditable(true);
//                }
//            });
//        }
//
//        cbSoNguoi.addActionListener(e -> {
//            int selected = (int) cbSoNguoi.getSelectedItem();
//            for (int i = 0; i < 4; i++) {
//                boolean enable = i < selected;
//                hoTenFields[i].setEnabled(enable);
//                loaiCombos[i].setEnabled(enable);
//                maFields[i].setEnabled(enable);
//                if (!enable) {
//                    hoTenFields[i].setText("");
//                    maFields[i].setText("");
//                }
//            }
//        });
//
//        cbSoNguoi.setSelectedIndex(0);
//        return formPanel;
//    }
//
//    private JPanel createButtonPanel() {
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        btnPanel.setBackground(backgroundColor);
//
//        JButton btnXacNhan = new JButton("Xác nhận");
//        JButton btnQuayLai = new JButton("Quay lại");
//
//        btnPanel.add(btnQuayLai);
//        btnPanel.add(btnXacNhan);
//
//        return btnPanel;
//    }
//
//    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
//        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
//        while (keys.hasMoreElements()) {
//            Object key = keys.nextElement();
//            Object value = UIManager.get(key);
//            if (value instanceof javax.swing.plaf.FontUIResource)
//                UIManager.put(key, f);
//        }
//    }
//
//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
//        } catch (Exception ignored) {}
//
//        SwingUtilities.invokeLater(() -> new Test4().setVisible(true));
//    }
//}
