//
//package org.example.view;
//
//import org.example.service.RequestService;
//import org.example.service.NotificationService;
//import org.example.entity.Person;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import org.example.entity.SelectedRoomInfo;
//import org.example.service.UserInfoService;
//import java.util.UUID;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.util.Objects;
//import java.util.UUID;
//
//public class CustomerBookingView {
//    public  JComboBox<String> cbMaChung;
//
//    public static JPanel createBookingPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setPreferredSize(new Dimension(950, 800));
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//
//        // Form và button panel
//        JPanel formPanel = createFormPanel(mainFrame, username, selectedRoom);
//        JPanel buttonPanel = createButtonPanel(mainFrame, username, selectedRoom);
//
//        panel.add(formPanel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(buttonPanel);
//
//        return panel;
//    }
//
//    private static JTextField[] hoTenFields = new JTextField[3];
//    private static JComboBox<String>[] loaiCombos = new JComboBox[3];
//    private static JTextField[] maFields = new JTextField[3];
//    private static JComboBox<Integer> cbSoNguoi;
//    private static JTextField tfMaChung;
//
//    private static JPanel createFormPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
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
//        Integer[] soNguoiOptions = {0, 1, 2, 3};
//        cbSoNguoi = new JComboBox<>(soNguoiOptions);
//        formPanel.add(cbSoNguoi, gbc);
//        gbc.gridwidth = 1;
//
//        // Người đại diện (người đặt phòng)
////        gbc.gridy = 1;
////        gbc.gridx = 0;
////        formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
////        gbc.gridx = 1;
////        JComboBox<String> cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
////        formPanel.add(cbLoaiChung, gbc);
////        gbc.gridx = 2;
////        formPanel.add(new JLabel("Mã:"), gbc);
////        gbc.gridx = 3;
////        tfMaChung = new JTextField();
////        formPanel.add(tfMaChung, gbc);
////
////        cbLoaiChung.addActionListener(e -> {
////            String selected = (String) cbLoaiChung.getSelectedItem();
////            if ("Không có".equals(selected)) {
////                tfMaChung.setText("Trẻ em");
////                tfMaChung.setEditable(false);
////            }
////        });
//
//        gbc.gridy = 1;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
//
//        gbc.gridx = 1;
//        JComboBox<String> cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
//        formPanel.add(cbLoaiChung, gbc);
//
//        gbc.gridx = 2;
//        formPanel.add(new JLabel("Mã:"), gbc);
//
//        gbc.gridx = 3;
//        cbMaChung = new JComboBox<>();
//        cbMaChung.setEditable(true);
//
//        formPanel.add(cbMaChung, gbc);
//
//// Listener cho loại giấy tờ
//        cbLoaiChung.addActionListener(e -> {
//            String selected = (String) cbLoaiChung.getSelectedItem();
//
//            if ("Không có".equals(selected)) {
//                // Thêm các lựa chọn đặc biệt nếu chưa có
//                DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cbMaChung.getModel();
//                if (model.getIndexOf("Trẻ em") == -1) model.addElement("Trẻ em");
//                if (model.getIndexOf("Giấy khai sinh") == -1) model.addElement("Giấy khai sinh");
//
//                cbMaChung.setSelectedItem("Trẻ em");
//            } else {
//                cbMaChung.setSelectedItem(""); // reset khi chọn loại giấy khác
//            }
//        });
//
//
//        String[] loaiOptions = {"Không có", "Mã định danh", "Hộ chiếu"};
//
//        for (int i = 0; i < 3; i++) {
//            int row = i * 2 + 2;
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
//    private static String generateRequestId() {
//        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
//    }
//
//    private static JPanel createButtonPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
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
//            int soNguoi = (int) cbSoNguoi.getSelectedItem();
//
//            String hoTen = UserInfoService.getFullName("userinfos.xml", username);
//            String gmail = UserInfoService.getEmail("userinfos.xml", username);
//            String sdt = UserInfoService.getPhone("userinfos.xml", username);
//            //String cccdNguoiDaiDien = tfMaChung.getText();
//            String cccdNguoiDaiDien = (String) cbMaChung.getEditor().getItem();
//
//
//            StringBuilder danhSachKhach = new StringBuilder();
//            danhSachKhach.append("- ").append(hoTen)
//                    .append(" [Mã định danh : ").append(cccdNguoiDaiDien).append("]\n");
//
//            for (int i = 0; i < soNguoi; i++) {
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
//            String thoiGianTao;
//            try {
//                thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//
//            String requestId = generateRequestId();
//
//            String info = String.format("""
//    THÔNG TIN ĐẶT PHÒNG
//
//    Người dùng: %s
//    Người đại diện: %s
//    Gmail: %s
//    SĐT: %s
//
//    Số người: %d
//
//    Danh sách khách:
//    %s
//
//    Phòng: %s
//    Mô tả: %s
//    Loại: %s
//    Giá: %,.0f VND
//
//    Check-in: %s
//    Check-out: %s
//    Tạo yêu cầu: %s
//    Trạng thái: Đã gửi yêu cầu
//    """,
//                    username,
//                    hoTen,
//                    gmail,
//                    sdt,
//                    soNguoi,
//                    danhSachKhach.toString(),
//                    selectedRoom.getRoomId(),
//                    selectedRoom.getDescription(),
//                    selectedRoom.getType(),
//                    selectedRoom.getPrice(),
//                    selectedRoom.getCheckIn(),
//                    selectedRoom.getCheckOut(),
//                    thoiGianTao
//            );
//
//            JTextArea textArea = new JTextArea(info);
//            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            textArea.setEditable(false);
//            textArea.setMargin(new Insets(10, 10, 10, 10));
//            textArea.setLineWrap(true);
//            textArea.setWrapStyleWord(true);
//
//            JScrollPane scrollPane = new JScrollPane(textArea);
//            scrollPane.setPreferredSize(new Dimension(500, 400));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.add(scrollPane, BorderLayout.CENTER);
//
//            Object[] options = {"Quay lại", "Xác nhận"};
//
//            int result = JOptionPane.showOptionDialog(
//                    mainFrame,
//                    content,
//                    "Xác nhận đặt phòng",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.PLAIN_MESSAGE,
//                    null,
//                    options,
//                    options[1]
//            );
//
//
//            if (result == 1) { // Xác nhận
//
//                List<Person> danhSach = new ArrayList<>();
//
//                // Người đại diện là người đầu tiên
//                danhSach.add(new Person(hoTen, (String) loaiCombos[0].getSelectedItem(), cccdNguoiDaiDien));
//
//                for (int i = 0; i < soNguoi; i++) {
//                    String ten = hoTenFields[i].getText().trim();
//                    String loai = (String) loaiCombos[i].getSelectedItem();
//                    String ma = maFields[i].getText().trim();
//
//                    if (ten.isEmpty() || ma.isEmpty()) continue;
//
//                    danhSach.add(new Person(ten, loai, ma));
//                }
//
//                // Gửi request
//                RequestService.createRequest(
//                        requestId,
//                        username,
//                        hoTen,
//                        gmail,
//                        sdt,
//                        selectedRoom.getRoomId(),
//                        selectedRoom.getCheckIn(),
//                        selectedRoom.getCheckOut(),
//                        selectedRoom.getPrice(),
//                        danhSach
//                );
//
//                NotificationService.createNotification(
//                        "BK00000001", requestId, username,
//                        "Yêu cầu duyệt", "Đã gửi"
//                );
//
//                // Thông báo và quay về giao diện tìm kiếm
//                JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                JPanel homePanel = CustomerDashboardView.createUserHomePanel();
//                mainFrame.setCustomerDynamicContent(homePanel);
//                mainFrame.setCustomerSelectedMenu("Trang chủ");
//            }
//        });
//
//
//        btnPanel.add(btnQuayLai);
//        btnPanel.add(btnXacNhan);
//
//        return btnPanel;
//    }
//}
//
//
//
//


//package org.example.view;
//
//import org.example.entity.Person;
//import org.example.entity.SelectedRoomInfo;
//import org.example.service.NotificationService;
//import org.example.service.RequestService;
//import org.example.service.UserInfoService;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class CustomerBookingView {
//    private static JTextField[] hoTenFields = new JTextField[3];
//    private static JComboBox<String>[] loaiCombos = new JComboBox[3];
//    private static JTextField[] maTextFields = new JTextField[3];
//    private static JComboBox<String>[] maComboFields = new JComboBox[3];
//    private static CardLayout[] maCardLayouts = new CardLayout[3];
//    private static JPanel[] maPanels = new JPanel[3];
//
//    private static JComboBox<Integer> cbSoNguoi;
//    private static JComboBox<String> cbLoaiChung;
//    private static JComboBox<String> cbMaChung;
//    private static JTextField tfMaChung;
//    private static JPanel panelMaChung;
//    private static CardLayout cardLayoutMaChung;
//
//    public static JPanel createBookingPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setPreferredSize(new Dimension(950, 800));
//        panel.setBackground(Color.WHITE);
//
//        JPanel formPanel = createFormPanel(mainFrame, username, selectedRoom);
//        JPanel buttonPanel = createButtonPanel(mainFrame, username, selectedRoom);
//
//        panel.add(formPanel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(buttonPanel);
//
//        return panel;
//    }
//
//    private static JPanel createFormPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
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
//
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        Integer[] soNguoiOptions = {0, 1, 2, 3};
//        cbSoNguoi = new JComboBox<>(soNguoiOptions);
//        formPanel.add(cbSoNguoi, gbc);
//        gbc.gridwidth = 1;
//
//        // Loại giấy tờ đại diện
//        gbc.gridy = 1;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
//
//        gbc.gridx = 1;
//        cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
//        formPanel.add(cbLoaiChung, gbc);
//
//        gbc.gridx = 2;
//        formPanel.add(new JLabel("Mã:"), gbc);
//
//        gbc.gridx = 3;
//        cardLayoutMaChung = new CardLayout();
//        panelMaChung = new JPanel(cardLayoutMaChung);
//
//        Dimension fixedSize = new Dimension(150, 25);
//        cbMaChung = new JComboBox<>(new String[]{"Trẻ em", "Giấy khai sinh"});
//        cbMaChung.setPreferredSize(fixedSize);
//        tfMaChung = new JTextField();
//        tfMaChung.setPreferredSize(fixedSize);
//
//        panelMaChung.add(cbMaChung, "combo");
//        panelMaChung.add(tfMaChung, "text");
//        formPanel.add(panelMaChung, gbc);
//
//        Runnable updateMaChungField = () -> {
//            String selected = (String) cbLoaiChung.getSelectedItem();
//            if ("Không có".equals(selected)) {
//                cbMaChung.setSelectedItem("Trẻ em");
//                cardLayoutMaChung.show(panelMaChung, "combo");
//            } else {
//                tfMaChung.setText("");
//                cardLayoutMaChung.show(panelMaChung, "text");
//            }
//        };
//
//        updateMaChungField.run();
//        cbLoaiChung.addActionListener(e -> updateMaChungField.run());
//
//        String[] loaiOptions = {"Không có", "Mã định danh", "Hộ chiếu"};
//        String[] comboOptions = {"Trẻ em", "Giấy khai sinh"};
//        Dimension maFieldSize = new Dimension(150, 25);
//
//        for (int i = 0; i < 3; i++) {
//            int row = i * 2 + 2;
//
//            gbc.gridy = row;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
//
//            gbc.gridx = 1;
//            gbc.gridwidth = 3;
//            hoTenFields[i] = new JTextField();
//            formPanel.add(hoTenFields[i], gbc);
//            gbc.gridwidth = 1;
//
//            gbc.gridy = row + 1;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
//
//            loaiCombos[i] = new JComboBox<>(loaiOptions);
//            gbc.gridx = 1;
//            formPanel.add(loaiCombos[i], gbc);
//
//            gbc.gridx = 2;
//            formPanel.add(new JLabel("Mã:"), gbc);
//
//            gbc.gridx = 3;
//            maCardLayouts[i] = new CardLayout();
//            maPanels[i] = new JPanel(maCardLayouts[i]);
//
//            maComboFields[i] = new JComboBox<>(comboOptions);
//            maComboFields[i].setPreferredSize(maFieldSize);
//            maTextFields[i] = new JTextField();
//            maTextFields[i].setPreferredSize(maFieldSize);
//
//            maPanels[i].add(maComboFields[i], "combo");
//            maPanels[i].add(maTextFields[i], "text");
//            formPanel.add(maPanels[i], gbc);
//
//            int index = i;
//            loaiCombos[i].addActionListener(e -> {
//                String selected = (String) loaiCombos[index].getSelectedItem();
//                if ("Không có".equals(selected)) {
//                    maComboFields[index].setSelectedItem("Trẻ em");
//                    maCardLayouts[index].show(maPanels[index], "combo");
//                } else {
//                    maTextFields[index].setText("");
//                    maCardLayouts[index].show(maPanels[index], "text");
//                }
//            });
//
//            loaiCombos[i].setSelectedIndex(0);
//        }
//
//        cbSoNguoi.addActionListener(e -> {
//            int selected = (int) cbSoNguoi.getSelectedItem();
//            for (int i = 0; i < 3; i++) {
//                boolean enable = i < selected;
//                hoTenFields[i].setEnabled(enable);
//                loaiCombos[i].setEnabled(enable);
//                maComboFields[i].setEnabled(enable);
//                maTextFields[i].setEnabled(enable);
//
//                if (!enable) {
//                    hoTenFields[i].setText("");
//                    maTextFields[i].setText("");
//                    maComboFields[i].setSelectedIndex(0);
//                }
//
//                String selectedLoai = (String) loaiCombos[i].getSelectedItem();
//                maCardLayouts[i].show(maPanels[i], "Không có".equals(selectedLoai) ? "combo" : "text");
//            }
//        });
//
//        cbSoNguoi.setSelectedIndex(0);
//        return formPanel;
//    }
//
//    private static JPanel createButtonPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
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
//            int soNguoi = (int) cbSoNguoi.getSelectedItem();
//            String hoTen = UserInfoService.getFullName("userinfos.xml", username);
//            String gmail = UserInfoService.getEmail("userinfos.xml", username);
//            String sdt = UserInfoService.getPhone("userinfos.xml", username);
//
//            // Lấy mã giấy tờ đại diện
//            String cccdNguoiDaiDien;
//            String loaiChung = (String) cbLoaiChung.getSelectedItem();
//            if ("Không có".equals(loaiChung)) {
//                cccdNguoiDaiDien = (String) cbMaChung.getSelectedItem();
//            }
//            else {
//                cccdNguoiDaiDien = tfMaChung.getText().trim();
//            }
//
//            // Kiểm tra dữ liệu hợp lệ
//            if (soNguoi > 0 && hoTen.isEmpty()) {
//                JOptionPane.showMessageDialog(mainFrame, "Vui lòng nhập họ tên người đại diện!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            StringBuilder danhSachKhach = new StringBuilder();
//            danhSachKhach.append("- ").append(hoTen).append(" [").append(loaiChung)
//                    .append(" : ").append(cccdNguoiDaiDien).append("]\n");
//
//            List<Person> danhSach = new ArrayList<>();
//            danhSach.add(new Person(hoTen, loaiChung, cccdNguoiDaiDien));
//
//            for (int i = 0; i < soNguoi; i++) {
//                String ten = hoTenFields[i].getText().trim();
//                String loai = (String) loaiCombos[i].getSelectedItem();
//                String ma = "Không có".equals(loai) ? (String) maComboFields[i].getSelectedItem() : maTextFields[i].getText().trim();
//
//                if (!ten.isEmpty() && (!ma.isEmpty() || "Không có".equals(loai))) {
//                    danhSachKhach.append("- ").append(ten).append(" [").append(loai).append(" : ").append(ma).append("]\n");
//                    danhSach.add(new Person(ten, loai, ma));
//                }
//            }
//
//            String thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            String requestId = generateRequestId();
//
//            String info = String.format("""
//                    THÔNG TIN ĐẶT PHÒNG
//
//                    Người dùng: %s
//                    Người đại diện: %s
//                    Gmail: %s
//                    SĐT: %s
//
//                    Số người: %d
//
//                    Danh sách khách:
//                    %s
//
//                    Phòng: %s
//                    Mô tả: %s
//                    Loại: %s
//                    Giá: %,.0f VND
//
//                    Check-in: %s
//                    Check-out: %s
//                    Tạo yêu cầu: %s
//                    Trạng thái: Đã gửi yêu cầu
//                    """,
//                    username, hoTen, gmail, sdt, soNguoi, danhSachKhach,
//                    selectedRoom.getRoomId(), selectedRoom.getDescription(),
//                    selectedRoom.getType(), selectedRoom.getPrice(),
//                    selectedRoom.getCheckIn(), selectedRoom.getCheckOut(), thoiGianTao);
//
//            JTextArea textArea = new JTextArea(info);
//            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            textArea.setEditable(false);
//            textArea.setMargin(new Insets(10, 10, 10, 10));
//            textArea.setLineWrap(true);
//            textArea.setWrapStyleWord(true);
//
//            JScrollPane scrollPane = new JScrollPane(textArea);
//            scrollPane.setPreferredSize(new Dimension(500, 400));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.add(scrollPane, BorderLayout.CENTER);
//
//            Object[] options = {"Quay lại", "Xác nhận"};
//
//            int result = JOptionPane.showOptionDialog(
//                    mainFrame,
//                    content,
//                    "Xác nhận đặt phòng",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.PLAIN_MESSAGE,
//                    null,
//                    options,
//                    options[1]
//            );
//
//            if (result == 1) {
//                RequestService.createRequest(
//                        requestId, username, hoTen, gmail, sdt,
//                        selectedRoom.getRoomId(),
//                        selectedRoom.getCheckIn(), selectedRoom.getCheckOut(),
//                        selectedRoom.getPrice(), danhSach
//                );
//
//                NotificationService.createNotification(
//                        "BK00000001", requestId, username,
//                        "Yêu cầu duyệt", "Đã gửi"
//                );
//
//                JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                JPanel homePanel = CustomerDashboardView.createUserHomePanel();
//                mainFrame.setCustomerDynamicContent(homePanel);
//                mainFrame.setCustomerSelectedMenu("Trang chủ");
//            }
//        });
//
//        btnPanel.add(btnQuayLai);
//        btnPanel.add(btnXacNhan);
//        return btnPanel;
//    }
//
//    private static String generateRequestId() {
//        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
//    }
//}

//package org.example.view;
//
//import org.example.entity.Person;
//import org.example.entity.SelectedRoomInfo;
//import org.example.service.NotificationService;
//import org.example.service.RequestService;
//import org.example.service.UserInfoService;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.regex.Pattern;
//import java.util.UUID;
//
//public class CustomerBookingView {
//    private static JTextField[] hoTenFields = new JTextField[3];
//    private static JComboBox<String>[] loaiCombos = new JComboBox[3];
//    private static JTextField[] maTextFields = new JTextField[3];
//    private static JComboBox<String>[] maComboFields = new JComboBox[3];
//    private static CardLayout[] maCardLayouts = new CardLayout[3];
//    private static JPanel[] maPanels = new JPanel[3];
//
//    private static JComboBox<Integer> cbSoNguoi;
//    private static JComboBox<String> cbLoaiChung;
//    private static JComboBox<String> cbMaChung;
//    private static JTextField tfMaChung;
//    private static JPanel panelMaChung;
//    private static CardLayout cardLayoutMaChung;
//
//    // Regex cho mã định danh (12 chữ số) và hộ chiếu (8 ký tự chữ hoặc số)
//    private static final Pattern MA_DINH_DANH_PATTERN = Pattern.compile("\\d{12}");
//    private static final Pattern HO_CHIEU_PATTERN = Pattern.compile("[A-Za-z0-9]{8}");
//
//    public static JPanel createBookingPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setPreferredSize(new Dimension(950, 800));
//        panel.setBackground(Color.WHITE);
//
//        JPanel formPanel = createFormPanel(mainFrame, username, selectedRoom);
//        JPanel buttonPanel = createButtonPanel(mainFrame, username, selectedRoom);
//
//        panel.add(formPanel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(buttonPanel);
//
//        return panel;
//    }
//
//    private static JPanel createFormPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
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
//
//        gbc.gridx = 1;
//        gbc.gridwidth = 3;
//        Integer[] soNguoiOptions = {0, 1, 2, 3};
//        cbSoNguoi = new JComboBox<>(soNguoiOptions);
//        formPanel.add(cbSoNguoi, gbc);
//        gbc.gridwidth = 1;
//
//        // Loại giấy tờ đại diện
//        gbc.gridy = 1;
//        gbc.gridx = 0;
//        formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
//
//        gbc.gridx = 1;
//        cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
//        formPanel.add(cbLoaiChung, gbc);
//
//        gbc.gridx = 2;
//        formPanel.add(new JLabel("Mã:"), gbc);
//
//        gbc.gridx = 3;
//        cardLayoutMaChung = new CardLayout();
//        panelMaChung = new JPanel(cardLayoutMaChung);
//
//        Dimension fixedSize = new Dimension(150, 25);
//        cbMaChung = new JComboBox<>(new String[]{"Không xác định", "Dưới 14 tuổi"});
//        cbMaChung.setPreferredSize(fixedSize);
//        tfMaChung = new JTextField();
//        tfMaChung.setPreferredSize(fixedSize);
//
//        panelMaChung.add(cbMaChung, "combo");
//        panelMaChung.add(tfMaChung, "text");
//        formPanel.add(panelMaChung, gbc);
//
//        Runnable updateMaChungField = () -> {
//            String selected = (String) cbLoaiChung.getSelectedItem();
//            if ("Không có".equals(selected)) {
//                cbMaChung.setSelectedItem("Không xác định");
//                cardLayoutMaChung.show(panelMaChung, "combo");
//            } else {
//                tfMaChung.setText("");
//                cardLayoutMaChung.show(panelMaChung, "text");
//            }
//        };
//
//        updateMaChungField.run();
//        cbLoaiChung.addActionListener(e -> updateMaChungField.run());
//
//        String[] loaiOptions = {"Không có", "Mã định danh", "Hộ chiếu"};
//        String[] comboOptions = {"Không xác định", "Dưới 14 tuổi"};
//        Dimension maFieldSize = new Dimension(150, 25);
//
//        for (int i = 0; i < 3; i++) {
//            int row = i * 2 + 2;
//
//            gbc.gridy = row;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);
//
//            gbc.gridx = 1;
//            gbc.gridwidth = 3;
//            hoTenFields[i] = new JTextField();
//            formPanel.add(hoTenFields[i], gbc);
//            gbc.gridwidth = 1;
//
//            gbc.gridy = row + 1;
//            gbc.gridx = 0;
//            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);
//
//            loaiCombos[i] = new JComboBox<>(loaiOptions);
//            gbc.gridx = 1;
//            formPanel.add(loaiCombos[i], gbc);
//
//            gbc.gridx = 2;
//            formPanel.add(new JLabel("Mã:"), gbc);
//
//            gbc.gridx = 3;
//            maCardLayouts[i] = new CardLayout();
//            maPanels[i] = new JPanel(maCardLayouts[i]);
//
//            maComboFields[i] = new JComboBox<>(comboOptions);
//            maComboFields[i].setPreferredSize(maFieldSize);
//            maTextFields[i] = new JTextField();
//            maTextFields[i].setPreferredSize(maFieldSize);
//
//            maPanels[i].add(maComboFields[i], "combo");
//            maPanels[i].add(maTextFields[i], "text");
//            formPanel.add(maPanels[i], gbc);
//
//            int index = i;
//            loaiCombos[i].addActionListener(e -> {
//                String selected = (String) loaiCombos[index].getSelectedItem();
//                if ("Không có".equals(selected)) {
//                    maComboFields[index].setSelectedItem("Không xác định");
//                    maCardLayouts[index].show(maPanels[index], "combo");
//                } else {
//                    maTextFields[index].setText("");
//                    maCardLayouts[index].show(maPanels[index], "text");
//                }
//            });
//
//            loaiCombos[i].setSelectedIndex(0);
//        }
//
//        cbSoNguoi.addActionListener(e -> {
//            int selected = (int) cbSoNguoi.getSelectedItem();
//            for (int i = 0; i < 3; i++) {
//                boolean enable = i < selected;
//                hoTenFields[i].setEnabled(enable);
//                loaiCombos[i].setEnabled(enable);
//                maComboFields[i].setEnabled(enable);
//                maTextFields[i].setEnabled(enable);
//
//                if (!enable) {
//                    hoTenFields[i].setText("");
//                    maTextFields[i].setText("");
//                    maComboFields[i].setSelectedIndex(0);
//                }
//
//                String selectedLoai = (String) loaiCombos[i].getSelectedItem();
//                maCardLayouts[i].show(maPanels[i], "Không có".equals(selectedLoai) ? "combo" : "text");
//            }
//        });
//
//        cbSoNguoi.setSelectedIndex(0);
//        return formPanel;
//    }
//
//    // Hàm kiểm tra định dạng mã giấy tờ
//    private static boolean isValidMaGiayTo(String loai, String ma) {
//        if ("Không có".equals(loai)) {
//            return ma.equals("Không xác định ") || ma.equals("Dưới 14 tuổi");
//        } else if ("Mã định danh".equals(loai)) {
//            return MA_DINH_DANH_PATTERN.matcher(ma).matches();
//        } else if ("Hộ chiếu".equals(loai)) {
//            return HO_CHIEU_PATTERN.matcher(ma).matches();
//        }
//        return false;
//    }
//
//    // Hàm kiểm tra thông tin đầu vào và trùng lặp mã
//    private static String validateInput(MainFrameView mainFrame, int soNguoi, String hoTen, String loaiChung, String cccdNguoiDaiDien) {
//        // Kiểm tra họ tên người đại diện
//        if (hoTen.isEmpty()) {
//            return "Họ tên người đại diện không được để trống!";
//        }
//
//        // Kiểm tra mã giấy tờ người đại diện
//        if (!"Không có".equals(loaiChung) && cccdNguoiDaiDien.isEmpty()) {
//            return "Mã giấy tờ người đại diện không được để trống!";
//        }
//        if (!isValidMaGiayTo(loaiChung, cccdNguoiDaiDien)) {
//            return "Mã giấy tờ người đại diện không đúng định dạng!\n" +
//                    "Mã định danh: 12 chữ số\nHộ chiếu: 8 ký tự chữ hoặc số";
//        }
//
//        // Kiểm tra thông tin các khách và trùng lặp mã
//        int validGuests = 0;
//        Set<String> maDinhDanhSet = new HashSet<>();
//        Set<String> hoChieuSet = new HashSet<>();
//
//        // Thêm mã của người đại diện vào tập hợp kiểm tra
//        if ("Mã định danh".equals(loaiChung)) {
//            maDinhDanhSet.add(cccdNguoiDaiDien);
//        } else if ("Hộ chiếu".equals(loaiChung)) {
//            hoChieuSet.add(cccdNguoiDaiDien);
//        }
//
//        for (int i = 0; i < soNguoi; i++) {
//            String ten = hoTenFields[i].getText().trim();
//            String loai = (String) loaiCombos[i].getSelectedItem();
//            String ma = "Không có".equals(loai) ? (String) maComboFields[i].getSelectedItem() : maTextFields[i].getText().trim();
//
//            if (ten.isEmpty() && !ma.isEmpty()) {
//                return "Họ tên người " + (i + 1) + " không được để trống khi đã nhập mã!";
//            }
//            if (!ten.isEmpty() && !"Không có".equals(loai) && ma.isEmpty()) {
//                return "Mã giấy tờ người " + (i + 1) + " không được để trống!";
//            }
//            if (!ten.isEmpty() && !isValidMaGiayTo(loai, ma)) {
//                return "Mã giấy tờ người " + (i + 1) + " không đúng định dạng!\n" +
//                        "Mã định danh: 12 chữ số\nHộ chiếu: 8 ký tự chữ hoặc số";
//            }
//            if (!ten.isEmpty()) {
//                validGuests++;
//                // Kiểm tra trùng mã
//                if ("Mã định danh".equals(loai)) {
//                    if (!maDinhDanhSet.add(ma)) {
//                        return "Mã định danh của người " + (i + 1) + " trùng với mã đã nhập trước đó!";
//                    }
//                } else if ("Hộ chiếu".equals(loai)) {
//                    if (!hoChieuSet.add(ma)) {
//                        return "Hộ chiếu của người " + (i + 1) + " trùng với mã đã nhập trước đó!";
//                    }
//                }
//            }
//        }
//
//        // Kiểm tra số người hợp lệ
//        if (validGuests < soNguoi) {
//            return "Số lượng khách hợp lệ (" + validGuests + ") không khớp với số người đã chọn (" + soNguoi + ")!";
//        }
//
//        return null; // Không có lỗi
//    }
//
//    private static JPanel createButtonPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
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
//            int soNguoi = (int) cbSoNguoi.getSelectedItem();
//            String hoTen = UserInfoService.getFullName("userinfos.xml", username);
//            String gmail = UserInfoService.getEmail("userinfos.xml", username);
//            String sdt = UserInfoService.getPhone("userinfos.xml", username);
//
//            // Lấy mã giấy tờ đại diện
//            String cccdNguoiDaiDien;
//            String loaiChung = (String) cbLoaiChung.getSelectedItem();
//            if ("Không có".equals(loaiChung)) {
//                cccdNguoiDaiDien = (String) cbMaChung.getSelectedItem();
//            } else {
//                cccdNguoiDaiDien = tfMaChung.getText().trim();
//            }
//
//            // Kiểm tra dữ liệu đầu vào
//            String errorMessage = validateInput(mainFrame, soNguoi, hoTen, loaiChung, cccdNguoiDaiDien);
//            if (errorMessage != null) {
//                JOptionPane.showMessageDialog(mainFrame, errorMessage, "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            StringBuilder danhSachKhach = new StringBuilder();
//            danhSachKhach.append("- ").append(hoTen).append(" [").append(loaiChung)
//                    .append(" : ").append(cccdNguoiDaiDien).append("]\n");
//
//            List<Person> danhSach = new ArrayList<>();
//            danhSach.add(new Person(hoTen, loaiChung, cccdNguoiDaiDien));
//
//            for (int i = 0; i < soNguoi; i++) {
//                String ten = hoTenFields[i].getText().trim();
//                String loai = (String) loaiCombos[i].getSelectedItem();
//                String ma = "Không có".equals(loai) ? (String) maComboFields[i].getSelectedItem() : maTextFields[i].getText().trim();
//
//                if (!ten.isEmpty()) {
//                    danhSachKhach.append("- ").append(ten).append(" [").append(loai).append(" : ").append(ma).append("]\n");
//                    danhSach.add(new Person(ten, loai, ma));
//                }
//            }
//
//            String thoiGianTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            String requestId = generateRequestId();
//
//            String info = String.format("""
//                    THÔNG TIN ĐẶT PHÒNG
//
//                    Người dùng: %s
//                    Người đại diện: %s
//                    Gmail: %s
//                    SĐT: %s
//
//                    Số người: %d
//
//                    Danh sách khách:
//                    %s
//
//                    Phòng: %s
//                    Mô tả: %s
//                    Loại: %s
//                    Giá: %,.0f VND
//
//                    Check-in: %s
//                    Check-out: %s
//                    Tạo yêu cầu: %s
//                    Trạng thái: Đã gửi yêu cầu
//                    """,
//                    username, hoTen, gmail, sdt, soNguoi, danhSachKhach,
//                    selectedRoom.getRoomId(), selectedRoom.getDescription(),
//                    selectedRoom.getType(), selectedRoom.getPrice(),
//                    selectedRoom.getCheckIn(), selectedRoom.getCheckOut(), thoiGianTao);
//
//            JTextArea textArea = new JTextArea(info);
//            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            textArea.setEditable(false);
//            textArea.setMargin(new Insets(10, 10, 10, 10));
//            textArea.setLineWrap(true);
//            textArea.setWrapStyleWord(true);
//
//            JScrollPane scrollPane = new JScrollPane(textArea);
//            scrollPane.setPreferredSize(new Dimension(500, 400));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.add(scrollPane, BorderLayout.CENTER);
//
//            Object[] options = {"Quay lại", "Xác nhận"};
//
//            int result = JOptionPane.showOptionDialog(
//                    mainFrame,
//                    content,
//                    "Xác nhận đặt phòng",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.PLAIN_MESSAGE,
//                    null,
//                    options,
//                    options[1]
//            );
//
//            if (result == 1) {
//                RequestService.createRequest(
//                        requestId, username, hoTen, gmail, sdt,
//                        selectedRoom.getRoomId(),
//                        selectedRoom.getCheckIn(), selectedRoom.getCheckOut(),
//                        selectedRoom.getPrice(), danhSach
//                );
//
//                NotificationService.createNotification(
//                        "BK00000001", requestId, username,
//                        "Yêu cầu duyệt", "Đã gửi"
//                );
//
//                JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                JPanel homePanel = CustomerDashboardView.createUserHomePanel();
//                mainFrame.setCustomerDynamicContent(homePanel);
//                mainFrame.setCustomerSelectedMenu("Trang chủ");
//            }
//        });
//
//        btnPanel.add(btnQuayLai);
//        btnPanel.add(btnXacNhan);
//        return btnPanel;
//    }
//
//    private static String generateRequestId() {
//        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
//    }
//}

package org.example.view;

import org.example.entity.Person;
import org.example.entity.SelectedRoomInfo;
import org.example.service.NotificationService;
import org.example.service.RequestService;
import org.example.service.UserInfoService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class CustomerBookingView {
    private static JTextField[] hoTenFields = new JTextField[3];
    private static JComboBox<String>[] loaiCombos = new JComboBox[3];
    private static JTextField[] maTextFields = new JTextField[3];
    private static JComboBox<String>[] maComboFields = new JComboBox[3];
    private static CardLayout[] maCardLayouts = new CardLayout[3];
    private static JPanel[] maPanels = new JPanel[3];

    private static JComboBox<Integer> cbSoNguoi;
    private static JComboBox<String> cbLoaiChung;
    private static JComboBox<String> cbMaChung;
    private static JTextField tfMaChung;
    private static JPanel panelMaChung;
    private static CardLayout cardLayoutMaChung;

    // Regex cho mã định danh (12 chữ số) và hộ chiếu (8 ký tự chữ hoặc số)
    private static final Pattern MA_DINH_DANH_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern HO_CHIEU_PATTERN = Pattern.compile("[A-Za-z0-9]{8}");

    public static JPanel createBookingPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(950, 800));
        panel.setBackground(Color.WHITE);

        JPanel formPanel = createFormPanel(mainFrame, username, selectedRoom);
        JPanel buttonPanel = createButtonPanel(mainFrame, username, selectedRoom);

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);

        return panel;
    }

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

        // Loại giấy tờ đại diện
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Loại giấy tờ:"), gbc);

        gbc.gridx = 1;
        cbLoaiChung = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
        formPanel.add(cbLoaiChung, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Mã:"), gbc);

        gbc.gridx = 3;
        cardLayoutMaChung = new CardLayout();
        panelMaChung = new JPanel(cardLayoutMaChung);

        Dimension fixedSize = new Dimension(150, 25);
        cbMaChung = new JComboBox<>(new String[]{"Không xác định", "Dưới 14 tuổi"});
        cbMaChung.setPreferredSize(fixedSize);
        tfMaChung = new JTextField();
        tfMaChung.setPreferredSize(fixedSize);

        panelMaChung.add(cbMaChung, "combo");
        panelMaChung.add(tfMaChung, "text");
        formPanel.add(panelMaChung, gbc);

        Runnable updateMaChungField = () -> {
            String selected = (String) cbLoaiChung.getSelectedItem();
            if ("Không có".equals(selected)) {
                cbMaChung.setSelectedItem("Không xác định");
                cardLayoutMaChung.show(panelMaChung, "combo");
            } else {
                tfMaChung.setText("");
                cardLayoutMaChung.show(panelMaChung, "text");
            }
        };

        updateMaChungField.run();
        cbLoaiChung.addActionListener(e -> updateMaChungField.run());

        String[] loaiOptions = {"Không có", "Mã định danh", "Hộ chiếu"};
        String[] comboOptions = {"Không xác định", "Dưới 14 tuổi"};
        Dimension maFieldSize = new Dimension(150, 25);

        for (int i = 0; i < 3; i++) {
            int row = i * 2 + 2;

            gbc.gridy = row;
            gbc.gridx = 0;
            formPanel.add(new JLabel("Họ tên người " + (i + 1) + ":"), gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 3;
            hoTenFields[i] = new JTextField();
            formPanel.add(hoTenFields[i], gbc);
            gbc.gridwidth = 1;

            gbc.gridy = row + 1;
            gbc.gridx = 0;
            formPanel.add(new JLabel("Loại giấy tờ:"), gbc);

            loaiCombos[i] = new JComboBox<>(loaiOptions);
            gbc.gridx = 1;
            formPanel.add(loaiCombos[i], gbc);

            gbc.gridx = 2;
            formPanel.add(new JLabel("Mã:"), gbc);

            gbc.gridx = 3;
            maCardLayouts[i] = new CardLayout();
            maPanels[i] = new JPanel(maCardLayouts[i]);

            maComboFields[i] = new JComboBox<>(comboOptions);
            maComboFields[i].setPreferredSize(maFieldSize);
            maTextFields[i] = new JTextField();
            maTextFields[i].setPreferredSize(maFieldSize);

            maPanels[i].add(maComboFields[i], "combo");
            maPanels[i].add(maTextFields[i], "text");
            formPanel.add(maPanels[i], gbc);

            int index = i;
            loaiCombos[i].addActionListener(e -> {
                String selected = (String) loaiCombos[index].getSelectedItem();
                if ("Không có".equals(selected)) {
                    maComboFields[index].setSelectedItem("Không xác định");
                    maCardLayouts[index].show(maPanels[index], "combo");
                } else {
                    maTextFields[index].setText("");
                    maCardLayouts[index].show(maPanels[index], "text");
                }
            });

            loaiCombos[i].setSelectedIndex(0);
        }

        cbSoNguoi.addActionListener(e -> {
            int selected = (int) cbSoNguoi.getSelectedItem();
            for (int i = 0; i < 3; i++) {
                boolean enable = i < selected;
                hoTenFields[i].setEnabled(enable);
                loaiCombos[i].setEnabled(enable);
                maComboFields[i].setEnabled(enable);
                maTextFields[i].setEnabled(enable);

                if (!enable) {
                    hoTenFields[i].setText("");
                    maTextFields[i].setText("");
                    maComboFields[i].setSelectedIndex(0);
                }

                String selectedLoai = (String) loaiCombos[i].getSelectedItem();
                maCardLayouts[i].show(maPanels[i], "Không có".equals(selectedLoai) ? "combo" : "text");
            }
        });

        cbSoNguoi.setSelectedIndex(0);
        return formPanel;
    }

    // Hàm kiểm tra định dạng mã giấy tờ
    private static boolean isValidMaGiayTo(String loai, String ma) {
        if ("Không có".equals(loai)) {
            return ma.equals("Không xác định") || ma.equals("Dưới 14 tuổi");
        } else if ("Mã định danh".equals(loai)) {
            return MA_DINH_DANH_PATTERN.matcher(ma).matches();
        } else if ("Hộ chiếu".equals(loai)) {
            return HO_CHIEU_PATTERN.matcher(ma).matches();
        }
        return false;
    }

    // Hàm kiểm tra thông tin đầu vào và trùng lặp mã
    private static String validateInput(MainFrameView mainFrame, int soNguoi, String hoTen, String loaiChung, String cccdNguoiDaiDien) {
        // Kiểm tra họ tên người đại diện
        if (hoTen.isEmpty()) {
            return "Họ tên người đại diện không được để trống!";
        }

        // Kiểm tra mã giấy tờ người đại diện
        if (!"Không có".equals(loaiChung) && cccdNguoiDaiDien.isEmpty()) {
            return "Mã giấy tờ người đại diện không được để trống!";
        }
        if (!isValidMaGiayTo(loaiChung, cccdNguoiDaiDien)) {
            return "Mã giấy tờ người đại diện không đúng định dạng!\n" +
                    "Mã định danh: 12 chữ số\nHộ chiếu: 8 ký tự chữ hoặc số";
        }
        // Kiểm tra độ tuổi người đại diện dựa trên mã
        if ("Mã định danh".equals(loaiChung) && "Dưới 14 tuổi".equals(cccdNguoiDaiDien)) {
            return "Người đại diện dưới 14 tuổi không được phép sử dụng Mã định danh!";
        }

        // Kiểm tra thông tin các khách và trùng lặp mã
        int validGuests = 0;
        Set<String> maDinhDanhSet = new HashSet<>();
        Set<String> hoChieuSet = new HashSet<>();

        // Thêm mã của người đại diện vào tập hợp kiểm tra
        if ("Mã định danh".equals(loaiChung)) {
            maDinhDanhSet.add(cccdNguoiDaiDien);
        } else if ("Hộ chiếu".equals(loaiChung)) {
            hoChieuSet.add(cccdNguoiDaiDien);
        }

        for (int i = 0; i < soNguoi; i++) {
            String ten = hoTenFields[i].getText().trim();
            String loai = (String) loaiCombos[i].getSelectedItem();
            String ma = "Không có".equals(loai) ? (String) maComboFields[i].getSelectedItem() : maTextFields[i].getText().trim();

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
                // Kiểm tra trùng mã
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

        // Kiểm tra số người hợp lệ
        if (validGuests < soNguoi) {
            return "Số lượng khách hợp lệ (" + validGuests + ") không khớp với số người đã chọn (" + soNguoi + ")!";
        }

        return null; // Không có lỗi
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
            String hoTen = UserInfoService.getFullName("userinfos.xml", username);
            String gmail = UserInfoService.getEmail("userinfos.xml", username);
            String sdt = UserInfoService.getPhone("userinfos.xml", username);

            // Lấy mã giấy tờ đại diện
            String cccdNguoiDaiDien;
            String loaiChung = (String) cbLoaiChung.getSelectedItem();
            if ("Không có".equals(loaiChung)) {
                cccdNguoiDaiDien = (String) cbMaChung.getSelectedItem();
            } else {
                cccdNguoiDaiDien = tfMaChung.getText().trim();
            }

            // Kiểm tra dữ liệu đầu vào
            String errorMessage = validateInput(mainFrame, soNguoi, hoTen, loaiChung, cccdNguoiDaiDien);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(mainFrame, errorMessage, "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder danhSachKhach = new StringBuilder();
            danhSachKhach.append("- ").append(hoTen).append(" [").append(loaiChung)
                    .append(" : ").append(cccdNguoiDaiDien).append("]\n");

            List<Person> danhSach = new ArrayList<>();
            danhSach.add(new Person(hoTen, loaiChung, cccdNguoiDaiDien));

            for (int i = 0; i < soNguoi; i++) {
                String ten = hoTenFields[i].getText().trim();
                String loai = (String) loaiCombos[i].getSelectedItem();
                String ma = "Không có".equals(loai) ? (String) maComboFields[i].getSelectedItem() : maTextFields[i].getText().trim();

                if (!ten.isEmpty()) {
                    danhSachKhach.append("- ").append(ten).append(" [").append(loai).append(" : ").append(ma).append("]\n");
                    danhSach.add(new Person(ten, loai, ma));
                }
            }

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
                    username, hoTen, gmail, sdt, soNguoi, danhSachKhach,
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
                    mainFrame,
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
                        requestId, username, hoTen, gmail, sdt,
                        selectedRoom.getRoomId(),
                        selectedRoom.getCheckIn(), selectedRoom.getCheckOut(),
                        selectedRoom.getPrice(), danhSach
                );

                NotificationService.createNotification(
                        "BK00000001", requestId, username,
                        "Yêu cầu duyệt", "Đã gửi"
                );

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

    private static String generateRequestId() {
        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}