
package org.example.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.example.entity.Room;
import org.example.entity.SelectedRoomInfo;
import org.example.service.RoomFinderService;
import java.time.LocalDateTime;
import java.time.Duration;
import org.example.service.BookingService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

         //=== Form tìm kiếm ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
        //formPanel.setBackground(Color.WHITE);
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
        gbc.anchor = GridBagConstraints.WEST;
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
                            currencyFormat.format(BookingService.getAmount(checkIn, checkOut, room.getPrice()))
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




