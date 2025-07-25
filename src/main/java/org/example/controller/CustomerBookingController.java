//package org.example.controller;
//
//import org.example.entity.Person;
//import org.example.entity.SelectedRoomInfo;
//import org.example.service.RequestService;
//import org.example.service.NotificationService;
//import org.example.service.UserInfoService;
//import org.example.view.CustomerBookingView;
//import org.example.view.CustomerDashboardView;
//import org.example.view.CustomerSearchView;
//import org.example.view.MainFrameView;
//import javax.swing.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class CustomerBookingController {
//
//    private final MainFrameView mainFrame;
//    private final String username;
//    private final SelectedRoomInfo selectedRoom;
//
//    public CustomerBookingController(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
//        this.mainFrame = mainFrame;
//        this.username = username;
//        this.selectedRoom = selectedRoom;
//    }
//
//    public void handleSoNguoiChange() {
//        int selected = (int) CustomerBookingView.getCbSoNguoi().getSelectedItem();
//        for (int i = 0; i < 3; i++) {
//            boolean enable = i < selected;
//            CustomerBookingView.getHoTenFields()[i].setEnabled(enable);
//            CustomerBookingView.getLoaiCombos()[i].setEnabled(enable);
//            CustomerBookingView.getMaFields()[i].setEnabled(enable);
//            if (!enable) {
//                CustomerBookingView.getHoTenFields()[i].setText("");
//                CustomerBookingView.getMaFields()[i].setText("");
//            }
//        }
//    }
//
//    public void handleLoaiGiayToChange(int index) {
//        String selected = (String) CustomerBookingView.getLoaiCombos()[index].getSelectedItem();
//        if ("Không có".equals(selected)) {
//            CustomerBookingView.getMaFields()[index].setText("Trẻ em");
//            CustomerBookingView.getMaFields()[index].setEditable(false);
//        } else {
//            CustomerBookingView.getMaFields()[index].setText("");
//            CustomerBookingView.getMaFields()[index].setEditable(true);
//        }
//    }
//
//    public void handleLoaiChungChange() {
//        String selected = (String) CustomerBookingView.getCbLoaiChung().getSelectedItem();
//        if ("Không có".equals(selected)) {
//            CustomerBookingView.getTfMaChung().setText("Trẻ em");
//            CustomerBookingView.getTfMaChung().setEditable(false);
//        } else {
//            CustomerBookingView.getTfMaChung().setText("");
//            CustomerBookingView.getTfMaChung().setEditable(true);
//        }
//    }
//
//    public void handleQuayLai() {
//        JPanel searchPanel = CustomerSearchView.createSearchPanel(mainFrame);
//        mainFrame.setCustomerDynamicContent(searchPanel);
//    }
//
//    public void handleBookingConfirmation() {
//        int soNguoi = (int) CustomerBookingView.getCbSoNguoi().getSelectedItem() + 1;
//
//        String cccdNguoiDaiDien = CustomerBookingView.getTfMaChung().getText().trim();
//        String hoTen = UserInfoService.getFullName("userinfos.xml", username);
//        String gmail = UserInfoService.getEmail("userinfos.xml", username);
//        String sdt = UserInfoService.getPhone("userinfos.xml", username);
//
//        if (hoTen == null || gmail == null || sdt == null || cccdNguoiDaiDien.isEmpty()) {
//            JOptionPane.showMessageDialog(mainFrame, "Vui lòng điền đầy đủ thông tin người đại diện!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        StringBuilder danhSachKhach = new StringBuilder();
//        danhSachKhach.append("- ").append(hoTen)
//                .append(" [Mã định danh: ").append(cccdNguoiDaiDien).append("]\n");
//
//        List<Person> danhSach = new ArrayList<>();
//        danhSach.add(new Person(hoTen, (String) CustomerBookingView.getCbLoaiChung().getSelectedItem(), cccdNguoiDaiDien));
//
//        for (int i = 0; i < soNguoi - 1; i++) {
//            String ten = CustomerBookingView.getHoTenFields()[i].getText().trim();
//            String loai = (String) CustomerBookingView.getLoaiCombos()[i].getSelectedItem();
//            String ma = CustomerBookingView.getMaFields()[i].getText().trim();
//
//            if (ten.isEmpty() || ma.isEmpty()) continue;
//
//            danhSach.add(new Person(ten, loai, ma));
//            danhSachKhach.append("- ").append(ten)
//                    .append(" [").append(loai).append(": ").append(ma).append("]\n");
//        }
//
//        String thoiGianTao;
//        try {
//            thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(mainFrame, "Lỗi khi tạo thời gian!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String requestId = "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
//
//        String info = String.format("""
//                THÔNG TIN ĐẶT PHÒNG
//
//                Người dùng: %s
//                Người đại diện: %s
//                CCCD: %s
//                Gmail: %s
//                SĐT: %s
//
//                Số người: %d
//
//                Danh sách khách:
//                %s
//                Phòng: %s
//                Mô tả: %s
//                Loại: %s
//                Giá: %,.0f VND
//
//                Check-in: %s
//                Check-out: %s
//                Tạo yêu cầu: %s
//                Trạng thái: Đã gửi yêu cầu
//                """,
//                username, hoTen, cccdNguoiDaiDien, gmail, sdt, soNguoi, danhSachKhach.toString(),
//                selectedRoom.getRoomId(), selectedRoom.getDescription(), selectedRoom.getType(),
//                selectedRoom.getPrice(), selectedRoom.getCheckIn(), selectedRoom.getCheckOut(), thoiGianTao
//        );
//
//        String[] options = {"Quay lại", "Xác nhận"};
//        int result = JOptionPane.showOptionDialog(
//                mainFrame, info, "📋 Xác nhận đặt phòng",
//                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
//                null, options, options[1]
//        );
//
//        if (result == 1) {
//            RequestService.createRequest(
//                    requestId, username, hoTen, gmail, sdt,
//                    selectedRoom.getRoomId(), selectedRoom.getCheckIn(), selectedRoom.getCheckOut(),
//                    selectedRoom.getPrice(), danhSach
//            );
//
//            NotificationService.createNotification(
//                    "BK00000001", requestId, username,
//                    "Yêu cầu duyệt", "Đã gửi"
//            );
//
//            JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            JPanel homePanel = CustomerDashboardView.createUserHomePanel();
//            mainFrame.setCustomerDynamicContent(homePanel);
//            mainFrame.setCustomerSelectedMenu("Trang chủ");
//        }
//    }
//}