package org.example.action;

import org.example.controller.CustomerSearchController;
import org.example.entity.Room;
import org.example.entity.SelectedRoomInfo;
import org.example.view.CustomerBookingView;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CheckCustomerSearch {
    private CustomerSearchController controller;

    public CheckCustomerSearch(CustomerSearchController controller) {
        this.controller = controller;
    }

    public void handleSearch() {
        try {
            LocalDate ngayDen = controller.getView().getDpCheckIn().getDate();
            LocalDate ngayDi = controller.getView().getDpCheckOut().getDate();

            if (ngayDen == null || ngayDi == null) {
                JOptionPane.showMessageDialog(controller.getPanel(), "Vui lòng chọn ngày đến và ngày đi.");
                return;
            }

            int gioDen = controller.getView().getCbCheckInHour().getSelectedIndex();
            int phutDen = controller.getView().getCbCheckInMinute().getSelectedIndex();
            int gioDi = controller.getView().getCbCheckOutHour().getSelectedIndex();
            int phutDi = controller.getView().getCbCheckOutMinute().getSelectedIndex();

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

            Duration duration = Duration.between(LocalDateTime.now(), checkIn);
            if (!checkIn.isBefore(LocalDateTime.now()) && duration.toHours() < 4) {
                JOptionPane.showMessageDialog(controller.getPanel(), "Đặt phòng cần trước check-in ít nhất 4 giờ");
                return;
            }

            String loaiPhong = controller.getView().getCbLoaiPhong().getSelectedItem().toString();
            List<Room> rooms = controller.getFinder().findAvailableRooms(checkIn, checkOut, loaiPhong);

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
            JOptionPane.showMessageDialog(controller.getPanel(), "Bạn chưa chọn phòng nào để đặt.");
            return;
        }

        SelectedRoomInfo selectedRoom = controller.getSelectedRoomInfo();
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(controller.getPanel(), "Vui lòng chọn một phòng để đặt.");
            return;
        }

        try {
            JPanel bookingPanel = CustomerBookingView.createBookingPanel(
                    controller.getMainFrame(),
                    controller.getMainFrame().getLoggedInUsername(),
                    selectedRoom
            );
            controller.getMainFrame().setCustomerDynamicContent(bookingPanel);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(controller.getPanel(), "Lỗi khi xử lý dữ liệu phòng đã chọn.");
        }
    }
}