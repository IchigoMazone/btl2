package org.example.action;

import org.example.controller.AdminSearchController;
import org.example.entity.Room;
import org.example.entity.SelectedRoomInfo;
import org.example.view.AdminBookingView;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CheckAdminSearch {
    private AdminSearchController controller;

    public CheckAdminSearch(AdminSearchController controller) {
        this.controller = controller;
    }

    public void handleSearch() {
        try {
            LocalDate ngayDen = controller.getView().getDpIn().getDate();
            LocalDate ngayDi = controller.getView().getDpOut().getDate();

            if (ngayDen == null || ngayDi == null) {
                JOptionPane.showMessageDialog(controller.getPanel(), "Vui lòng chọn đầy đủ ngày đến và ngày đi.");
                return;
            }

            int gioDen = controller.getView().getCbHourIn().getSelectedIndex();
            int phutDen = controller.getView().getCbMinIn().getSelectedIndex();
            int gioDi = controller.getView().getCbHourOut().getSelectedIndex();
            int phutDi = controller.getView().getCbMinOut().getSelectedIndex();

            LocalDateTime checkIn = LocalDateTime.of(ngayDen, java.time.LocalTime.of(gioDen, phutDen));
            LocalDateTime checkOut = LocalDateTime.of(ngayDi, java.time.LocalTime.of(gioDi, phutDi));
            LocalDateTime now = LocalDateTime.now();

            if (checkIn.isAfter(checkOut)) {
                JOptionPane.showMessageDialog(controller.getPanel(), "Ngày đến phải trước ngày đi.");
                return;
            }

            if (checkIn.isBefore(now)) {
                JOptionPane.showMessageDialog(controller.getPanel(), "Ngày đến phải sau thời gian hiện tại.");
                return;
            }

            String type = controller.getView().getCbType().getSelectedItem().toString();
            List<Room> rooms = controller.getFinder().findAvailableRooms(checkIn, checkOut, type);

            if (rooms.isEmpty()) {
                JOptionPane.showMessageDialog(controller.getPanel(), "Không có phòng phù hợp.");
            } else {
                controller.displayRooms(rooms, checkIn, checkOut);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(controller.getPanel(), "Lỗi xử lý dữ liệu!");
        }
    }

    public void handleBooking() {
        if (controller.getView().getTableModel().getRowCount() == 0) {
            JOptionPane.showMessageDialog(controller.getPanel(), "Bạn chưa tìm kiếm hoặc không có phòng.");
            return;
        }

        SelectedRoomInfo selectedRoom = controller.getSelectedRoomInfo();
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(controller.getPanel(), "Vui lòng chọn một phòng để đặt.");
            return;
        }

        try {
            JPanel bookingPanel = AdminBookingView.createBookingPanel(
                    controller.getMainFrame(),
                    selectedRoom
            );
            controller.getMainFrame().setAdminDynamicContent(bookingPanel);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(controller.getPanel(), "Lỗi khi xử lý dữ liệu phòng.");
        }
    }
}