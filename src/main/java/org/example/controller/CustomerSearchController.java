//package org.example.controller;
//
//import com.github.lgooddatepicker.components.DatePicker;
//import org.example.entity.Room;
//import org.example.entity.SelectedRoomInfo;
//import org.example.service.RoomFinderService;
//import org.example.view.*;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class CustomerSearchController {
//    private final MainFrameView mainFrame;
//    private final JPanel panel;
//    private final JTable table;
//    private final DefaultTableModel tableModel;
//    private final DatePicker dpCheckIn;
//    private final DatePicker dpCheckOut;
//    private final JComboBox<Integer> cbCheckInHour;
//    private final JComboBox<Integer> cbCheckInMinute;
//    private final JComboBox<Integer> cbCheckOutHour;
//    private final JComboBox<Integer> cbCheckOutMinute;
//    private final JComboBox<String> cbLoaiPhong;
//    private final RoomFinderService finder;
//    private final DecimalFormat currencyFormat;
//
//    public CustomerSearchController(
//            MainFrameView mainFrame, JPanel panel, JTable table, DefaultTableModel tableModel,
//            DatePicker dpCheckIn, DatePicker dpCheckOut, JComboBox<Integer> cbCheckInHour,
//            JComboBox<Integer> cbCheckInMinute, JComboBox<Integer> cbCheckOutHour,
//            JComboBox<Integer> cbCheckOutMinute, JComboBox<String> cbLoaiPhong,
//            RoomFinderService finder) {
//        this.mainFrame = mainFrame;
//        this.panel = panel;
//        this.table = table;
//        this.tableModel = tableModel;
//        this.dpCheckIn = dpCheckIn;
//        this.dpCheckOut = dpCheckOut;
//        this.cbCheckInHour = cbCheckInHour;
//        this.cbCheckInMinute = cbCheckInMinute;
//        this.cbCheckOutHour = cbCheckOutHour;
//        this.cbCheckOutMinute = cbCheckOutMinute;
//        this.cbLoaiPhong = cbLoaiPhong;
//        this.finder = finder;
//        this.currencyFormat = new DecimalFormat("#,###");
//    }
//
//    public void handleSearchAction(java.awt.event.ActionEvent e) {
//        try {
//            LocalDate ngayDen = dpCheckIn.getDate();
//            LocalDate ngayDi = dpCheckOut.getDate();
//
//            if (ngayDen == null || ngayDi == null) {
//                JOptionPane.showMessageDialog(panel, "Vui lòng chọn ngày đến và ngày đi.");
//                return;
//            }
//
//            int gioDen = cbCheckInHour.getSelectedIndex();
//            int phutDen = cbCheckInMinute.getSelectedIndex();
//            int gioDi = cbCheckOutHour.getSelectedIndex();
//            int phutDi = cbCheckOutMinute.getSelectedIndex();
//
//            LocalDateTime checkIn = LocalDateTime.of(ngayDen, java.time.LocalTime.of(gioDen, phutDen));
//            LocalDateTime checkOut = LocalDateTime.of(ngayDi, java.time.LocalTime.of(gioDi, phutDi));
//            LocalDateTime now = LocalDateTime.now();
//
//            if (checkIn.isAfter(checkOut)) {
//                JOptionPane.showMessageDialog(panel, "Ngày đến phải trước ngày đi.");
//                return;
//            }
//
//            if (checkIn.isBefore(now)) {
//                JOptionPane.showMessageDialog(panel, "Ngày đến phải sau thời gian hiện tại.");
//                return;
//            }
//
//            String loaiPhong = cbLoaiPhong.getSelectedItem().toString();
//            List<Room> rooms = finder.findAvailableRooms(checkIn, checkOut, loaiPhong);
//
//            tableModel.setRowCount(0);
//            if (rooms.isEmpty()) {
//                JOptionPane.showMessageDialog(panel, "Không có phòng phù hợp.");
//            } else {
//                for (Room room : rooms) {
//                    tableModel.addRow(new Object[]{
//                            room.getRoomId(),
//                            room.getDescription(),
//                            room.getType(),
//                            currencyFormat.format(room.getPrice())
//                    });
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(panel, "Lỗi xử lý dữ liệu!");
//        }
//    }
//
//    public void handleBookAction(java.awt.event.ActionEvent e) {
//        if (tableModel.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(panel, "Bạn chưa chọn phòng nào để đặt.");
//            return;
//        }
//
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(panel, "Vui lòng chọn một phòng để đặt.");
//            return;
//        }
//
//        try {
//            String roomId = tableModel.getValueAt(selectedRow, 0).toString();
//            String description = tableModel.getValueAt(selectedRow, 1).toString();
//            String type = tableModel.getValueAt(selectedRow, 2).toString();
//            String priceStr = tableModel.getValueAt(selectedRow, 3).toString().replace(",", "");
//            double price = Double.parseDouble(priceStr);
//
//            LocalDateTime checkIn = LocalDateTime.of(dpCheckIn.getDate(), java.time.LocalTime.of(
//                    cbCheckInHour.getSelectedIndex(), cbCheckInMinute.getSelectedIndex()));
//
//            LocalDateTime checkOut = LocalDateTime.of(dpCheckOut.getDate(), java.time.LocalTime.of(
//                    cbCheckOutHour.getSelectedIndex(), cbCheckOutMinute.getSelectedIndex()));
//
//            SelectedRoomInfo selectedRoom = new SelectedRoomInfo(roomId, description, type, price, checkIn, checkOut);
//
//            JPanel bookingPanel = CustomerBookingView.createBookingPanel(mainFrame, mainFrame.getLoggedInUsername(), selectedRoom);
//            mainFrame.setCustomerDynamicContent(bookingPanel);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(panel, "Lỗi khi xử lý dữ liệu phòng đã chọn.");
//        }
//    }
//}
