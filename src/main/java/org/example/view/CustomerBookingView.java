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
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//import java.util.regex.Pattern;
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
//
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
//    private static boolean isValidMaGiayTo(String loai, String ma) {
//        if ("Không có".equals(loai)) {
//            return ma.equals("Không xác định") || ma.equals("Dưới 14 tuổi");
//        } else if ("Mã định danh".equals(loai)) {
//            return MA_DINH_DANH_PATTERN.matcher(ma).matches();
//        } else if ("Hộ chiếu".equals(loai)) {
//            return HO_CHIEU_PATTERN.matcher(ma).matches();
//        }
//        return false;
//    }
//
//    private static String validateInput(MainFrameView mainFrame, int soNguoi, String hoTen, String loaiChung, String cccdNguoiDaiDien) {
//
//        if (hoTen.isEmpty()) {
//            return "Họ tên người đại diện không được để trống!";
//        }
//
//        if (!"Không có".equals(loaiChung) && cccdNguoiDaiDien.isEmpty()) {
//            return "Mã giấy tờ người đại diện không được để trống!";
//        }
//        if (!isValidMaGiayTo(loaiChung, cccdNguoiDaiDien)) {
//            return "Mã giấy tờ người đại diện không đúng định dạng!\n" +
//                    "Mã định danh: 12 chữ số\nHộ chiếu: 8 ký tự chữ hoặc số";
//        }
//
//        if ("Mã định danh".equals(loaiChung) && "Dưới 14 tuổi".equals(cccdNguoiDaiDien)) {
//            return "Người đại diện dưới 14 tuổi không được phép sử dụng Mã định danh!";
//        }
//
//        int validGuests = 0;
//        Set<String> maDinhDanhSet = new HashSet<>();
//        Set<String> hoChieuSet = new HashSet<>();
//
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
//            if (!ten.isEmpty() && "Mã định danh".equals(loai) && "Dưới 14 tuổi".equals(ma)) {
//                return "Người " + (i + 1) + " dưới 14 tuổi không được phép sử dụng Mã định danh!";
//            }
//            if (!ten.isEmpty()) {
//                validGuests++;
//
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
//        if (validGuests < soNguoi) {
//            return "Số lượng khách hợp lệ (" + validGuests + ") không khớp với số người đã chọn (" + soNguoi + ")!";
//        }
//
//        return null;
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
//            String cccdNguoiDaiDien;
//            String loaiChung = (String) cbLoaiChung.getSelectedItem();
//            if ("Không có".equals(loaiChung)) {
//                cccdNguoiDaiDien = (String) cbMaChung.getSelectedItem();
//            } else {
//                cccdNguoiDaiDien = tfMaChung.getText().trim();
//            }
//
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

import org.example.controller.CustomerBookingController;
import org.example.entity.SelectedRoomInfo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CustomerBookingView {
    private JPanel panel;
    private JTextField[] hoTenFields = new JTextField[3];
    private JComboBox<String>[] loaiCombos = new JComboBox[3];
    private JTextField[] maTextFields = new JTextField[3];
    private JComboBox<String>[] maComboFields = new JComboBox[3];
    private CardLayout[] maCardLayouts = new CardLayout[3];
    private JPanel[] maPanels = new JPanel[3];
    private JComboBox<Integer> cbSoNguoi;
    private JComboBox<String> cbLoaiChung;
    private JComboBox<String> cbMaChung;
    private JTextField tfMaChung;
    private JPanel panelMaChung;
    private CardLayout cardLayoutMaChung;
    private JButton btnXacNhan;
    private JButton btnQuayLai;

    private CustomerBookingView(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
        initializeComponents(mainFrame, username, selectedRoom);
    }

    public static JPanel createBookingPanel(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
        CustomerBookingView view = new CustomerBookingView(mainFrame, username, selectedRoom);
        CustomerBookingController controller = new CustomerBookingController(view, mainFrame, username, selectedRoom);
        return view.getPanel();
    }

    private void initializeComponents(MainFrameView mainFrame, String username, SelectedRoomInfo selectedRoom) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(950, 800));
        panel.setBackground(Color.WHITE);

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
    }

    private JPanel createFormPanel() {
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

        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Số người:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        Integer[] soNguoiOptions = {0, 1, 2, 3};
        cbSoNguoi = new JComboBox<>(soNguoiOptions);
        formPanel.add(cbSoNguoi, gbc);
        gbc.gridwidth = 1;

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

            loaiCombos[i].setSelectedIndex(0);
        }

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIManager.getColor("Panel.background"));

        btnXacNhan = new JButton("Xác nhận");
        btnQuayLai = new JButton("Quay lại");

        btnPanel.add(btnQuayLai);
        btnPanel.add(btnXacNhan);
        return btnPanel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JTextField[] getHoTenFields() {
        return hoTenFields;
    }

    public JComboBox<String>[] getLoaiCombos() {
        return loaiCombos;
    }

    public JTextField[] getMaTextFields() {
        return maTextFields;
    }

    public JComboBox<String>[] getMaComboFields() {
        return maComboFields;
    }

    public CardLayout[] getMaCardLayouts() {
        return maCardLayouts;
    }

    public JPanel[] getMaPanels() {
        return maPanels;
    }

    public JComboBox<Integer> getCbSoNguoi() {
        return cbSoNguoi;
    }

    public JComboBox<String> getCbLoaiChung() {
        return cbLoaiChung;
    }

    public JComboBox<String> getCbMaChung() {
        return cbMaChung;
    }

    public JTextField getTfMaChung() {
        return tfMaChung;
    }

    public JPanel getPanelMaChung() {
        return panelMaChung;
    }

    public CardLayout getCardLayoutMaChung() {
        return cardLayoutMaChung;
    }

    public JButton getBtnXacNhan() {
        return btnXacNhan;
    }

    public JButton getBtnQuayLai() {
        return btnQuayLai;
    }
}