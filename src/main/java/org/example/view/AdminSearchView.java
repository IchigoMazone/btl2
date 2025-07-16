////
////
////package org.example.view;
////
////import com.github.lgooddatepicker.components.DatePicker;
////import com.github.lgooddatepicker.components.DatePickerSettings;
////
////import javax.swing.*;
////import javax.swing.border.TitledBorder;
////import javax.swing.table.DefaultTableModel;
////import java.awt.*;
////
////public class AdminSearchView {
////    public static JPanel createSearchPanel() {
////        JPanel panel = new JPanel(new BorderLayout(10, 10));
////        panel.setBackground(Color.WHITE);
////
////        // DatePicker cấu hình định dạng
////        DatePickerSettings dateSettingsIn = new DatePickerSettings();
////        dateSettingsIn.setFormatForDatesCommonEra("dd-MM-yyyy");
////        DatePicker dpIn = new DatePicker(dateSettingsIn);
////
////        DatePickerSettings dateSettingsOut = new DatePickerSettings();
////        dateSettingsOut.setFormatForDatesCommonEra("dd-MM-yyyy");
////        DatePicker dpOut = new DatePicker(dateSettingsOut);
////
////        // Giờ, phút
////        Integer[] hours = new Integer[24];
////        Integer[] minutes = new Integer[60];
////        for (int i = 0; i < 24; i++) hours[i] = i;
////        for (int i = 0; i < 60; i++) minutes[i] = i;
////
////        JComboBox<Integer> cbHourIn = new JComboBox<>(hours);
////        JComboBox<Integer> cbMinIn = new JComboBox<>(minutes);
////        JComboBox<Integer> cbHourOut = new JComboBox<>(hours);
////        JComboBox<Integer> cbMinOut = new JComboBox<>(minutes);
////
////        // Loại phòng
////        JComboBox<String> cbType = new JComboBox<>(new String[]{
////                "Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"
////        });
////
////        // Tạo form tìm kiếm
////        JPanel formPanel = new JPanel(new GridBagLayout());
////        formPanel.setBorder(BorderFactory.createTitledBorder(
////                BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(5, 10, 10, 5);
////        gbc.anchor = GridBagConstraints.WEST;
////
////        // Hàng 1: Ngày đến
////        gbc.gridx = 0; gbc.gridy = 0;
////        formPanel.add(new JLabel("Ngày đến:"), gbc);
////        gbc.gridx = 1; formPanel.add(dpIn, gbc);
////        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
////        gbc.gridx = 3; formPanel.add(cbHourIn, gbc);
////        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
////        gbc.gridx = 5; formPanel.add(cbMinIn, gbc);
////        gbc.gridx = 6; formPanel.add(new JLabel("Loại phòng:"), gbc);
////        gbc.gridx = 7; formPanel.add(cbType, gbc);
////
////        // Hàng 2: Ngày đi
////        gbc.gridx = 0; gbc.gridy = 1;
////        formPanel.add(new JLabel("Ngày đi:"), gbc);
////        gbc.gridx = 1; formPanel.add(dpOut, gbc);
////        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
////        gbc.gridx = 3; formPanel.add(cbHourOut, gbc);
////        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
////        gbc.gridx = 5; formPanel.add(cbMinOut, gbc);
////
////        // Bảng kết quả
////        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
////        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
////            @Override
////            public boolean isCellEditable(int row, int column) { return false; }
////        };
////        JTable table = new JTable(tableModel);
////        JScrollPane scrollPane = new JScrollPane(table);
////        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));
////
////        // Nút tìm kiếm
////        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
////        JButton btnSearch = new JButton("Tìm kiếm");
////        btnPanel.add(btnSearch);
////
////        // Hành động tìm kiếm
////        btnSearch.addActionListener(e -> {
////            String inDate = dpIn.getText();
////            String outDate = dpOut.getText();
////            int inHour = (Integer) cbHourIn.getSelectedItem();
////            int inMin = (Integer) cbMinIn.getSelectedItem();
////            int outHour = (Integer) cbHourOut.getSelectedItem();
////            int outMin = (Integer) cbMinOut.getSelectedItem();
////            String roomType = (String) cbType.getSelectedItem();
////
////            // Hiển thị log để kiểm tra
////            System.out.println("➡️ Ngày đến: " + inDate + " " + inHour + ":" + inMin);
////            System.out.println("➡️ Ngày đi: " + outDate + " " + outHour + ":" + outMin);
////            System.out.println("➡️ Loại phòng: " + roomType);
////
////            // Xóa dữ liệu cũ, thêm dữ liệu giả lập mới
////            tableModel.setRowCount(0);
////            tableModel.addRow(new Object[]{"P101", "Phòng đơn view biển", "Phòng đơn", "500.000"});
////            tableModel.addRow(new Object[]{"P205", "Phòng đôi có ban công", "Phòng đôi", "850.000"});
////        });
////
////        // Gắn các thành phần vào panel chính
////        panel.add(formPanel, BorderLayout.NORTH);
////        panel.add(scrollPane, BorderLayout.CENTER);
////        panel.add(btnPanel, BorderLayout.SOUTH);
////
////        return panel;
////    }
////}
////
//
//
//
//package org.example.view;
//
//import com.github.lgooddatepicker.components.DatePicker;
//import com.github.lgooddatepicker.components.DatePickerSettings;
//import org.example.entity.Room;
//import org.example.entity.SelectedRoomInfo;
//import org.example.service.RoomFinderService;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class AdminSearchView {
//
//    public static JPanel createSearchPanel(MainFrameView mainFrame) {
//        JPanel panel = new JPanel(new BorderLayout(10, 10));
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//
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
//        JComboBox<String> cbLoaiPhong = new JComboBox<>(new String[]{
//                "Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"
//        });
//
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBorder(BorderFactory.createTitledBorder(
//                BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 15, 5);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        gbc.gridx = 0; gbc.gridy = 0;
//        gbc.insets = new Insets(5, 10, 15, 20);
//        formPanel.add(new JLabel("Ngày đến:"), gbc);
//        gbc.gridx = 1; formPanel.add(dpCheckIn, gbc);
//        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
//        gbc.gridx = 3; formPanel.add(cbCheckInHour, gbc);
//        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
//        gbc.gridx = 5; formPanel.add(cbCheckInMinute, gbc);
//
//        gbc.gridx = 7;
//        gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Loại phòng:"), gbc);
//        gbc.gridx = 8;
//        formPanel.add(cbLoaiPhong, gbc);
//
//        gbc.gridx = 0; gbc.gridy = 1;
//        gbc.anchor = GridBagConstraints.WEST;
//        formPanel.add(new JLabel("Ngày đi:"), gbc);
//        gbc.gridx = 1; formPanel.add(dpCheckOut, gbc);
//        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
//        gbc.gridx = 3; formPanel.add(cbCheckOutHour, gbc);
//        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
//        gbc.gridx = 5; formPanel.add(cbCheckOutMinute, gbc);
//
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnTimKiem = new JButton("Tìm kiếm");
//        JButton btnDatPhong = new JButton("Đặt phòng");
//        btnPanel.add(btnTimKiem);
//        btnPanel.add(btnDatPhong);
//
//        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
//        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
//            public boolean isCellEditable(int row, int col) { return false; }
//        };
//
//        JTable table = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));
//
//        table.getColumnModel().getColumn(0).setPreferredWidth(80);
//        table.getColumnModel().getColumn(1).setPreferredWidth(300);
//        table.getColumnModel().getColumn(2).setPreferredWidth(100);
//        table.getColumnModel().getColumn(3).setPreferredWidth(120);
//
//        RoomFinderService finder = new RoomFinderService("rooms.xml", "bookings.xml");
//        DecimalFormat currencyFormat = new DecimalFormat("#,###");
//
//        btnTimKiem.addActionListener(e -> {
//            try {
//                LocalDate ngayDen = dpCheckIn.getDate();
//                LocalDate ngayDi = dpCheckOut.getDate();
//
//                if (ngayDen == null || ngayDi == null) {
//                    JOptionPane.showMessageDialog(panel, "Vui lòng chọn ngày đến và ngày đi.");
//                    return;
//                }
//
//                int gioDen = cbCheckInHour.getSelectedIndex();
//                int phutDen = cbCheckInMinute.getSelectedIndex();
//                int gioDi = cbCheckOutHour.getSelectedIndex();
//                int phutDi = cbCheckOutMinute.getSelectedIndex();
//
//                LocalDateTime checkIn = LocalDateTime.of(ngayDen, java.time.LocalTime.of(gioDen, phutDen));
//                LocalDateTime checkOut = LocalDateTime.of(ngayDi, java.time.LocalTime.of(gioDi, phutDi));
//                LocalDateTime now = LocalDateTime.now();
//
//                if (checkIn.isAfter(checkOut)) {
//                    JOptionPane.showMessageDialog(panel, "Ngày đến phải trước ngày đi.");
//                    return;
//                }
//
//                if (checkIn.isBefore(now)) {
//                    JOptionPane.showMessageDialog(panel, "Ngày đến phải sau thời gian hiện tại.");
//                    return;
//                }
//
//                String loaiPhong = cbLoaiPhong.getSelectedItem().toString();
//                List<Room> rooms = finder.findAvailableRooms(checkIn, checkOut, loaiPhong);
//
//                tableModel.setRowCount(0);
//                if (rooms.isEmpty()) {
//                    JOptionPane.showMessageDialog(panel, "Không có phòng phù hợp.");
//                } else {
//                    for (Room room : rooms) {
//                        tableModel.addRow(new Object[]{
//                                room.getRoomId(),
//                                room.getDescription(),
//                                room.getType(),
//                                currencyFormat.format(room.getPrice())
//                        });
//                    }
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(panel, "Lỗi xử lý dữ liệu!");
//            }
//        });
//
//        btnDatPhong.addActionListener(e -> {
//            if (tableModel.getRowCount() == 0) {
//                JOptionPane.showMessageDialog(panel, "Bạn chưa chọn phòng nào để đặt.");
//                return;
//            }
//
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(panel, "Vui lòng chọn một phòng để đặt.");
//                return;
//            }
//
//            try {
//                String roomId = tableModel.getValueAt(selectedRow, 0).toString();
//                String description = tableModel.getValueAt(selectedRow, 1).toString();
//                String type = tableModel.getValueAt(selectedRow, 2).toString();
//                String priceStr = tableModel.getValueAt(selectedRow, 3).toString().replace(",", "");
//                double price = Double.parseDouble(priceStr);
//
//                LocalDateTime checkIn = LocalDateTime.of(dpCheckIn.getDate(), java.time.LocalTime.of(
//                        cbCheckInHour.getSelectedIndex(), cbCheckInMinute.getSelectedIndex()));
//
//                LocalDateTime checkOut = LocalDateTime.of(dpCheckOut.getDate(), java.time.LocalTime.of(
//                        cbCheckOutHour.getSelectedIndex(), cbCheckOutMinute.getSelectedIndex()));
//
//                SelectedRoomInfo selectedRoom = new SelectedRoomInfo(roomId, description, type, price, checkIn, checkOut);
//
//                JPanel bookingPanel = AdminBookingView.createBookingPanel(mainFrame, selectedRoom);
//                mainFrame.setAdminDynamicContent(bookingPanel);
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(panel, "Lỗi khi xử lý dữ liệu phòng đã chọn.");
//            }
//        });
//
//        panel.add(formPanel, BorderLayout.NORTH);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(btnPanel, BorderLayout.SOUTH);
//        return panel;
//    }
//}


package org.example.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.example.entity.Room;
import org.example.entity.SelectedRoomInfo;
import org.example.service.RoomFinderService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminSearchView {

    public static JPanel createSearchPanel(MainFrameView mainFrame) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Date pickers
        DatePickerSettings inSettings = new DatePickerSettings();
        inSettings.setFormatForDatesCommonEra("dd-MM-yyyy");
        DatePicker dpIn = new DatePicker(inSettings);

        DatePickerSettings outSettings = new DatePickerSettings();
        outSettings.setFormatForDatesCommonEra("dd-MM-yyyy");
        DatePicker dpOut = new DatePicker(outSettings);

        // Giờ, phút
        Integer[] hours = new Integer[24];
        Integer[] mins = new Integer[60];
        for (int i = 0; i < 24; i++) hours[i] = i;
        for (int i = 0; i < 60; i++) mins[i] = i;

        JComboBox<Integer> cbHourIn = new JComboBox<>(hours);
        JComboBox<Integer> cbMinIn = new JComboBox<>(mins);
        JComboBox<Integer> cbHourOut = new JComboBox<>(hours);
        JComboBox<Integer> cbMinOut = new JComboBox<>(mins);

        Dimension comboSize = new Dimension(60, cbHourIn.getPreferredSize().height);
        cbHourIn.setPreferredSize(comboSize);
        cbMinIn.setPreferredSize(comboSize);
        cbHourOut.setPreferredSize(comboSize);
        cbMinOut.setPreferredSize(comboSize);

        JComboBox<String> cbType = new JComboBox<>(new String[]{
                "Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"
        });

        // === Form tìm kiếm ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Ngày đến:"), gbc);
        gbc.gridx = 1; formPanel.add(dpIn, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3; formPanel.add(cbHourIn, gbc);
        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5; formPanel.add(cbMinIn, gbc);
        gbc.gridx = 6; formPanel.add(new JLabel("Loại phòng:"), gbc);
        gbc.gridx = 7; formPanel.add(cbType, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ngày đi:"), gbc);
        gbc.gridx = 1; formPanel.add(dpOut, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3; formPanel.add(cbHourOut, gbc);
        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5; formPanel.add(cbMinOut, gbc);

        // === Bảng kết quả ===
        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        // === Nút chức năng ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSearch = new JButton("Tìm kiếm");
        JButton btnBook = new JButton("Đặt phòng");
        btnPanel.add(btnSearch);
        btnPanel.add(btnBook);

        // === Xử lý tìm kiếm ===
        RoomFinderService finder = new RoomFinderService("rooms.xml", "bookings.xml");
        DecimalFormat currencyFormat = new DecimalFormat("#,###");

        btnSearch.addActionListener(e -> {
            LocalDate ngayDen = dpIn.getDate();
            LocalDate ngayDi = dpOut.getDate();

            if (ngayDen == null || ngayDi == null) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn đầy đủ ngày đến và ngày đi.");
                return;
            }

            LocalDateTime checkIn = LocalDateTime.of(ngayDen,
                    java.time.LocalTime.of(cbHourIn.getSelectedIndex(), cbMinIn.getSelectedIndex()));
            LocalDateTime checkOut = LocalDateTime.of(ngayDi,
                    java.time.LocalTime.of(cbHourOut.getSelectedIndex(), cbMinOut.getSelectedIndex()));

            if (checkIn.isAfter(checkOut)) {
                JOptionPane.showMessageDialog(panel, "Ngày đến phải trước ngày đi.");
                return;
            }

            if (checkIn.isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(panel, "Ngày đến phải sau thời gian hiện tại.");
                return;
            }

            String type = cbType.getSelectedItem().toString();
            List<Room> rooms = finder.findAvailableRooms(checkIn, checkOut, type);

            tableModel.setRowCount(0);
            if (rooms.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Không có phòng phù hợp.");
            } else {
                for (Room room : rooms) {
                    tableModel.addRow(new Object[]{
                            room.getRoomId(),
                            room.getDescription(),
                            room.getType(),
                            currencyFormat.format(room.getPrice())
                    });
                }
            }
        });

        // === Xử lý đặt phòng ===
        btnBook.addActionListener(e -> {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "Bạn chưa tìm kiếm hoặc không có phòng.");
                return;
            }

            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn một phòng để đặt.");
                return;
            }

            try {
                String roomId = tableModel.getValueAt(row, 0).toString();
                String desc = tableModel.getValueAt(row, 1).toString();
                String type = tableModel.getValueAt(row, 2).toString();
                double price = Double.parseDouble(tableModel.getValueAt(row, 3).toString().replace(",", ""));

                LocalDateTime checkIn = LocalDateTime.of(dpIn.getDate(),
                        java.time.LocalTime.of(cbHourIn.getSelectedIndex(), cbMinIn.getSelectedIndex()));
                LocalDateTime checkOut = LocalDateTime.of(dpOut.getDate(),
                        java.time.LocalTime.of(cbHourOut.getSelectedIndex(), cbMinOut.getSelectedIndex()));

                SelectedRoomInfo info = new SelectedRoomInfo(roomId, desc, type, price, checkIn, checkOut);
                JPanel bookingPanel = AdminBookingView.createBookingPanel(mainFrame, info);
                mainFrame.setAdminDynamicContent(bookingPanel);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Lỗi xử lý dữ liệu phòng.");
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
}
