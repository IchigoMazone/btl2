package org.example.action;

import org.example.controller.AdminBookingController;
import org.example.entity.Person;
import org.example.entity.SelectedRoomInfo;
import org.example.service.BookingService;
import org.example.view.AdminDashboardView;
import org.example.view.AdminSearchView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class CheckAdminBooking {
    private AdminBookingController controller;
    private static final Pattern MA_DINH_DANH_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern HO_CHIEU_PATTERN = Pattern.compile("[A-Za-z0-9]{8}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");

    public CheckAdminBooking(AdminBookingController controller) {
        this.controller = controller;
        updateMaChungField();
        for (int i = 1; i < 4; i++) {
            updateMaField(i);
        }
        updateGuestFields();
    }

    public void updateMaChungField() {
        String selected = (String) controller.getView().getCbLoaiChung().getSelectedItem();
        if ("Không có".equals(selected)) {
            controller.getView().getCbMaChung().setSelectedItem("Không xác định");
            controller.getView().getCardLayoutMaChung().show(controller.getView().getPanelMaChung(), "combo");
        } else {
            controller.getView().getTfMaChung().setText("");
            controller.getView().getCardLayoutMaChung().show(controller.getView().getPanelMaChung(), "text");
        }
    }

    public void updateMaField(int index) {
        String selected = (String) controller.getView().getLoaiCombos()[index].getSelectedItem();
        if ("Không có".equals(selected)) {
            controller.getView().getMaComboFields()[index].setSelectedItem("Không xác định");
            controller.getView().getMaCardLayouts()[index].show(controller.getView().getMaPanels()[index], "combo");
        } else {
            controller.getView().getMaTextFields()[index].setText("");
            controller.getView().getMaCardLayouts()[index].show(controller.getView().getMaPanels()[index], "text");
        }
    }

    public void updateGuestFields() {
        int selected = (int) controller.getView().getCbSoNguoi().getSelectedItem();
        for (int i = 1; i < 4; i++) {
            boolean enable = i < selected;
            controller.getView().getHoTenFields()[i].setEnabled(enable);
            controller.getView().getLoaiCombos()[i].setEnabled(enable);
            controller.getView().getMaComboFields()[i].setEnabled(enable);
            controller.getView().getMaTextFields()[i].setEnabled(enable);

            if (!enable) {
                controller.getView().getHoTenFields()[i].setText("");
                controller.getView().getMaTextFields()[i].setText("");
                controller.getView().getMaComboFields()[i].setSelectedIndex(0);
            }

            String selectedLoai = (String) controller.getView().getLoaiCombos()[i].getSelectedItem();
            controller.getView().getMaCardLayouts()[i].show(controller.getView().getMaPanels()[i], "Không có".equals(selectedLoai) ? "combo" : "text");
        }
    }

    public void handleRandomUsername() {
        String randomStr = "user" + String.format("%08d", new Random().nextInt(100_000_000));
        controller.getView().getTfTaiKhoan().setText(randomStr);
    }

    public void handleQuayLai() {
        JPanel searchPanel = AdminSearchView.createSearchPanel(controller.getMainFrame());
        controller.getMainFrame().setAdminDynamicContent(searchPanel);
    }

    public void handleXacNhan() {
        int soNguoi = (int) controller.getView().getCbSoNguoi().getSelectedItem();
        String username = controller.getView().getTfTaiKhoan().getText().trim();
        String sdt = controller.getView().getTfSdt().getText().trim();
        String gmail = controller.getView().getTfGmail().getText().trim();
        String hoTenDaiDien = controller.getView().getHoTenFields()[0].getText().trim();
        String loaiDaiDien = (String) controller.getView().getCbLoaiChung().getSelectedItem();
        String maDaiDien = "Không có".equals(loaiDaiDien) ? (String) controller.getView().getCbMaChung().getSelectedItem() : controller.getView().getTfMaChung().getText().trim();

        String errorMessage = validateInput(soNguoi, username, sdt, gmail, hoTenDaiDien, loaiDaiDien, maDaiDien);
        if (errorMessage != null) {
            JOptionPane.showMessageDialog(controller.getMainFrame(), errorMessage, "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder danhSachKhach = new StringBuilder();
        danhSachKhach.append("- ").append(hoTenDaiDien)
                .append(" [").append(loaiDaiDien).append(": ").append(maDaiDien).append("]\n");

        List<Person> danhSach = new ArrayList<>();
        danhSach.add(new Person(hoTenDaiDien, loaiDaiDien, maDaiDien));

        for (int i = 1; i < soNguoi; i++) {
            String ten = controller.getView().getHoTenFields()[i].getText().trim();
            String loai = (String) controller.getView().getLoaiCombos()[i].getSelectedItem();
            String ma = "Không có".equals(loai) ? (String) controller.getView().getMaComboFields()[i].getSelectedItem() : controller.getView().getMaTextFields()[i].getText().trim();

            if (!ten.isEmpty()) {
                danhSachKhach.append("- ").append(ten)
                        .append(" [").append(loai).append(": ").append(ma).append("]\n");
                danhSach.add(new Person(ten, loai, ma));
            }
        }

        SelectedRoomInfo selectedRoom = controller.getSelectedRoom();
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
                selectedRoom.getPrice(), selectedRoom.getCheckIn(), selectedRoom.getCheckOut(),
                thoiGianTao);

        JTextArea textArea = new JTextArea(info);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JPanel content = new JPanel(new BorderLayout());
        content.add(scrollPane, BorderLayout.CENTER);

        int confirm = JOptionPane.showOptionDialog(
                controller.getMainFrame(),
                content,
                "Xác nhận đặt phòng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Quay lại", "Xác nhận"},
                "Xác nhận");

        if (confirm == 1) {
            String bookingId = "BK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%05d", new Random().nextInt(100000));
            String requestId = "REQ" + String.format("%04d", new Random().nextInt(10000));

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
            long minutesUntilCheckIn = ChronoUnit.MINUTES.between(LocalDateTime.now(), selectedRoom.getCheckIn());

            if (minutesUntilCheckIn <= 15 && minutesUntilCheckIn >= 0) {
                BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-in");
            }

            JOptionPane.showMessageDialog(controller.getMainFrame(), "Gửi yêu cầu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            controller.getMainFrame().setAdminDynamicContent(AdminDashboardView.createDashboardPanel());
            controller.getMainFrame().setAdminSelectedMenu("Trang chủ");
        }
    }

    private String validateInput(int soNguoi, String username, String sdt, String gmail, String hoTenDaiDien, String loaiDaiDien, String maDaiDien) {
        if (username.isEmpty()) {
            return "Tài khoản không được để trống!";
        }
        if (!PHONE_PATTERN.matcher(sdt).matches()) {
            return "Số điện thoại phải là 10 chữ số và bắt đầu bằng 0!";
        }
        if (!EMAIL_PATTERN.matcher(gmail).matches()) {
            return "Định dạng Gmail không hợp lệ!";
        }

        if (hoTenDaiDien.isEmpty()) {
            return "Họ tên người đại diện không được để trống!";
        }

        if (!"Không có".equals(loaiDaiDien) && maDaiDien.isEmpty()) {
            return "Mã giấy tờ người đại diện không được để trống!";
        }
        if (!isValidMaGiayTo(loaiDaiDien, maDaiDien)) {
            return "Mã giấy tờ người đại diện không đúng định dạng!\n" +
                    "Mã định danh: 12 chữ số\nHộ chiếu: 8 ký tự chữ hoặc số";
        }
        if ("Mã định danh".equals(loaiDaiDien) && "Dưới 14 tuổi".equals(maDaiDien)) {
            return "Người đại diện dưới 14 tuổi không được phép sử dụng Mã định danh!";
        }

        int validGuests = 1;
        Set<String> maDinhDanhSet = new HashSet<>();
        Set<String> hoChieuSet = new HashSet<>();

        if ("Mã định danh".equals(loaiDaiDien)) {
            maDinhDanhSet.add(maDaiDien);
        } else if ("Hộ chiếu".equals(loaiDaiDien)) {
            hoChieuSet.add(maDaiDien);
        }

        for (int i = 1; i < soNguoi; i++) {
            String ten = controller.getView().getHoTenFields()[i].getText().trim();
            String loai = (String) controller.getView().getLoaiCombos()[i].getSelectedItem();
            String ma = "Không có".equals(loai) ? (String) controller.getView().getMaComboFields()[i].getSelectedItem() : controller.getView().getMaTextFields()[i].getText().trim();

            if (ten.isEmpty() && !ma.isEmpty()) {
                return "Họ tên người " + (i + 1) + " không được để trống khi đã nhập mã!";
            }
            if (!ten.isEmpty() && !"Không có".equals(loai) && ma.isEmpty()) {
                return "Mã giấy tờ người " + (i + 1) + " không được để trống!";
            }
            if (!ten.isEmpty() && !isValidMaGiayTo(loai, ma)) {
                return "Mã giấy tờ người " + (i + 1) + " không đúng định dạng!\n" +
                        "Mã định danh: 12 chữ số\nHộ chiếu: 8 ký tự chữ hoặc số";
            }
            if (!ten.isEmpty() && "Mã định danh".equals(loai) && "Dưới 14 tuổi".equals(ma)) {
                return "Người " + (i + 1) + " dưới 14 tuổi không được phép sử dụng Mã định danh!";
            }
            if (!ten.isEmpty()) {
                validGuests++;

                if ("Mã định danh".equals(loai)) {
                    if (!maDinhDanhSet.add(ma)) {
                        return "Mã định danh của người " + (i + 1) + " trùng với mã đã nhập trước đó!";
                    }
                } else if ("Hộ chiếu".equals(loai)) {
                    if (!hoChieuSet.add(ma)) {
                        return "Hộ chiếu của người " + (i + 1) + " trùng với mã đã nhập trước đó!";
                    }
                }
            }
        }

        if (validGuests < soNguoi) {
            return "Số lượng khách hợp lệ (" + validGuests + ") không khớp với số người đã chọn (" + soNguoi + ")!";
        }

        return null;
    }

    private boolean isValidMaGiayTo(String loai, String ma) {
        if ("Không có".equals(loai)) {
            return ma.equals("Không xác định") || ma.equals("Dưới 14 tuổi");
        } else if ("Mã định danh".equals(loai)) {
            return MA_DINH_DANH_PATTERN.matcher(ma).matches();
        } else if ("Hộ chiếu".equals(loai)) {
            return HO_CHIEU_PATTERN.matcher(ma).matches();
        }
        return false;
    }
}