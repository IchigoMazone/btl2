package org.example.action;

import org.example.controller.CustomerBookingController;
import org.example.entity.Person;
import org.example.entity.SelectedRoomInfo;
import org.example.service.NotificationService;
import org.example.service.RequestService;
import org.example.service.UserInfoService;
import org.example.view.CustomerDashboardView;
import org.example.view.CustomerSearchView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class CheckCustomerBooking {
    private CustomerBookingController controller;
    private static final Pattern MA_DINH_DANH_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern HO_CHIEU_PATTERN = Pattern.compile("[A-Za-z0-9]{8}");

    public CheckCustomerBooking(CustomerBookingController controller) {
        this.controller = controller;
        updateMaChungField();
        for (int i = 0; i < 3; i++) {
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
        for (int i = 0; i < 3; i++) {
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

    private String validateInput(int soNguoi, String hoTen, String loaiChung, String cccdNguoiDaiDien) {
        if (hoTen.isEmpty()) {
            return "Họ tên người đại diện không được để trống!";
        }

        if (!"Không có".equals(loaiChung) && cccdNguoiDaiDien.isEmpty()) {
            return "Mã giấy tờ người đại diện không được để trống!";
        }
        if (!isValidMaGiayTo(loaiChung, cccdNguoiDaiDien)) {
            return "Mã giấy tờ người đại diện không đúng định dạng!\n" +
                    "Mã định danh: 12 chữ số\nHộ chiếu: 8 ký tự chữ hoặc số";
        }

        if ("Mã định danh".equals(loaiChung) && "Dưới 14 tuổi".equals(cccdNguoiDaiDien)) {
            return "Người đại diện dưới 14 tuổi không được phép sử dụng Mã định danh!";
        }

        int validGuests = 0;
        Set<String> maDinhDanhSet = new HashSet<>();
        Set<String> hoChieuSet = new HashSet<>();

        if ("Mã định danh".equals(loaiChung)) {
            maDinhDanhSet.add(cccdNguoiDaiDien);
        } else if ("Hộ chiếu".equals(loaiChung)) {
            hoChieuSet.add(cccdNguoiDaiDien);
        }

        for (int i = 0; i < soNguoi; i++) {
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

    public void handleQuayLai() {
        JPanel searchPanel = CustomerSearchView.createSearchPanel(controller.getMainFrame());
        controller.getMainFrame().setCustomerDynamicContent(searchPanel);
    }

    public void handleXacNhan() {
        int soNguoi = (int) controller.getView().getCbSoNguoi().getSelectedItem();
        String hoTen = UserInfoService.getFullName("userinfos.xml", controller.getUsername());
        String gmail = UserInfoService.getEmail("userinfos.xml", controller.getUsername());
        String sdt = UserInfoService.getPhone("userinfos.xml", controller.getUsername());

        String cccdNguoiDaiDien;
        String loaiChung = (String) controller.getView().getCbLoaiChung().getSelectedItem();
        if ("Không có".equals(loaiChung)) {
            cccdNguoiDaiDien = (String) controller.getView().getCbMaChung().getSelectedItem();
        } else {
            cccdNguoiDaiDien = controller.getView().getTfMaChung().getText().trim();
        }

        String errorMessage = validateInput(soNguoi, hoTen, loaiChung, cccdNguoiDaiDien);
        if (errorMessage != null) {
            JOptionPane.showMessageDialog(controller.getMainFrame(), errorMessage, "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder danhSachKhach = new StringBuilder();
        danhSachKhach.append("- ").append(hoTen).append(" [").append(loaiChung)
                .append(" : ").append(cccdNguoiDaiDien).append("]\n");

        List<Person> danhSach = new ArrayList<>();
        danhSach.add(new Person(hoTen, loaiChung, cccdNguoiDaiDien));

        for (int i = 0; i < soNguoi; i++) {
            String ten = controller.getView().getHoTenFields()[i].getText().trim();
            String loai = (String) controller.getView().getLoaiCombos()[i].getSelectedItem();
            String ma = "Không có".equals(loai) ? (String) controller.getView().getMaComboFields()[i].getSelectedItem() : controller.getView().getMaTextFields()[i].getText().trim();

            if (!ten.isEmpty()) {
                danhSachKhach.append("- ").append(ten).append(" [").append(loai).append(" : ").append(ma).append("]\n");
                danhSach.add(new Person(ten, loai, ma));
            }
        }

        SelectedRoomInfo selectedRoom = controller.getSelectedRoom();
        String thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String requestId = generateRequestId();

        String info = String.format("""
                THÔNG TIN ĐẶT PHÒNG

                Người dùng: %s
                Người đại diện: %s
                Gmail: %s
                SĐT: %s

                Số người: %d

                Danh sách khách:
                %s

                Phòng: %s
                Mô tả: %s
                Loại: %s
                Giá: %,.0f VND

                Check-in: %s
                Check-out: %s
                Tạo yêu cầu: %s
                Trạng thái: Đã gửi yêu cầu
                """,
                controller.getUsername(), hoTen, gmail, sdt, soNguoi, danhSachKhach,
                selectedRoom.getRoomId(), selectedRoom.getDescription(),
                selectedRoom.getType(), selectedRoom.getPrice(),
                selectedRoom.getCheckIn(), selectedRoom.getCheckOut(), thoiGianTao);

        JTextArea textArea = new JTextArea(info);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JPanel content = new JPanel(new BorderLayout());
        content.add(scrollPane, BorderLayout.CENTER);

        Object[] options = {"Quay lại", "Xác nhận"};
        int result = JOptionPane.showOptionDialog(
                controller.getMainFrame(),
                content,
                "Xác nhận đặt phòng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[1]
        );

        if (result == 1) {
            RequestService.createRequest(
                    requestId, controller.getUsername(), hoTen, gmail, sdt,
                    selectedRoom.getRoomId(),
                    selectedRoom.getCheckIn(), selectedRoom.getCheckOut(),
                    selectedRoom.getPrice(), danhSach
            );

            NotificationService.createNotification(
                    "BK00000001", requestId, controller.getUsername(),
                    "Yêu cầu duyệt", "Đã gửi"
            );

            JOptionPane.showMessageDialog(controller.getMainFrame(), "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            JPanel homePanel = CustomerDashboardView.createUserHomePanel();
            controller.getMainFrame().setCustomerDynamicContent(homePanel);
            controller.getMainFrame().setCustomerSelectedMenu("Trang chủ");
        }
    }

    private String generateRequestId() {
        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}