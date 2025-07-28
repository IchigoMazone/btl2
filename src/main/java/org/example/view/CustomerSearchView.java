
package org.example.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.example.entity.Room;
import org.example.entity.SelectedRoomInfo;
import org.example.service.RoomFinderService;
import org.example.service.BookingService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerSearchView {

    public static JPanel createSearchPanel(MainFrameView mainFrame) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

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

        JComboBox<String> cbLoaiPhong = new JComboBox<>(new String[]{
                "Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"
        });

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 15, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 15, 20);
        formPanel.add(new JLabel("Ngày đến:"), gbc);
        gbc.gridx = 1; formPanel.add(dpCheckIn, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3; formPanel.add(cbCheckInHour, gbc);
        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5; formPanel.add(cbCheckInMinute, gbc);

        gbc.gridx = 7;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Loại phòng:"), gbc);
        gbc.gridx = 8;
        formPanel.add(cbLoaiPhong, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Ngày đi:"), gbc);
        gbc.gridx = 1; formPanel.add(dpCheckOut, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3; formPanel.add(cbCheckOutHour, gbc);
        gbc.gridx = 4; formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5; formPanel.add(cbCheckOutMinute, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTimKiem = new JButton("Tìm kiếm");
        JButton btnDatPhong = new JButton("Đặt phòng");
        btnPanel.add(btnTimKiem);
        btnPanel.add(btnDatPhong);

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

        RoomFinderService finder = new RoomFinderService("rooms.xml", "bookings.xml");
        DecimalFormat currencyFormat = new DecimalFormat("#,###");

        btnTimKiem.addActionListener(e -> {
            try {
                LocalDate ngayDen = dpCheckIn.getDate();
                LocalDate ngayDi = dpCheckOut.getDate();

                if (ngayDen == null || ngayDi == null) {
                    JOptionPane.showMessageDialog(panel, "Vui lòng chọn ngày đến và ngày đi.");
                    return;
                }

                int gioDen = cbCheckInHour.getSelectedIndex();
                int phutDen = cbCheckInMinute.getSelectedIndex();
                int gioDi = cbCheckOutHour.getSelectedIndex();
                int phutDi = cbCheckOutMinute.getSelectedIndex();

                LocalDateTime checkIn = LocalDateTime.of(ngayDen, java.time.LocalTime.of(gioDen, phutDen));
                LocalDateTime checkOut = LocalDateTime.of(ngayDi, java.time.LocalTime.of(gioDi, phutDi));
                LocalDateTime now = LocalDateTime.now();

                if (checkIn.isAfter(checkOut)) {
                    JOptionPane.showMessageDialog(panel, "Ngày đến phải trước ngày đi.");
                    return;
                }

                if (checkIn.isBefore(now)) {
                    JOptionPane.showMessageDialog(panel, "Ngày đến phải sau thời gian hiện tại.");
                    return;
                }

                Duration duration = Duration.between(LocalDateTime.now(), checkIn);
                if (!checkIn.isBefore(LocalDateTime.now()) && duration.toHours() < 4) {
                    JOptionPane.showMessageDialog(panel, "Đặt phòng cần trước check-in ít nhất 4 giờ");
                    return;
                }

                String loaiPhong = cbLoaiPhong.getSelectedItem().toString();
                List<Room> rooms = finder.findAvailableRooms(checkIn, checkOut, loaiPhong);

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

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Lỗi xử lý dữ liệu!");
            }
        });

        btnDatPhong.addActionListener(e -> {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "Bạn chưa chọn phòng nào để đặt.");
                return;
            }

            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn một phòng để đặt.");
                return;
            }

            try {
                String roomId = tableModel.getValueAt(selectedRow, 0).toString();
                String description = tableModel.getValueAt(selectedRow, 1).toString();
                String type = tableModel.getValueAt(selectedRow, 2).toString();
                String priceStr = tableModel.getValueAt(selectedRow, 3).toString().replace(",", "");
                double price = Double.parseDouble(priceStr);

                LocalDateTime checkIn = LocalDateTime.of(dpCheckIn.getDate(), java.time.LocalTime.of(
                        cbCheckInHour.getSelectedIndex(), cbCheckInMinute.getSelectedIndex()));

                LocalDateTime checkOut = LocalDateTime.of(dpCheckOut.getDate(), java.time.LocalTime.of(
                        cbCheckOutHour.getSelectedIndex(), cbCheckOutMinute.getSelectedIndex()));

                SelectedRoomInfo selectedRoom = new SelectedRoomInfo(roomId, description, type, price, checkIn, checkOut);

                JPanel bookingPanel = CustomerBookingView.createBookingPanel(mainFrame, mainFrame.getLoggedInUsername(), selectedRoom);
                mainFrame.setCustomerDynamicContent(bookingPanel);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Lỗi khi xử lý dữ liệu phòng đã chọn.");
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }
}
