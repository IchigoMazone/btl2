//package org.example.view;
//
//import com.github.lgooddatepicker.components.DatePicker;
//import com.github.lgooddatepicker.components.DatePickerSettings;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//
//public class AdminSearchView {
//    public static JPanel createSearchPanel() {
//        JPanel panel = new JPanel(new BorderLayout(10,10));
//        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//        panel.setBackground(Color.WHITE);
//
//        // Tạo DatePicker cho Ngày đến và Ngày đi
//        DatePickerSettings dateSettingsCheckIn = new DatePickerSettings();
//        dateSettingsCheckIn.setFormatForDatesCommonEra("dd-MM-yyyy");
//        DatePicker dpCheckIn = new DatePicker(dateSettingsCheckIn);
//
//        DatePickerSettings dateSettingsCheckOut = new DatePickerSettings();
//        dateSettingsCheckOut.setFormatForDatesCommonEra("dd-MM-yyyy");
//        DatePicker dpCheckOut = new DatePicker(dateSettingsCheckOut);
//
//        Integer[] hours = new Integer[24];
//        for (int i = 0; i < 24; i++) hours[i] = i;
//        Integer[] minutes = new Integer[60];
//        for (int i = 0; i < 60; i++) minutes[i] = i;
//
//        JComboBox<Integer> cbCheckInHour = new JComboBox<>(hours);
//        JComboBox<Integer> cbCheckInMinute = new JComboBox<>(minutes);
//        JComboBox<Integer> cbCheckOutHour = new JComboBox<>(hours);
//        JComboBox<Integer> cbCheckOutMinute = new JComboBox<>(minutes);
//
//        Dimension comboSize = new Dimension(60, cbCheckInHour.getPreferredSize().height);
//        cbCheckInHour.setPreferredSize(comboSize);
//        cbCheckInMinute.setPreferredSize(comboSize);
//        cbCheckOutHour.setPreferredSize(comboSize);
//        cbCheckOutMinute.setPreferredSize(comboSize);
//
//        JComboBox<String> cbLoaiPhong = new JComboBox<>(new String[]{"Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"});
//
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 15, 5);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        // Hàng 1: Ngày đến + giờ + phút
//        gbc.gridx = 0; gbc.gridy = 0;
//        gbc.insets = new Insets(5, 10, 15, 20);
//
//        formPanel.add(new JLabel("Ngày đến:"), gbc);
//        gbc.gridx = 1;
//        formPanel.add(dpCheckIn, gbc);
//        gbc.gridx = 2;
//        formPanel.add(new JLabel("Giờ:"), gbc);
//        gbc.gridx = 3;
//        formPanel.add(cbCheckInHour, gbc);
//        gbc.gridx = 4;
//        formPanel.add(new JLabel("Phút:"), gbc);
//        gbc.gridx = 5;
//        formPanel.add(cbCheckInMinute, gbc);
//
//        // Hàng 1 tiếp: Loại phòng lùi phải
//        gbc.gridx = 7;
//        gbc.weightx = 0;  // để đẩy sang phải
//        gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Loại phòng:"), gbc);
//
//        gbc.gridx = 8;
//        gbc.weightx = 0;
//        formPanel.add(cbLoaiPhong, gbc);
//
//        // Hàng 2: Ngày đi + giờ + phút lùi trái
//        gbc.gridx = 0; gbc.gridy = 1;
//        gbc.weightx = 0;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(new JLabel("Ngày đi:"), gbc);
//        gbc.gridx = 1;
//        formPanel.add(dpCheckOut, gbc);
//        gbc.gridx = 2;
//        formPanel.add(new JLabel("Giờ:"), gbc);
//        gbc.gridx = 3;
//        formPanel.add(cbCheckOutHour, gbc);
//        gbc.gridx = 4;
//        formPanel.add(new JLabel("Phút:"), gbc);
//        gbc.gridx = 5;
//        formPanel.add(cbCheckOutMinute, gbc);
//
//        // Nút Tìm kiếm và Đặt phòng
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnTimKiem = new JButton("Tìm kiếm");
//        JButton btnDatPhong = new JButton("Đặt phòng");
//        btnPanel.add(btnTimKiem);
//        btnPanel.add(btnDatPhong);
//
//        // Bảng kết quả
//        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
//        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
//            @Override
//            public boolean isCellEditable(int row, int column) { return false; }
//        };
//        JTable table = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));
//
//        panel.add(formPanel, BorderLayout.NORTH);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(btnPanel, BorderLayout.SOUTH);
//
//        return panel;
//    }
//}
//

package org.example.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminSearchView {
    public static JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // DatePicker cấu hình định dạng
        DatePickerSettings dateSettingsIn = new DatePickerSettings();
        dateSettingsIn.setFormatForDatesCommonEra("dd-MM-yyyy");
        DatePicker dpIn = new DatePicker(dateSettingsIn);

        DatePickerSettings dateSettingsOut = new DatePickerSettings();
        dateSettingsOut.setFormatForDatesCommonEra("dd-MM-yyyy");
        DatePicker dpOut = new DatePicker(dateSettingsOut);

        // Giờ, phút
        Integer[] hours = new Integer[24];
        Integer[] minutes = new Integer[60];
        for (int i = 0; i < 24; i++) hours[i] = i;
        for (int i = 0; i < 60; i++) minutes[i] = i;

        JComboBox<Integer> cbHourIn = new JComboBox<>(hours);
        JComboBox<Integer> cbMinIn = new JComboBox<>(minutes);
        JComboBox<Integer> cbHourOut = new JComboBox<>(hours);
        JComboBox<Integer> cbMinOut = new JComboBox<>(minutes);

        // Loại phòng
        JComboBox<String> cbType = new JComboBox<>(new String[]{
                "Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"
        });

        // Tạo form tìm kiếm
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Hàng 1: Ngày đến
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Ngày đến:"), gbc);
        gbc.gridx = 1; formPanel.add(dpIn, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3; formPanel.add(cbHourIn, gbc);
        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5; formPanel.add(cbMinIn, gbc);
        gbc.gridx = 6; formPanel.add(new JLabel("Loại phòng:"), gbc);
        gbc.gridx = 7; formPanel.add(cbType, gbc);

        // Hàng 2: Ngày đi
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ngày đi:"), gbc);
        gbc.gridx = 1; formPanel.add(dpOut, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3; formPanel.add(cbHourOut, gbc);
        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5; formPanel.add(cbMinOut, gbc);

        // Bảng kết quả
        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));

        // Nút tìm kiếm
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSearch = new JButton("Tìm kiếm");
        btnPanel.add(btnSearch);

        // Hành động tìm kiếm
        btnSearch.addActionListener(e -> {
            String inDate = dpIn.getText();
            String outDate = dpOut.getText();
            int inHour = (Integer) cbHourIn.getSelectedItem();
            int inMin = (Integer) cbMinIn.getSelectedItem();
            int outHour = (Integer) cbHourOut.getSelectedItem();
            int outMin = (Integer) cbMinOut.getSelectedItem();
            String roomType = (String) cbType.getSelectedItem();

            // Hiển thị log để kiểm tra
            System.out.println("➡️ Ngày đến: " + inDate + " " + inHour + ":" + inMin);
            System.out.println("➡️ Ngày đi: " + outDate + " " + outHour + ":" + outMin);
            System.out.println("➡️ Loại phòng: " + roomType);

            // Xóa dữ liệu cũ, thêm dữ liệu giả lập mới
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"P101", "Phòng đơn view biển", "Phòng đơn", "500.000"});
            tableModel.addRow(new Object[]{"P205", "Phòng đôi có ban công", "Phòng đôi", "850.000"});
        });

        // Gắn các thành phần vào panel chính
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
}

