package org.example.controller;

import com.github.lgooddatepicker.components.DatePicker;
import org.example.entity.Room;
import org.example.entity.SelectedRoomInfo;
import org.example.service.RoomFinderService;
import org.example.view.MainFrameView;
import org.example.view.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminSearchController {
    private final MainFrameView mainFrame;
    private final JPanel panel;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final DatePicker dpIn;
    private final DatePicker dpOut;
    private final JComboBox<Integer> cbHourIn;
    private final JComboBox<Integer> cbMinIn;
    private final JComboBox<Integer> cbHourOut;
    private final JComboBox<Integer> cbMinOut;
    private final JComboBox<String> cbType;
    private final RoomFinderService finder;
    private final DecimalFormat currencyFormat;

    public AdminSearchController(
            MainFrameView mainFrame, JPanel panel, JTable table, DefaultTableModel tableModel,
            DatePicker dpIn, DatePicker dpOut, JComboBox<Integer> cbHourIn, JComboBox<Integer> cbMinIn,
            JComboBox<Integer> cbHourOut, JComboBox<Integer> cbMinOut, JComboBox<String> cbType,
            RoomFinderService finder) {
        this.mainFrame = mainFrame;
        this.panel = panel;
        this.table = table;
        this.tableModel = tableModel;
        this.dpIn = dpIn;
        this.dpOut = dpOut;
        this.cbHourIn = cbHourIn;
        this.cbMinIn = cbMinIn;
        this.cbHourOut = cbHourOut;
        this.cbMinOut = cbMinOut;
        this.cbType = cbType;
        this.finder = finder;
        this.currencyFormat = new DecimalFormat("#,###");
    }

    public void handleSearchAction(java.awt.event.ActionEvent e) {
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
    }

    public void handleBookAction(java.awt.event.ActionEvent e) {
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
    }
}