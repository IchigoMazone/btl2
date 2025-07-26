
package org.example.view;

import org.example.service.RequestService;
import org.example.service.NotificationService;
import org.example.entity.Person;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.example.entity.SelectedRoomInfo;
import org.example.service.UserInfoService;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Objects;
import java.util.UUID;

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
        JComboBox<String> cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
        formPanel.add(cbLoaiChung, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Mã:"), gbc);
        gbc.gridx = 3;
        tfMaChung = new JTextField();
        formPanel.add(tfMaChung, gbc);

        String[] loaiOptions = {"Không có", "Mã định danh", "Hộ chiếu"};

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

    private static String generateRequestId() {
        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
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
            int soNguoi = (int) cbSoNguoi.getSelectedItem();

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



            String thoiGianTao;
            try {
                thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

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
                    username,
                    hoTen,
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

            JTextArea textArea = new JTextArea(info);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setEditable(false);
            textArea.setMargin(new Insets(10, 10, 10, 10));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

// Đặt JTextArea vào JScrollPane
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

// Tạo nội dung panel chứa nội dung cuộn
            JPanel content = new JPanel(new BorderLayout());
            content.add(scrollPane, BorderLayout.CENTER);

// Tùy chọn nút
            //Object[] options = {"Quay lại", "Xác nhận"};

            Object[] options = {"Quay lại", "Xác nhận"};

            int result = JOptionPane.showOptionDialog(
                    mainFrame,
                    content,
                    "Xác nhận đặt phòng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[1] // mặc định là "Xác nhận"
            );


            if (result == 1) { // Xác nhận
                // Tạo danh sách Person từ các trường nhập
                List<Person> danhSach = new ArrayList<>();

                // Người đại diện là người đầu tiên
                danhSach.add(new Person(hoTen, (String) loaiCombos[0].getSelectedItem(), cccdNguoiDaiDien));

                for (int i = 0; i < soNguoi; i++) {
                    String ten = hoTenFields[i].getText().trim();
                    String loai = (String) loaiCombos[i].getSelectedItem();
                    String ma = maFields[i].getText().trim();

                    if (ten.isEmpty() || ma.isEmpty()) continue;

                    danhSach.add(new Person(ten, loai, ma));
                }

                // Gửi request
                RequestService.createRequest(
                        requestId,
                        username,
                        hoTen,
                        gmail,
                        sdt,
                        selectedRoom.getRoomId(),
                        selectedRoom.getCheckIn(),
                        selectedRoom.getCheckOut(),
                        selectedRoom.getPrice(),
                        danhSach
                );

                NotificationService.createNotification(
                        "BK00000001", requestId, username,
                        "Yêu cầu duyệt", "Đã gửi"
                );

                // Thông báo và quay về giao diện tìm kiếm
                JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                JPanel homePanel = CustomerDashboardView.createUserHomePanel();
                mainFrame.setCustomerDynamicContent(homePanel);
                mainFrame.setCustomerSelectedMenu("Trang chủ");
            }
        });


        btnPanel.add(btnQuayLai);
        btnPanel.add(btnXacNhan);

        return btnPanel;
    }
}


