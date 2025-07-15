//package org.example.view;
//import org.example.entity.SelectedRoomInfo;
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.time.LocalDateTime;
//
//public class CustomerBookingView extends JPanel {
//    private MainFrameView mainFrame;
//    private String username;
//    private SelectedRoomInfo selectedRoom;
//
//    private JComboBox<Integer> cbSoNguoi;
//    private JTextField tfTenKhach;
//    private JTextArea taGhiChu;
//    private JTextField[] hoTenFields = new JTextField[3];
//    private JComboBox<String>[] loaiCombos = new JComboBox[3];
//    private JTextField[] maFields = new JTextField[3];
//
//    public CustomerBookingView(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
//        this.mainFrame = mainFrame;
//        this.username = username;
//        this.selectedRoom = selectedRoom;
//
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        setPreferredSize(new Dimension(950, 800));
//        setBackground(Color.WHITE);
//        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//
//        add(createFormPanel());
//        add(Box.createVerticalStrut(10));
//        add(createButtonPanel());
//    }
//
//    public static JPanel createBookingPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
//        return new CustomerBookingView(mainFrame, username, selectedRoom);
//    }
//
//    private JPanel createFormPanel() {
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBackground(UIManager.getColor("Panel.background"));
//        formPanel.setBorder(BorderFactory.createTitledBorder(
//                BorderFactory.createLineBorder(new Color(180, 180, 180)),
//                "Thông tin người dùng",
//                TitledBorder.LEFT, TitledBorder.TOP));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(6, 10, 6, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 1.0;
//
//        // Tên khách
//        gbc.gridy = 0;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Tên khách hàng:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        tfTenKhach = new JTextField();
//        formPanel.add(tfTenKhach, gbc);
//        gbc.gridwidth = 1;
//
//        // Số người
//        gbc.gridy++;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Số người:"), gbc);
//        gbc.gridx = 1;
//        Integer[] soNguoiOptions = {0, 1, 2, 3};
//        cbSoNguoi = new JComboBox<>(soNguoiOptions);
//        formPanel.add(cbSoNguoi, gbc);
//
//        // Ghi chú
//        gbc.gridy++;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Ghi chú:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        taGhiChu = new JTextArea(3, 20);
//        JScrollPane scroll = new JScrollPane(taGhiChu);
//        formPanel.add(scroll, gbc);
//        gbc.gridwidth = 1;
//
//        // Danh sách người đi cùng
//        String[] loaiOptions = {"Không có", "Mã định danh"};
//        for (int i = 0; i < 3; i++) {
//            int row = 3 + i * 2;
//
//            gbc.gridy = row;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
//            gbc.gridx = 1;
//            gbc.gridwidth = 3;
//            hoTenFields[i] = new JTextField();
//            formPanel.add(hoTenFields[i], gbc);
//            gbc.gridwidth = 1;
//
//            gbc.gridy = row + 1;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
//            gbc.gridx = 1;
//            loaiCombos[i] = new JComboBox<>(loaiOptions);
//            formPanel.add(loaiCombos[i], gbc);
//            gbc.gridx = 2;
//            formPanel.add(new JLabel("Mã:"), gbc);
//            gbc.gridx = 3;
//            maFields[i] = new JTextField();
//            formPanel.add(maFields[i], gbc);
//
//            int index = i;
//            loaiCombos[i].addActionListener(e -> {
//                String selected = (String) loaiCombos[index].getSelectedItem();
//                if ("Không có".equals(selected)) {
//                    maFields[index].setText("Trẻ em");
//                    maFields[index].setEditable(false);
//                } else {
//                    maFields[index].setText("");
//                    maFields[index].setEditable(true);
//                }
//            });
//        }
//
//        cbSoNguoi.addActionListener(e -> {
//            int selected = (int) cbSoNguoi.getSelectedItem();
//            for (int i = 0; i < 3; i++) {
//                boolean enable = i < selected;
//                hoTenFields[i].setEnabled(enable);
//                loaiCombos[i].setEnabled(enable);
//                maFields[i].setEnabled(enable);
//                if (!enable) {
//                    hoTenFields[i].setText("");
//                    maFields[i].setText("");
//                }
//            }
//        });
//
//        cbSoNguoi.setSelectedIndex(0);
//        return formPanel;
//    }
//
//    private JPanel createButtonPanel() {
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        btnPanel.setBackground(UIManager.getColor("Panel.background"));
//
//        JButton btnXacNhan = new JButton("Xác nhận");
//        JButton btnQuayLai = new JButton("Quay lại");
//
//        btnQuayLai.addActionListener(e -> {
//            JPanel searchPanel = CustomerSearchView.createSearchPanel(mainFrame);
//            mainFrame.setCustomerDynamicContent(searchPanel);
//        });
//
//        btnXacNhan.addActionListener(e -> {
//            String tenKhach = tfTenKhach.getText().trim();
//            String ghiChu = taGhiChu.getText().trim();
//            int soNguoi = (int) cbSoNguoi.getSelectedItem();
//
//            StringBuilder danhSachKhach = new StringBuilder();
//            for (int i = 0; i < soNguoi; i++) {
//                danhSachKhach.append("- ").append(hoTenFields[i].getText()).append(" [")
//                        .append(loaiCombos[i].getSelectedItem()).append(", ")
//                        .append(maFields[i].getText()).append("]\n");
//            }
//
//            String roomId = selectedRoom.getRoomId();
//            String moTa = selectedRoom.getDescription();
//            String loai = selectedRoom.getType();
//            double gia = selectedRoom.getPrice();
//            LocalDateTime checkIn = selectedRoom.getCheckIn();
//            LocalDateTime checkOut = selectedRoom.getCheckOut();
//            String username = mainFrame.getLoggedInUsername();
//
//            String info = String.format("""
//✅ THÔNG TIN ĐẶT PHÒNG
//
//👤 Người dùng: %s
//📛 Tên khách: %s
//👥 Số người: %d
//📝 Ghi chú: %s
//
//🧾 Danh sách khách:
//%s
//🏨 Phòng: %s
//📋 Mô tả: %s
//🏷️ Loại: %s
//💵 Giá: %,.0f VND
//
//⏰ Check-in: %s
//⏰ Check-out: %s
//""",
//                    username, tenKhach, soNguoi, ghiChu, danhSachKhach.toString(),
//                    roomId, moTa, loai, gia, checkIn, checkOut);
//
//            JTextArea ta = new JTextArea(info);
//            ta.setEditable(false);
//            ta.setLineWrap(true);
//            ta.setWrapStyleWord(true);
//            ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
//            JScrollPane scrollPane = new JScrollPane(ta);
//            scrollPane.setPreferredSize(new Dimension(600, 400));
//
//            JOptionPane.showMessageDialog(this, scrollPane, "📦 XÁC NHẬN ĐẶT PHÒNG", JOptionPane.INFORMATION_MESSAGE);
//        });
//
//        btnPanel.add(btnQuayLai);
//        btnPanel.add(btnXacNhan);
//        return btnPanel;
//    }
//}


package org.example.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.example.entity.SelectedRoomInfo;
import org.example.service.UserInfoService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CustomerBookingView {

    public static JPanel createBookingPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(950, 800));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Form và button panel
        JPanel formPanel = createFormPanel(mainFrame, username, selectedRoom);
        JPanel buttonPanel = createButtonPanel(mainFrame, username, selectedRoom);

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);

        return panel;
    }

    private static JTextField[] hoTenFields = new JTextField[3];
    private static JComboBox<String>[] loaiCombos = new JComboBox[3];
    private static JTextField[] maFields = new JTextField[3];
    private static JComboBox<Integer> cbSoNguoi;
    private static JTextField tfMaChung;

    private static JPanel createFormPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIManager.getColor("Panel.background"));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Thông tin người dùng",
                TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Số người
        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Số người:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        Integer[] soNguoiOptions = {0, 1, 2, 3};
        cbSoNguoi = new JComboBox<>(soNguoiOptions);
        formPanel.add(cbSoNguoi, gbc);
        gbc.gridwidth = 1;

        // Người đại diện (người đặt phòng)
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh"});
        formPanel.add(cbLoaiChung, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Mã:"), gbc);
        gbc.gridx = 3;
        tfMaChung = new JTextField();
        formPanel.add(tfMaChung, gbc);

        String[] loaiOptions = {"Không có", "Mã định danh"};

        for (int i = 0; i < 3; i++) {
            int row = i * 2 + 2;

            gbc.gridy = row;
            gbc.gridx = 0;
            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            hoTenFields[i] = new JTextField();
            formPanel.add(hoTenFields[i], gbc);

            gbc.gridy = row + 1;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
            gbc.gridx = 1;
            loaiCombos[i] = new JComboBox<>(loaiOptions);
            formPanel.add(loaiCombos[i], gbc);
            gbc.gridx = 2;
            formPanel.add(new JLabel("Mã:"), gbc);
            gbc.gridx = 3;
            maFields[i] = new JTextField();
            formPanel.add(maFields[i], gbc);

            int index = i;
            loaiCombos[i].addActionListener(e -> {
                String selected = (String) loaiCombos[index].getSelectedItem();
                if ("Không có".equals(selected)) {
                    maFields[index].setText("Trẻ em");
                    maFields[index].setEditable(false);
                } else {
                    maFields[index].setText("");
                    maFields[index].setEditable(true);
                }
            });
        }

        cbSoNguoi.addActionListener(e -> {
            int selected = (int) cbSoNguoi.getSelectedItem();
            for (int i = 0; i < 3; i++) {
                boolean enable = i < selected;
                hoTenFields[i].setEnabled(enable);
                loaiCombos[i].setEnabled(enable);
                maFields[i].setEnabled(enable);
                if (!enable) {
                    hoTenFields[i].setText("");
                    maFields[i].setText("");
                }
            }
        });

        cbSoNguoi.setSelectedIndex(0);
        return formPanel;
    }

    private static JPanel createButtonPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton btnXacNhan = new JButton("Xác nhận");
        JButton btnQuayLai = new JButton("Quay lại");

        btnQuayLai.addActionListener(e -> {
            JPanel searchPanel = CustomerSearchView.createSearchPanel(mainFrame);
            mainFrame.setCustomerDynamicContent(searchPanel);
        });

        btnXacNhan.addActionListener(e -> {
            int soNguoi = (int) cbSoNguoi.getSelectedItem() + 1;

            // Người đại diện là người đầu tiên, lấy CCCD từ tfMaChung
            String cccdNguoiDaiDien = tfMaChung.getText();

            // Thông tin từ tài khoản đăng nhập
            String hoTen = UserInfoService.getFullName("userinfos.xml", username);
            String gmail = UserInfoService.getEmail("userinfos.xml", username);
            String sdt = UserInfoService.getPhone("userinfos.xml", username);


            StringBuilder danhSachKhach = new StringBuilder();
            danhSachKhach.append("- ").append(hoTen)
                    .append(" [Mã định danh : ").append(cccdNguoiDaiDien).append("]\n");

            for (int i = 0; i < soNguoi; i++) {
                String ten = hoTenFields[i].getText().trim();
                String loai = (String) loaiCombos[i].getSelectedItem();
                String ma = maFields[i].getText().trim();

                // Nếu tên hoặc mã bị rỗng, có thể thông báo lỗi ở đây (tuỳ bạn)
                if (ten.isEmpty() || ma.isEmpty()) continue;

                danhSachKhach.append("- ").append(ten)
                        .append(" [").append(loai).append(" : ").append(ma).append("]\n");
            }


            // Thời gian tạo yêu cầu
            String thoiGianTao;
            try {
                thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                // dùng biến thoiGianTao như bình thường
            } catch (Exception ex) {
                throw new RuntimeException(ex); // hoặc xử lý theo ý bạn
            }


            String info = String.format("""
THÔNG TIN ĐẶT PHÒNG

Người dùng: %s
Người đại diện: %s
CCCD: %s
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
                    username,
                    hoTen,
                    cccdNguoiDaiDien,
                    gmail,
                    sdt,
                    soNguoi,
                    danhSachKhach.toString(),
                    selectedRoom.getRoomId(),
                    selectedRoom.getDescription(),
                    selectedRoom.getType(),
                    selectedRoom.getPrice(),
                    selectedRoom.getCheckIn(),
                    selectedRoom.getCheckOut(),
                    thoiGianTao
            );

//            JOptionPane.showMessageDialog(mainFrame, info, "XÁC NHẬN ĐẶT PHÒNG", JOptionPane.INFORMATION_MESSAGE);
//            JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "THÔNG BÁO", JOptionPane.INFORMATION_MESSAGE);

            String[] options = {"Quay lại", "Xác nhận"};
            int result = JOptionPane.showOptionDialog(
                    mainFrame,
                    info,
                    "📋 Xác nhận đặt phòng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (result == 1) { // Xác nhận
                JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                JPanel searchPanel = CustomerSearchView.createSearchPanel(mainFrame);
                mainFrame.setCustomerDynamicContent(searchPanel);
            }
        });



        btnPanel.add(btnQuayLai);
        btnPanel.add(btnXacNhan);

        return btnPanel;
    }
}
