package org.example.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerSearchView {
    public static JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.setBackground(Color.WHITE);

        // Tạo DatePicker cho Ngày đến và Ngày đi
        DatePickerSettings dateSettingsCheckIn = new DatePickerSettings();
        dateSettingsCheckIn.setFormatForDatesCommonEra("dd-MM-yyyy");
        DatePicker dpCheckIn = new DatePicker(dateSettingsCheckIn);

        DatePickerSettings dateSettingsCheckOut = new DatePickerSettings();
        dateSettingsCheckOut.setFormatForDatesCommonEra("dd-MM-yyyy");
        DatePicker dpCheckOut = new DatePicker(dateSettingsCheckOut);

        Integer[] hours = new Integer[24];
        for (int i = 0; i < 24; i++) hours[i] = i;
        Integer[] minutes = new Integer[60];
        for (int i = 0; i < 60; i++) minutes[i] = i;

        JComboBox<Integer> cbCheckInHour = new JComboBox<>(hours);
        JComboBox<Integer> cbCheckInMinute = new JComboBox<>(minutes);
        JComboBox<Integer> cbCheckOutHour = new JComboBox<>(hours);
        JComboBox<Integer> cbCheckOutMinute = new JComboBox<>(minutes);

        Dimension comboSize = new Dimension(60, cbCheckInHour.getPreferredSize().height);
        cbCheckInHour.setPreferredSize(comboSize);
        cbCheckInMinute.setPreferredSize(comboSize);
        cbCheckOutHour.setPreferredSize(comboSize);
        cbCheckOutMinute.setPreferredSize(comboSize);

        JComboBox<String> cbLoaiPhong = new JComboBox<>(new String[]{"Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"});

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 15, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Hàng 1: Ngày đến + giờ + phút
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 15, 20);

        formPanel.add(new JLabel("Ngày đến:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dpCheckIn, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cbCheckInHour, gbc);
        gbc.gridx = 4;
        formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5;
        formPanel.add(cbCheckInMinute, gbc);

        // Hàng 1 tiếp: Loại phòng lùi phải
        gbc.gridx = 7;
        gbc.weightx = 0;  // để đẩy sang phải
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Loại phòng:"), gbc);

        gbc.gridx = 8;
        gbc.weightx = 0;
        formPanel.add(cbLoaiPhong, gbc);

        // Hàng 2: Ngày đi + giờ + phút lùi trái
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Ngày đi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dpCheckOut, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cbCheckOutHour, gbc);
        gbc.gridx = 4;
        formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5;
        formPanel.add(cbCheckOutMinute, gbc);

        // Nút Tìm kiếm và Đặt phòng
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTimKiem = new JButton("Tìm kiếm");
        JButton btnDatPhong = new JButton("Đặt phòng");
        btnPanel.add(btnTimKiem);
        btnPanel.add(btnDatPhong);

        // Bảng kết quả
        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
}
