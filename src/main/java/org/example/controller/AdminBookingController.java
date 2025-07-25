package org.example.controller;

import org.example.entity.Person;
import org.example.entity.SelectedRoomInfo;
import org.example.service.BookingService;
import org.example.view.AdminBookingView;
import org.example.view.AdminDashboardView;
import org.example.view.AdminSearchView;
import org.example.view.MainFrameView;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminBookingController {

    private final MainFrameView mainFrame;
    private final SelectedRoomInfo selectedRoom;

    public AdminBookingController(MainFrameView mainFrame, SelectedRoomInfo selectedRoom) {
        this.mainFrame = mainFrame;
        this.selectedRoom = selectedRoom;
    }

    public void handleRandomUsername() {
        String randomStr = "user" + String.format("%08d", new Random().nextInt(100_000_000));
        AdminBookingView.getTfTaiKhoan().setText(randomStr);
    }

    public void handleSoNguoiChange() {
        int selected = (int) AdminBookingView.getCbSoNguoi().getSelectedItem();
        for (int i = 0; i < 4; i++) {
            boolean enable = i < selected;
            AdminBookingView.getHoTenFields()[i].setEnabled(enable);
            AdminBookingView.getLoaiCombos()[i].setEnabled(enable);
            AdminBookingView.getMaFields()[i].setEnabled(enable);
            if (!enable) {
                AdminBookingView.getHoTenFields()[i].setText("");
                AdminBookingView.getMaFields()[i].setText("");
            }
        }
    }

    public void handleLoaiGiayToChange(int index) {
        String selected = (String) AdminBookingView.getLoaiCombos()[index].getSelectedItem();
        if ("Không có".equals(selected)) {
            AdminBookingView.getMaFields()[index].setText("Trẻ em");
            AdminBookingView.getMaFields()[index].setEditable(false);
        } else {
            AdminBookingView.getMaFields()[index].setText("");
            AdminBookingView.getMaFields()[index].setEditable(true);
        }
    }

    public void handleQuayLai() {
        JPanel searchPanel = AdminSearchView.createSearchPanel(mainFrame);
        mainFrame.setAdminDynamicContent(searchPanel);
    }

    public void handleBookingConfirmation() {
        int soNguoi = (int) AdminBookingView.getCbSoNguoi().getSelectedItem();

        String username = AdminBookingView.getTfTaiKhoan().getText().trim();
        String sdt = AdminBookingView.getTfSdt().getText().trim();
        String gmail = AdminBookingView.getTfGmail().getText().trim();

        if (username.isEmpty() || gmail.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Vui lòng điền đầy đủ thông tin người đại diện!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hoTenDaiDien = AdminBookingView.getHoTenFields()[0].getText().trim();
        String loaiDaiDien = (String) AdminBookingView.getLoaiCombos()[0].getSelectedItem();
        String maDaiDien = AdminBookingView.getMaFields()[0].getText().trim();

        if (hoTenDaiDien.isEmpty() || maDaiDien.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Vui lòng điền đầy đủ họ tên và mã giấy tờ người đại diện!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Person> danhSach = new ArrayList<>();
        danhSach.add(new Person(hoTenDaiDien, loaiDaiDien, maDaiDien));

        StringBuilder danhSachKhach = new StringBuilder();
        danhSachKhach.append("- ").append(hoTenDaiDien)
                .append(" [").append(loaiDaiDien).append(": ").append(maDaiDien).append("]\n");

        for (int i = 1; i < soNguoi; i++) {
            String ten = AdminBookingView.getHoTenFields()[i].getText().trim();
            String loai = (String) AdminBookingView.getLoaiCombos()[i].getSelectedItem();
            String ma = AdminBookingView.getMaFields()[i].getText().trim();

            if (ten.isEmpty() || ma.isEmpty()) continue;
            danhSach.add(new Person(ten, loai, ma));

            danhSachKhach.append("- ").append(ten)
                    .append(" [").append(loai).append(": ").append(ma).append("]\n");
        }

        String thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String info = String.format("""
                THÔNG TIN ĐẶT PHÒNG

                Tài khoản: %s
                SĐT: %s
                Gmail: %s
                Người đại diện: %s

                Số người: %d
                Danh sách khách:
                %s
                Phòng: %s
                Mô tả: %s
                Loại: %s
                Giá: %,.0f VND

                Check-in: %s
                Check-out: %s
                Thời gian tạo: %s
                Trạng thái: Đã gửi yêu cầu
                """,
                username, sdt, gmail, hoTenDaiDien, soNguoi, danhSachKhach.toString(),
                selectedRoom.getRoomId(), selectedRoom.getDescription(), selectedRoom.getType(),
                selectedRoom.getPrice(),
                selectedRoom.getCheckIn(), selectedRoom.getCheckOut(),
                thoiGianTao
        );

        int confirm = JOptionPane.showOptionDialog(
                mainFrame, info, "📋 Xác nhận đặt phòng",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Quay lại", "Xác nhận"}, "Xác nhận"
        );

        if (confirm == 1) {
            String bookingId = "BK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + (int)(Math.random() * 10000);
            String requestId = "REQ0000X";

            BookingService.createBooking(
                    "bookings.xml",
                    bookingId,
                    requestId,
                    username,
                    hoTenDaiDien,
                    gmail,
                    sdt,
                    selectedRoom.getRoomId(),
                    selectedRoom.getCheckIn(),
                    selectedRoom.getCheckOut(),
                    selectedRoom.getPrice(),
                    danhSach
            );

            BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-in");

            JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "✅ Thành công", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.setAdminDynamicContent(AdminDashboardView.createDashboardPanel());
            mainFrame.setAdminSelectedMenu("Trang chủ");
        }
    }
}