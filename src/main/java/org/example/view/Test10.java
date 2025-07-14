import com.formdev.flatlaf.FlatLightLaf;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class Test10 {

    public static JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(950, 800));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        panel.add(createFormPanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButtonPanel());

        return panel;
    }

    private static JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIManager.getColor("Panel.background"));
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
        Integer[] soNguoiOptions = {0, 1, 2, 3};
        JComboBox<Integer> cbSoNguoi = new JComboBox<>(soNguoiOptions);
        formPanel.add(cbSoNguoi, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh"});
        formPanel.add(cbLoaiChung, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Mã:"), gbc);
        gbc.gridx = 3;
        JTextField tfMaChung = new JTextField();
        formPanel.add(tfMaChung, gbc);

        String[] loaiOptions = {"Không có", "Mã định danh"};
        JTextField[] hoTenFields = new JTextField[3];
        JComboBox<String>[] loaiCombos = new JComboBox[3];
        JTextField[] maFields = new JTextField[3];

        for (int i = 0; i < 3; i++) {
            int row = i * 2 + 2;

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
            for (int i = 0; i < 3; i++) {
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

    private static JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton btnXacNhan = new JButton("Xác nhận");
        JButton btnQuayLai = new JButton("Quay lại");

        btnPanel.add(btnQuayLai);
        btnPanel.add(btnXacNhan);

        return btnPanel;
    }
}