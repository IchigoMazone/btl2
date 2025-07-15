package org.example.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class AdminBookingView {

    public static JPanel createUserPanel() {
        Color backgroundColor = UIManager.getColor("Panel.background");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        panel.add(createFormPanel(backgroundColor));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButtonPanel(backgroundColor));

        return panel;
    }

    private static JPanel createFormPanel(Color backgroundColor) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Thông tin người dùng",
                TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Số người:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        Integer[] soNguoiOptions = {1, 2, 3, 4};
        JComboBox<Integer> cbSoNguoi = new JComboBox<>(soNguoiOptions);
        formPanel.add(cbSoNguoi, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Tài khoản:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextField tfTaiKhoan = new JTextField();
        formPanel.add(tfTaiKhoan, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        JButton btnRandom = new JButton("Sinh ngẫu nhiên");
        formPanel.add(btnRandom, gbc);

        btnRandom.addActionListener(e -> {
            String randomStr = "user" + String.format("%08d", new Random().nextInt(100_000_000));
            tfTaiKhoan.setText(randomStr);
        });

        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JTextField tfSdt = new JTextField();
        formPanel.add(tfSdt, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Gmail:"), gbc);
        gbc.gridx = 1;
        JTextField tfGmail = new JTextField();
        formPanel.add(tfGmail, gbc);

        String[] loaiOptions = {"Không có", "Mã định danh"};
        JTextField[] hoTenFields = new JTextField[4];
        JComboBox<String>[] loaiCombos = new JComboBox[4];
        JTextField[] maFields = new JTextField[4];

        for (int i = 0; i < 4; i++) {
            int row = 4 + i * 2;
            gbc.gridy = row;
            gbc.gridx = 0;
            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            hoTenFields[i] = new JTextField();
            formPanel.add(hoTenFields[i], gbc);

            gbc.gridy = row + 1;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
            gbc.gridx = 1;
            loaiCombos[i] = new JComboBox<>(loaiOptions);
            formPanel.add(loaiCombos[i], gbc);
            gbc.gridx = 2;
            formPanel.add(new JLabel("Mã:"), gbc);
            gbc.gridx = 3;
            maFields[i] = new JTextField();
            formPanel.add(maFields[i], gbc);

            int index = i;
            loaiCombos[i].addActionListener(e -> {
                String selected = (String) loaiCombos[index].getSelectedItem();
                if ("Không có".equals(selected)) {
                    maFields[index].setText("Trẻ em");
                    maFields[index].setEditable(false);
                } else {
                    maFields[index].setText("");
                    maFields[index].setEditable(true);
                }
            });
        }

        cbSoNguoi.addActionListener(e -> {
            int selected = (int) cbSoNguoi.getSelectedItem();
            for (int i = 0; i < 4; i++) {
                boolean enable = i < selected;
                hoTenFields[i].setEnabled(enable);
                loaiCombos[i].setEnabled(enable);
                maFields[i].setEnabled(enable);
                if (!enable) {
                    hoTenFields[i].setText("");
                    maFields[i].setText("");
                }
            }
        });

        cbSoNguoi.setSelectedIndex(0);
        return formPanel;
    }

    private static JPanel createButtonPanel(Color backgroundColor) {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(backgroundColor);

        JButton btnXacNhan = new JButton("Xác nhận");
        JButton btnQuayLai = new JButton("Quay lại");

        btnPanel.add(btnQuayLai);
        btnPanel.add(btnXacNhan);

        return btnPanel;
    }
}
