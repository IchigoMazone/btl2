

package org.example.view;

import org.example.entity.Person;
import org.example.entity.SelectedRoomInfo;
import org.example.service.BookingService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
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

public class AdminBookingView {
    private static JTextField tfTaiKhoan, tfSdt, tfGmail;
    private static JTextField[] hoTenFields = new JTextField[4];
    private static JComboBox<String>[] loaiCombos = new JComboBox[4];
    private static JTextField[] maTextFields = new JTextField[4];
    private static JComboBox<String>[] maComboFields = new JComboBox[4];
    private static CardLayout[] maCardLayouts = new CardLayout[4];
    private static JPanel[] maPanels = new JPanel[4];
    private static JComboBox<Integer> cbSoNguoi;
    private static JComboBox<String> cbLoaiChung;
    private static JComboBox<String> cbMaChung;
    private static JTextField tfMaChung;
    private static JPanel panelMaChung;
    private static CardLayout cardLayoutMaChung;

    private static final Pattern MA_DINH_DANH_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern HO_CHIEU_PATTERN = Pattern.compile("[A-Za-z0-9]{8}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");

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

        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Số người:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        Integer[] soNguoiOptions = {1, 2, 3, 4};
        cbSoNguoi = new JComboBox<>(soNguoiOptions);
        formPanel.add(cbSoNguoi, gbc);
        gbc.gridwidth = 1;

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

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        tfSdt = new JTextField();
        formPanel.add(tfSdt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Gmail:"), gbc);
        gbc.gridx = 1;
        tfGmail = new JTextField();
        formPanel.add(tfGmail, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Họ tên người đại diện:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        hoTenFields[0] = new JTextField();
        formPanel.add(hoTenFields[0], gbc);
        gbc.gridwidth = 1;

        gbc.gridy++;
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

        for (int i = 1; i < 4; i++) {
            int row = gbc.gridy + 1 + (i - 1) * 2;

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
            gbc.gridx = 1;
            loaiCombos[i] = new JComboBox<>(loaiOptions);
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
            for (int i = 1; i < 4; i++) {
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

    private static String validateInput(MainFrameView mainFrame, int soNguoi, String username, String sdt, String gmail, String hoTenDaiDien, String loaiDaiDien, String maDaiDien) {

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

    private static JPanel createButtonPanel(MainFrameView mainFrame, SelectedRoomInfo selectedRoom) {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIManager.getColor("Panel.background"));

        JButton btnXacNhan = new JButton("Xác nhận");
        JButton btnQuayLai = new JButton("Quay lại");

        btnQuayLai.addActionListener(e -> {
            JPanel searchPanel = AdminSearchView.createSearchPanel(mainFrame);
            mainFrame.setAdminDynamicContent(searchPanel);
        });

        btnXacNhan.addActionListener(e -> {
            int soNguoi = (int) cbSoNguoi.getSelectedItem();
            String username = tfTaiKhoan.getText().trim();
            String sdt = tfSdt.getText().trim();
            String gmail = tfGmail.getText().trim();
            String hoTenDaiDien = hoTenFields[0].getText().trim();
            String loaiDaiDien = (String) cbLoaiChung.getSelectedItem();
            String maDaiDien = "Không có".equals(loaiDaiDien) ? (String) cbMaChung.getSelectedItem() : tfMaChung.getText().trim();

            String errorMessage = validateInput(mainFrame, soNguoi, username, sdt, gmail, hoTenDaiDien, loaiDaiDien, maDaiDien);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(mainFrame, errorMessage, "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder danhSachKhach = new StringBuilder();
            danhSachKhach.append("- ").append(hoTenDaiDien)
                    .append(" [").append(loaiDaiDien).append(": ").append(maDaiDien).append("]\n");

            List<Person> danhSach = new ArrayList<>();
            danhSach.add(new Person(hoTenDaiDien, loaiDaiDien, maDaiDien));

            for (int i = 1; i < soNguoi; i++) {
                String ten = hoTenFields[i].getText().trim();
                String loai = (String) loaiCombos[i].getSelectedItem();
                String ma = "Không có".equals(loai) ? (String) maComboFields[i].getSelectedItem() : maTextFields[i].getText().trim();

                if (!ten.isEmpty()) {
                    danhSachKhach.append("- ").append(ten)
                            .append(" [").append(loai).append(": ").append(ma).append("]\n");
                    danhSach.add(new Person(ten, loai, ma));
                }
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
                    mainFrame,
                    content,
                    "Xác nhận đặt phòng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[]{"Quay lại", "Xác nhận"},
                    "Xác nhận");

            if (confirm == 1) {
                String bookingId = "BK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%05d", new Random().nextInt(100000));

                BookingService.createBooking(
                        "bookings.xml",
                        bookingId,
                        "Đơn đặt trực tiếp",
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

                JOptionPane.showMessageDialog(mainFrame, "Gửi yêu cầu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.setAdminDynamicContent(AdminDashboardView.createDashboardPanel());
                mainFrame.setAdminSelectedMenu("Trang chủ");
            }
        });

        btnPanel.add(btnQuayLai);
        btnPanel.add(btnXacNhan);
        return btnPanel;
    }
}

