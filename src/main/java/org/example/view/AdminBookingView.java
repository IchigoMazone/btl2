////package org.example.view;
////
////import javax.swing.*;
////import javax.swing.border.TitledBorder;
////import java.awt.*;
////import java.util.Random;
////
////public class AdminBookingView {
////
////    public static JPanel createUserPanel() {
////        Color backgroundColor = UIManager.getColor("Panel.background");
////
////        JPanel panel = new JPanel();
////        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
////        panel.setBackground(Color.WHITE);
////        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
////
////        panel.add(createFormPanel(backgroundColor));
////        panel.add(Box.createVerticalStrut(10));
////        panel.add(createButtonPanel(backgroundColor));
////
////        return panel;
////    }
////
////    private static JPanel createFormPanel(Color backgroundColor) {
////        JPanel formPanel = new JPanel(new GridBagLayout());
////        formPanel.setBackground(backgroundColor);
////        formPanel.setBorder(BorderFactory.createTitledBorder(
////                BorderFactory.createLineBorder(new Color(180, 180, 180)),
////                "Thông tin người dùng",
////                TitledBorder.LEFT, TitledBorder.TOP));
////
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(6, 10, 6, 10);
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////        gbc.weightx = 1.0;
////
////        gbc.gridy = 0;
////        gbc.gridx = 0;
////        formPanel.add(new JLabel("Số người:"), gbc);
////        gbc.gridx = 1;
////        gbc.gridwidth = 3;
////        Integer[] soNguoiOptions = {1, 2, 3, 4};
////        JComboBox<Integer> cbSoNguoi = new JComboBox<>(soNguoiOptions);
////        formPanel.add(cbSoNguoi, gbc);
////        gbc.gridwidth = 1;
////
////        gbc.gridy = 1;
////        gbc.gridx = 0;
////        formPanel.add(new JLabel("Tài khoản:"), gbc);
////        gbc.gridx = 1;
////        gbc.gridwidth = 2;
////        JTextField tfTaiKhoan = new JTextField();
////        formPanel.add(tfTaiKhoan, gbc);
////        gbc.gridx = 3;
////        gbc.gridwidth = 1;
////        JButton btnRandom = new JButton("Sinh ngẫu nhiên");
////        formPanel.add(btnRandom, gbc);
////
////        btnRandom.addActionListener(e -> {
////            String randomStr = "user" + String.format("%08d", new Random().nextInt(100_000_000));
////            tfTaiKhoan.setText(randomStr);
////        });
////
////        gbc.gridy = 2;
////        gbc.gridx = 0;
////        formPanel.add(new JLabel("SĐT:"), gbc);
////        gbc.gridx = 1;
////        gbc.gridwidth = 3;
////        JTextField tfSdt = new JTextField();
////        formPanel.add(tfSdt, gbc);
////
////        gbc.gridy = 3;
////        gbc.gridx = 0;
////        formPanel.add(new JLabel("Gmail:"), gbc);
////        gbc.gridx = 1;
////        JTextField tfGmail = new JTextField();
////        formPanel.add(tfGmail, gbc);
////
////        String[] loaiOptions = {"Không có", "Mã định danh"};
////        JTextField[] hoTenFields = new JTextField[4];
////        JComboBox<String>[] loaiCombos = new JComboBox[4];
////        JTextField[] maFields = new JTextField[4];
////
////        for (int i = 0; i < 4; i++) {
////            int row = 4 + i * 2;
////            gbc.gridy = row;
////            gbc.gridx = 0;
////            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
////            gbc.gridx = 1;
////            gbc.gridwidth = 3;
////            hoTenFields[i] = new JTextField();
////            formPanel.add(hoTenFields[i], gbc);
////
////            gbc.gridy = row + 1;
////            gbc.gridwidth = 1;
////            gbc.gridx = 0;
////            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
////            gbc.gridx = 1;
////            loaiCombos[i] = new JComboBox<>(loaiOptions);
////            formPanel.add(loaiCombos[i], gbc);
////            gbc.gridx = 2;
////            formPanel.add(new JLabel("Mã:"), gbc);
////            gbc.gridx = 3;
////            maFields[i] = new JTextField();
////            formPanel.add(maFields[i], gbc);
////
////            int index = i;
////            loaiCombos[i].addActionListener(e -> {
////                String selected = (String) loaiCombos[index].getSelectedItem();
////                if ("Không có".equals(selected)) {
////                    maFields[index].setText("Trẻ em");
////                    maFields[index].setEditable(false);
////                } else {
////                    maFields[index].setText("");
////                    maFields[index].setEditable(true);
////                }
////            });
////        }
////
////        cbSoNguoi.addActionListener(e -> {
////            int selected = (int) cbSoNguoi.getSelectedItem();
////            for (int i = 0; i < 4; i++) {
////                boolean enable = i < selected;
////                hoTenFields[i].setEnabled(enable);
////                loaiCombos[i].setEnabled(enable);
////                maFields[i].setEnabled(enable);
////                if (!enable) {
////                    hoTenFields[i].setText("");
////                    maFields[i].setText("");
////                }
////            }
////        });
////
////        cbSoNguoi.setSelectedIndex(0);
////        return formPanel;
////    }
////
////    private static JPanel createButtonPanel(Color backgroundColor) {
////        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
////        btnPanel.setBackground(backgroundColor);
////
////        JButton btnXacNhan = new JButton("Xác nhận");
////        JButton btnQuayLai = new JButton("Quay lại");
////
////        btnPanel.add(btnQuayLai);
////        btnPanel.add(btnXacNhan);
////
////        return btnPanel;
////    }
////}
//
//
//package org.example.view;
//
//import org.example.entity.Person;
//import org.example.entity.SelectedRoomInfo;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class AdminBookingView {
//
//    private static JTextField tfTaiKhoan, tfSdt, tfGmail;
//    private static JTextField[] hoTenFields = new JTextField[4];
//    private static JComboBox<String>[] loaiCombos = new JComboBox[4];
//    private static JTextField[] maFields = new JTextField[4];
//    private static JComboBox<Integer> cbSoNguoi;
//
//    public static JPanel createBookingPanel(MainFrameView mainFrame, SelectedRoomInfo selectedRoom) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setPreferredSize(new Dimension(950, 800));
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//
//        JPanel formPanel = createFormPanel();
//        JPanel buttonPanel = createButtonPanel(mainFrame, selectedRoom);
//
//        panel.add(formPanel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(buttonPanel);
//
//        return panel;
//    }
//
//    private static JPanel createFormPanel() {
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
//        // Số người
//        gbc.gridy = 0;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Số người:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        Integer[] soNguoiOptions = {1, 2, 3, 4};
//        cbSoNguoi = new JComboBox<>(soNguoiOptions);
//        formPanel.add(cbSoNguoi, gbc);
//        gbc.gridwidth = 1;
//
//        // Tài khoản
//        gbc.gridy++;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Tài khoản:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 2;
//        tfTaiKhoan = new JTextField();
//        formPanel.add(tfTaiKhoan, gbc);
//        gbc.gridx = 3;
//        gbc.gridwidth = 1;
//        JButton btnRandom = new JButton("Sinh ngẫu nhiên");
//        formPanel.add(btnRandom, gbc);
//
//        btnRandom.addActionListener(e -> {
//            String randomStr = "user" + String.format("%08d", new Random().nextInt(100_000_000));
//            tfTaiKhoan.setText(randomStr);
//        });
//
//        // SĐT
//        gbc.gridy++;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("SĐT:"), gbc);
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        tfSdt = new JTextField();
//        formPanel.add(tfSdt, gbc);
//
//        // Gmail
//        gbc.gridy++;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Gmail:"), gbc);
//        gbc.gridx = 1;
//        tfGmail = new JTextField();
//        formPanel.add(tfGmail, gbc);
//
//        String[] loaiOptions = {"Không có", "Mã định danh"};
//
//        for (int i = 0; i < 4; i++) {
//            int row = gbc.gridy + 1 + i * 2;
//
//            gbc.gridy = row;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
//            gbc.gridx = 1;
//            gbc.gridwidth = 3;
//            hoTenFields[i] = new JTextField();
//            formPanel.add(hoTenFields[i], gbc);
//
//            gbc.gridy = row + 1;
//            gbc.gridwidth = 1;
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
//        // Xử lý enable theo số người
//        cbSoNguoi.addActionListener(e -> {
//            int selected = (int) cbSoNguoi.getSelectedItem();
//            for (int i = 0; i < 4; i++) {
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
//    private static JPanel createButtonPanel(MainFrameView mainFrame, SelectedRoomInfo selectedRoom) {
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        btnPanel.setBackground(UIManager.getColor("Panel.background"));
//
//        JButton btnXacNhan = new JButton("Xác nhận");
//        JButton btnQuayLai = new JButton("Quay lại");
//
//        btnQuayLai.addActionListener(e -> {
//            JPanel searchPanel = AdminSearchView.createSearchPanel(mainFrame);
//            mainFrame.setAdminDynamicContent(searchPanel);
//        });
//
//        btnXacNhan.addActionListener(e -> {
//            int soNguoi = (int) cbSoNguoi.getSelectedItem();
//
//            String username = tfTaiKhoan.getText().trim();
//            String sdt = tfSdt.getText().trim();
//            String gmail = tfGmail.getText().trim();
//
//            if (username.isEmpty() || gmail.isEmpty() || sdt.isEmpty()) {
//                JOptionPane.showMessageDialog(mainFrame, "Vui lòng điền đầy đủ thông tin người đại diện!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Lấy người đại diện (người đầu tiên)
//            String hoTenDaiDien = hoTenFields[0].getText().trim();
//            String loaiDaiDien = (String) loaiCombos[0].getSelectedItem();
//            String maDaiDien = maFields[0].getText().trim();
//
//            if (hoTenDaiDien.isEmpty() || maDaiDien.isEmpty()) {
//                JOptionPane.showMessageDialog(mainFrame, "Vui lòng điền đủ họ tên và mã giấy tờ của người đại diện!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            StringBuilder danhSachKhach = new StringBuilder();
//            danhSachKhach.append("- ").append(hoTenDaiDien)
//                    .append(" [").append(loaiDaiDien).append(" : ").append(maDaiDien).append("]\n");
//
//            for (int i = 1; i < soNguoi; i++) {
//                String ten = hoTenFields[i].getText().trim();
//                String loai = (String) loaiCombos[i].getSelectedItem();
//                String ma = maFields[i].getText().trim();
//
//                if (ten.isEmpty() || ma.isEmpty()) continue;
//
//                danhSachKhach.append("- ").append(ten)
//                        .append(" [").append(loai).append(" : ").append(ma).append("]\n");
//            }
//
//            String thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//            String info = String.format("""
//THÔNG TIN ĐẶT PHÒNG
//
//Người dùng (tài khoản): %s
//SĐT: %s
//Gmail: %s
//Người đại diện: %s
//
//Số người: %d
//
//Danh sách khách:
//%s
//Phòng: %s
//Mô tả: %s
//Loại: %s
//Giá: %,.0f VND
//
//Check-in: %s
//Check-out: %s
//Tạo yêu cầu: %s
//Trạng thái: Đã gửi yêu cầu
//""",
//                    username,
//                    sdt,
//                    gmail,
//                    hoTenDaiDien,
//                    soNguoi,
//                    danhSachKhach.toString().trim(),
//                    selectedRoom.getRoomId(),
//                    selectedRoom.getDescription(),
//                    selectedRoom.getType(),
//                    selectedRoom.getPrice(),
//                    selectedRoom.getCheckIn(),
//                    selectedRoom.getCheckOut(),
//                    thoiGianTao
//            );
//
//            String[] options = {"Quay lại", "Xác nhận"};
//            int result = JOptionPane.showOptionDialog(
//                    mainFrame,
//                    info,
//                    "📋 Xác nhận đặt phòng",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.INFORMATION_MESSAGE,
//                    null,
//                    options,
//                    options[1]
//            );
//
//            if (result == 1) {
//                List<Person> danhSach = new ArrayList<>();
//                danhSach.add(new Person(hoTenDaiDien, loaiDaiDien, maDaiDien));
//
//                for (int i = 1; i < soNguoi; i++) {
//                    String ten = hoTenFields[i].getText().trim();
//                    String loai = (String) loaiCombos[i].getSelectedItem();
//                    String ma = maFields[i].getText().trim();
//
//                    if (ten.isEmpty() || ma.isEmpty()) continue;
//
//                    danhSach.add(new Person(ten, loai, ma));
//                }
//
////                RequestService.createRequest(
////                        username,
////                        hoTenDaiDien,
////                        gmail,
////                        sdt,
////                        selectedRoom.getRoomId(),
////                        selectedRoom.getCheckIn(),
////                        selectedRoom.getCheckOut(),
////                        selectedRoom.getPrice(),
////                        danhSach
////                );
//
//                JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                JPanel homePanel = AdminDashboardView.createDashboardPanel();
//                mainFrame.setAdminDynamicContent(homePanel);
//            }
//        });
//
//
//
//
//        btnPanel.add(btnQuayLai);
//        btnPanel.add(btnXacNhan);
//        return btnPanel;
//    }
//}


package org.example.view;

import org.example.entity.Person;
import org.example.entity.SelectedRoomInfo;
import org.example.service.RequestService;
import org.example.service.BookingService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminBookingView {

    private static JTextField tfTaiKhoan, tfSdt, tfGmail;
    private static JTextField[] hoTenFields = new JTextField[4];
    private static JComboBox<String>[] loaiCombos = new JComboBox[4];
    private static JTextField[] maFields = new JTextField[4];
    private static JComboBox<Integer> cbSoNguoi;

    public static JPanel createBookingPanel(MainFrameView mainFrame, SelectedRoomInfo selectedRoom) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel(mainFrame, selectedRoom);

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);

        return panel;
    }

    private static JPanel createFormPanel() {
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
        Integer[] soNguoiOptions = {1, 2, 3, 4};
        cbSoNguoi = new JComboBox<>(soNguoiOptions);
        formPanel.add(cbSoNguoi, gbc);
        gbc.gridwidth = 1;

        // Tài khoản
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Tài khoản:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        tfTaiKhoan = new JTextField();
        formPanel.add(tfTaiKhoan, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        JButton btnRandom = new JButton("Sinh ngẫu nhiên");
        formPanel.add(btnRandom, gbc);

        btnRandom.addActionListener(e -> {
            String randomStr = "user" + String.format("%08d", new Random().nextInt(100_000_000));
            tfTaiKhoan.setText(randomStr);
        });

        // SĐT
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        tfSdt = new JTextField();
        formPanel.add(tfSdt, gbc);

        // Gmail
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Gmail:"), gbc);
        gbc.gridx = 1;
        tfGmail = new JTextField();
        formPanel.add(tfGmail, gbc);

        // Danh sách người đi cùng
        String[] loaiOptions = {"Không có", "Mã định danh"};

        for (int i = 0; i < 4; i++) {
            int row = gbc.gridy + 1 + i * 2;

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

        // Kích hoạt đúng số người
        cbSoNguoi.addActionListener(e -> {
            int selected = (int) cbSoNguoi.getSelectedItem();
            for (int i = 0; i < 4; i++) {
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

    private static JPanel createButtonPanel(MainFrameView mainFrame, SelectedRoomInfo selectedRoom) {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton btnXacNhan = new JButton("Xác nhận");
        JButton btnQuayLai = new JButton("Quay lại");

        btnXacNhan.addActionListener(e -> {
            int soNguoi = (int) cbSoNguoi.getSelectedItem();

            String username = tfTaiKhoan.getText().trim();
            String sdt = tfSdt.getText().trim();
            String gmail = tfGmail.getText().trim();

            if (username.isEmpty() || gmail.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Vui lòng điền đầy đủ thông tin người đại diện!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hoTenDaiDien = hoTenFields[0].getText().trim();
            String loaiDaiDien = (String) loaiCombos[0].getSelectedItem();
            String maDaiDien = maFields[0].getText().trim();

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
                String ten = hoTenFields[i].getText().trim();
                String loai = (String) loaiCombos[i].getSelectedItem();
                String ma = maFields[i].getText().trim();

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
                // Sinh mã booking ngẫu nhiên
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
        });


        btnPanel.add(btnQuayLai);
        btnPanel.add(btnXacNhan);
        return btnPanel;
    }
}
