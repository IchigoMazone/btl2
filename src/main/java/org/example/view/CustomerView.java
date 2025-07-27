//
//package org.example.view;
//
//import org.example.entity.Booking;
//import org.example.entity.BookingXML;
//import org.example.entity.Person;
//import org.example.service.BookingService;
//import org.example.utils.FileUtils;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumn;
//import java.awt.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.AbstractMap;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CustomerView {
//
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//    private static DefaultTableModel tableModel;
//    private static JTable table;
//    private static BookingService bookingService;
//
//    public static void setBookingService(BookingService service) {
//        bookingService = service;
//    }
//
//    public static JPanel createCustomerSearchPanel() {
//        JPanel panel = new JPanel(new BorderLayout(10, 10));
//        panel.setBackground(Color.WHITE);
//
//        if (bookingService == null) {
//            bookingService = new BookingService();
//        }
//
//        // Search panel
//        JPanel searchPanel = new JPanel(new GridBagLayout());
//        searchPanel.setBackground(UIManager.getColor("Panel.background"));
//        searchPanel.setBorder(BorderFactory.createTitledBorder(
//                BorderFactory.createEtchedBorder(),
//                "Tìm kiếm khách hàng",
//                TitledBorder.LEFT, TitledBorder.TOP));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 20, 10);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        JComboBox<String> cbTieuChi = new JComboBox<>(new String[]{
//                "Tài khoản", "Số điện thoại", "Gmail", "Họ tên", "Mã định danh", "Mã phòng"
//        });
//        JTextField tfTuKhoa = new JTextField(20);
//
//        gbc.gridx = 0; gbc.gridy = 0;
//        searchPanel.add(new JLabel("Tìm theo:"), gbc);
//        gbc.gridx = 1;
//        searchPanel.add(cbTieuChi, gbc);
//        gbc.gridx = 2;
//        searchPanel.add(new JLabel("Từ khóa:"), gbc);
//        gbc.gridx = 3;
//        searchPanel.add(tfTuKhoa, gbc);
//
//        // Table setup
//        String[] columns = {
//                "Tài khoản", "SĐT đại diện", "Email đại diện", "Họ tên", "Giấy tờ",
//                "Mã giấy tờ", "Phòng", "Check-in", "Check-out", "Booking ID"
//        };
//        tableModel = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int col) {
//                return false;
//            }
//        };
//
//        table = new JTable(tableModel);
//        table.setRowHeight(30);
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//
//        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
//        center.setHorizontalAlignment(JLabel.CENTER);
//        for (int i = 0; i < table.getColumnCount() - 1; i++) {
//            table.getColumnModel().getColumn(i).setCellRenderer(center);
//        }
//
//        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
//                .setHorizontalAlignment(SwingConstants.CENTER);
//
//        int[] widths = {150, 150, 250, 250, 150, 150, 100, 150, 150, 0};
//        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
//            TableColumn column = table.getColumnModel().getColumn(i);
//            column.setPreferredWidth(widths[i]);
//            if (i == 9) {
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
//            }
//        }
//
//        JScrollPane scrollPane = new JScrollPane(table,
//                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng đang lưu trú"));
//
//        loadPersonData(LocalDateTime.now());
//
//        // Button panel
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnThem = new JButton("Thêm");
//        JButton btnXoa = new JButton("Xóa");
//        JButton btnSua = new JButton("Sửa");
//        JButton btnTimKiem = new JButton("Tìm kiếm");
//
//        styleButton(btnThem, new Color(0, 153, 76));
//        styleButton(btnXoa, new Color(200, 55, 60));
//        styleButton(btnSua, new Color(120, 120, 120));
//        styleButton(btnTimKiem, new Color(0, 153, 76));
//
//        btnPanel.add(btnThem);
//        btnPanel.add(btnXoa);
//        btnPanel.add(btnSua);
//        btnPanel.add(btnTimKiem);
//
//        // Search button action
//        btnTimKiem.addActionListener(e -> {
//            String keyword = tfTuKhoa.getText().trim().toLowerCase();
//            String searchType = (String) cbTieuChi.getSelectedItem();
//            LocalDateTime currentTime = LocalDateTime.now();
//
//            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
//            if (bookingXML == null || bookingXML.getBookings() == null) {
//                JOptionPane.showMessageDialog(panel, "Không thể đọc dữ liệu khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            List<Booking> activeBookings = bookingXML.getBookings().stream()
//                    .filter(b -> !b.getCheckIn().isAfter(currentTime) &&
//                            b.getCheckOut().isAfter(currentTime) &&
//                            "Check-in".equalsIgnoreCase(b.getStatus()))
//                    .collect(Collectors.toList());
//
//
//            tableModel.setRowCount(0);
//            for (Booking booking : activeBookings) {
//                List<Person> filteredPersons = booking.getPersons().stream()
//                        .filter(person -> {
//                            if (!keyword.isEmpty()) {
//                                switch (searchType) {
//                                    case "Tài khoản":
//                                        return booking.getUserName() != null &&
//                                                booking.getUserName().toLowerCase().contains(keyword);
//                                    case "Số điện thoại":
//                                        return booking.getPhone().contains(keyword);
//                                    case "Gmail":
//                                        return booking.getEmail().toLowerCase().contains(keyword);
//                                    case "Họ tên":
//                                        return person.getFullName().toLowerCase().contains(keyword);
//                                    case "Mã định danh":
//                                        return person.getDocumentCode().toLowerCase().contains(keyword);
//                                    case "Mã phòng":
//                                        return booking.getRoomId().toLowerCase().contains(keyword);
//                                }
//                            }
//                            return true;
//                        }).collect(Collectors.toList());
//
//                for (Person person : filteredPersons) {
//                    tableModel.addRow(new Object[]{
//                            booking.getUserName() != null ? booking.getUserName() : "Không có",
//                            booking.getPhone(),
//                            booking.getEmail(),
//                            person.getFullName(),
//                            person.getDocumentType() != null ? person.getDocumentType() : "Không có",
//                            person.getDocumentCode() != null ? person.getDocumentCode() : "Không có",
//                            booking.getRoomId(),
//                            booking.getCheckIn().format(DATE_FORMATTER),
//                            booking.getCheckOut().format(DATE_FORMATTER),
//                            booking.getBookingId()
//                    });
//                }
//            }
//        });
//
//        // Add button action
//        btnThem.addActionListener(e -> showCustomerDialog(panel, tableModel, null));
//
//        // Delete button action
//        btnXoa.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            String documentCode = (String) tableModel.getValueAt(selectedRow, 5);
//            String fullName = (String) tableModel.getValueAt(selectedRow, 3);
//            String bookingId = (String) tableModel.getValueAt(selectedRow, 9);
//
//            int confirm = JOptionPane.showConfirmDialog(panel,
//                    "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                try {
//                    bookingService.deletePerson(bookingId, documentCode, fullName);
//                    loadPersonData(LocalDateTime.now());
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(panel, "Lỗi khi xóa khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//        // Edit button action
//        btnSua.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            String documentCode = (String) tableModel.getValueAt(selectedRow, 5);
//            String fullName = (String) tableModel.getValueAt(selectedRow, 3);
//            String bookingId = (String) tableModel.getValueAt(selectedRow, 9);
//
//            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
//            if (bookingXML == null || bookingXML.getBookings() == null) {
//                JOptionPane.showMessageDialog(panel, "Không thể đọc dữ liệu khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            Booking booking = bookingXML.getBookings().stream()
//                    .filter(b -> b.getBookingId().equals(bookingId) &&
//                            !b.getCheckIn().isAfter(LocalDateTime.now()) &&
//                            b.getCheckOut().isAfter(LocalDateTime.now()))
//                    .findFirst()
//                    .orElse(null);
//
//            if (booking != null) {
//                Person person = booking.getPersons().stream()
//                        .filter(p -> p.getDocumentCode().equals(documentCode) && p.getFullName().equals(fullName))
//                        .findFirst()
//                        .orElse(null);
//                if (person != null) {
//                    showCustomerDialog(panel, tableModel, new AbstractMap.SimpleEntry<>(selectedRow,
//                            new AbstractMap.SimpleEntry<>(bookingId, person)));
//                } else {
//                    JOptionPane.showMessageDialog(panel, "Không tìm thấy khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(panel, "Booking không còn hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//
//        panel.add(searchPanel, BorderLayout.NORTH);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(btnPanel, BorderLayout.SOUTH);
//
//        return panel;
//    }
//
//    private static void loadPersonData(LocalDateTime currentTime) {
//        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
//        if (bookingXML == null || bookingXML.getBookings() == null) {
//            bookingXML = new BookingXML();
//            bookingXML.setBookings(new ArrayList<>());
//        }
//
//        List<Booking> activeBookings = bookingXML.getBookings().stream()
//                .filter(b -> !b.getCheckIn().isAfter(currentTime) && b.getCheckOut().isAfter(currentTime))
//                .collect(Collectors.toList());
//
//        tableModel.setRowCount(0);
//        for (Booking booking : activeBookings) {
//            if ("Check-in".equalsIgnoreCase(booking.getStatus())) {
//                for (Person person : booking.getPersons()) {
//                    tableModel.addRow(new Object[]{
//                            booking.getUserName() != null ? booking.getUserName() : "Không có",
//                            booking.getPhone(),
//                            booking.getEmail(),
//                            person.getFullName(),
//                            person.getDocumentType() != null ? person.getDocumentType() : "Không có",
//                            person.getDocumentCode() != null ? person.getDocumentCode() : "Không có",
//                            booking.getRoomId(),
//                            booking.getCheckIn().format(DATE_FORMATTER),
//                            booking.getCheckOut().format(DATE_FORMATTER),
//                            booking.getBookingId()
//                    });
//                }
//            }
//        }
//    }
//
//    private static void showCustomerDialog(JPanel parent, DefaultTableModel model, AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<String, Person>> editData) {
//        JTextField tfHoTen = new JTextField(22);
//        JTextField tfDinhDanh = new JTextField(22);
//        JComboBox<String> cbDocType = new JComboBox<>(new String[]{"CCCD", "Hộ chiếu", "Mã định danh"});
//
//        String bookingId = null;
//        if (editData != null) {
//            Person person = editData.getValue().getValue();
//            bookingId = editData.getValue().getKey();
//            tfHoTen.setText(person.getFullName());
//            tfDinhDanh.setText(person.getDocumentCode());
//            cbDocType.setSelectedItem(person.getDocumentType() != null ? person.getDocumentType() : "CCCD");
//        }
//
//        JPanel inputPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 5, 10);
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.gridx = 0; gbc.gridy = 0;
//
//        inputPanel.add(new JLabel("Họ tên:"), gbc); gbc.gridx = 1;
//        inputPanel.add(tfHoTen, gbc); gbc.gridy++; gbc.gridx = 0;
//
//        inputPanel.add(new JLabel("Mã định danh:"), gbc); gbc.gridx = 1;
//        inputPanel.add(tfDinhDanh, gbc); gbc.gridy++; gbc.gridx = 0;
//
//        inputPanel.add(new JLabel("Loại giấy tờ:"), gbc); gbc.gridx = 1;
//        inputPanel.add(cbDocType, gbc);
//
//        JComboBox<String> cbBookingId = null;
//        if (editData == null) {
//            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
//            if (bookingXML == null || bookingXML.getBookings() == null) {
//                bookingXML = new BookingXML();
//                bookingXML.setBookings(new ArrayList<>());
//            }
//            List<String> bookingIds = bookingXML.getBookings().stream()
//                    .filter(b -> !b.getCheckIn().isAfter(LocalDateTime.now()) &&
//                            b.getCheckOut().isAfter(LocalDateTime.now()) &&
//                            "Check-in".equalsIgnoreCase(b.getStatus()))
//                    .map(Booking::getBookingId)
//                    .collect(Collectors.toList());
//
//            if (bookingIds.isEmpty()) {
//                JOptionPane.showMessageDialog(parent, "Không có booking đang hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            cbBookingId = new JComboBox<>(bookingIds.toArray(new String[0]));
//            gbc.gridy++; gbc.gridx = 0;
//            inputPanel.add(new JLabel("Mã booking:"), gbc); gbc.gridx = 1;
//            inputPanel.add(cbBookingId, gbc);
//        }
//
//        int result = JOptionPane.showConfirmDialog(parent, inputPanel,
//                editData == null ? "Thêm khách hàng" : "Sửa thông tin",
//                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//
//        if (result == JOptionPane.OK_OPTION) {
//            String fullName = tfHoTen.getText().trim();
//            String documentCode = tfDinhDanh.getText().trim();
//            String documentType = (String) cbDocType.getSelectedItem();
//
//            if (fullName.isEmpty() || documentCode.isEmpty()) {
//                JOptionPane.showMessageDialog(parent, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            Person person = new Person();
//            person.setFullName(fullName);
//            person.setDocumentCode(documentCode);
//            person.setDocumentType(documentType);
//
//            try {
//                if (editData == null) {
//                    bookingService.addPerson((String) cbBookingId.getSelectedItem(), person);
//                } else {
//                    String oldDocumentCode = (String) model.getValueAt(editData.getKey(), 5);
//                    String oldFullName = (String) model.getValueAt(editData.getKey(), 3);
//                    bookingService.updatePerson(bookingId, oldDocumentCode, oldFullName, person);
//                }
//                loadPersonData(LocalDateTime.now());
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(parent, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private static void styleButton(JButton btn, Color bgColor) {
//        btn.setForeground(Color.WHITE);
//        btn.setFocusPainted(false);
//        btn.setBackground(bgColor);
//        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btn.setPreferredSize(new Dimension(110, 36));
//    }
//}




package org.example.view;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.entity.Person;
import org.example.service.BookingService;
import org.example.utils.FileUtils;
import java.awt.event.ItemEvent;
import javax.swing.*;
import java.util.Optional;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerView {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static DefaultTableModel tableModel;
    private static JTable table;
    private static BookingService bookingService;

    public static void setBookingService(BookingService service) {
        bookingService = service;
    }

    public static JPanel createCustomerSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        if (bookingService == null) {
            bookingService = new BookingService();
        }

        // Search panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(UIManager.getColor("Panel.background"));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Tìm kiếm khách hàng",
                TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<String> cbTieuChi = new JComboBox<>(new String[]{
                "Tài khoản", "Số điện thoại", "Gmail", "Họ tên", "Mã định danh", "Hộ chiếu", "Mã phòng"
        });
        JTextField tfTuKhoa = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Tìm theo:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(cbTieuChi, gbc);
        gbc.gridx = 2;
        searchPanel.add(new JLabel("Từ khóa:"), gbc);
        gbc.gridx = 3;
        searchPanel.add(tfTuKhoa, gbc);

        // Table setup
        String[] columns = {
                "Tài khoản", "SĐT đại diện", "Email đại diện", "Họ tên", "Giấy tờ",
                "Mã giấy tờ", "Phòng", "Check-in", "Check-out", "Booking ID"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        int[] widths = {150, 150, 250, 250, 150, 150, 100, 150, 150, 0};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            if (i == 9) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng đang lưu trú"));

        loadPersonData(LocalDateTime.now());

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThem = new JButton("Thêm");
        JButton btnXoa = new JButton("Xóa");
        JButton btnSua = new JButton("Sửa");
        JButton btnTimKiem = new JButton("Tìm kiếm");

//        styleButton(btnThem, new Color(0, 153, 76));
//        styleButton(btnXoa, new Color(200, 55, 60));
//        styleButton(btnSua, new Color(120, 120, 120));
//        styleButton(btnTimKiem, new Color(0, 153, 76));

        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
        btnPanel.add(btnSua);
        btnPanel.add(btnTimKiem);

        // Search button action
//        btnTimKiem.addActionListener(e -> {
//            String keyword = tfTuKhoa.getText().trim().toLowerCase();
//            String searchType = (String) cbTieuChi.getSelectedItem();
//            LocalDateTime currentTime = LocalDateTime.now();
//
//            if (keyword.isEmpty()) {
//                JOptionPane.showMessageDialog(panel,
//                        "Vui lòng nhập từ khóa tìm kiếm!",
//                        "Lỗi",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // Optional: Validate keyword format based on search type
//            if (searchType.equals("Số điện thoại") && !keyword.matches("0\\d{9}")) {
//                JOptionPane.showMessageDialog(panel,
//                        "Số điện thoại phải chứa đúng 10 chữ số và bắt đầu bằng 0!",
//                        "Lỗi",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            } else if (searchType.equals("Gmail") && !keyword.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
//                JOptionPane.showMessageDialog(panel,
//                        "Email không đúng định dạng!",
//                        "Lỗi",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            } else if (searchType.equals("Mã định danh") && !keyword.matches("[A-Za-z0-9]+")) {
//                JOptionPane.showMessageDialog(panel,
//                        "Mã định danh chỉ chứa chữ cái và số!",
//                        "Lỗi",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
//            if (bookingXML == null || bookingXML.getBookings() == null) {
//                JOptionPane.showMessageDialog(panel, "Không thể đọc dữ liệu khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            List<Booking> activeBookings = bookingXML.getBookings().stream()
//                    .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
//                    .collect(Collectors.toList());
//
//
//            tableModel.setRowCount(0);
//            for (Booking booking : activeBookings) {
//                List<Person> filteredPersons = booking.getPersons().stream()
//                        .filter(person -> {
//                            if (!keyword.isEmpty()) {
//                                switch (searchType) {
//                                    case "Tài khoản":
//                                        return booking.getUserName() != null &&
//                                                booking.getUserName().toLowerCase().contains(keyword);
//                                    case "Số điện thoại":
//                                        return booking.getPhone().contains(keyword);
//                                    case "Gmail":
//                                        return booking.getEmail().toLowerCase().contains(keyword);
//                                    case "Họ tên":
//                                        return person.getFullName().toLowerCase().contains(keyword);
//                                    case "Mã định danh":
//                                        return person.getDocumentCode().toLowerCase().contains(keyword);
//                                    case "Mã phòng":
//                                        return booking.getRoomId().toLowerCase().contains(keyword);
//                                }
//                            }
//                            return true;
//                        }).collect(Collectors.toList());
//
//                for (Person person : filteredPersons) {
//                    tableModel.addRow(new Object[]{
//                            booking.getUserName() != null ? booking.getUserName() : "Không có",
//                            booking.getPhone(),
//                            booking.getEmail(),
//                            person.getFullName(),
//                            person.getDocumentType() != null ? person.getDocumentType() : "Không có",
//                            person.getDocumentCode() != null ? person.getDocumentCode() : "Không có",
//                            booking.getRoomId(),
//                            booking.getCheckIn().format(DATE_FORMATTER),
//                            booking.getCheckOut().format(DATE_FORMATTER),
//                            booking.getBookingId()
//                    });
//                }
//            }
//        });

        btnTimKiem.addActionListener(e -> {
            String keyword = tfTuKhoa.getText().trim().toLowerCase();
            String searchType = (String) cbTieuChi.getSelectedItem();
            LocalDateTime currentTime = LocalDateTime.now();

            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Vui lòng nhập từ khóa tìm kiếm!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate keyword format based on search type
            if (searchType.equals("Số điện thoại") && !keyword.matches("0\\d{9}")) {
                JOptionPane.showMessageDialog(panel,
                        "Số điện thoại phải chứa đúng 10 chữ số và bắt đầu bằng 0!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } else if (searchType.equals("Gmail") && !keyword.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(panel,
                        "Email không đúng định dạng!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } else if (searchType.equals("Mã định danh") && !keyword.matches("\\d{12}")) {
                JOptionPane.showMessageDialog(panel,
                        "Mã định danh phải chứa đúng 12 chữ số!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } else if (searchType.equals("Hộ chiếu") && !keyword.matches("[A-Za-z0-9]{8}")) {
                JOptionPane.showMessageDialog(panel,
                        "Hộ chiếu phải chứa đúng 8 chữ cái hoặc số!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } else if (searchType.equals("Mã phòng") && !keyword.matches("r\\d{3,4}")) {
                JOptionPane.showMessageDialog(panel,
                        "Mã phòng phải bắt đầu bằng 'R' và có 3 đến 4 chữ số!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Read booking data
            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
            if (bookingXML == null || bookingXML.getBookings() == null) {
                JOptionPane.showMessageDialog(panel,
                        "Không thể đọc dữ liệu khách hàng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Filter active bookings
            List<Booking> activeBookings = bookingXML.getBookings().stream()
                    .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                    .collect(Collectors.toList());

            // Clear table before adding new data
            tableModel.setRowCount(0);
            boolean hasResults = false;

            // Perform search
            for (Booking booking : activeBookings) {
                List<Person> filteredPersons = booking.getPersons().stream()
                        .filter(person -> {
                            switch (searchType) {
                                case "Tài khoản":
                                    return booking.getUserName() != null &&
                                            booking.getUserName().toLowerCase().contains(keyword);
                                case "Số điện thoại":
                                    return booking.getPhone().contains(keyword);
                                case "Gmail":
                                    return booking.getEmail().toLowerCase().contains(keyword);
                                case "Họ tên":
                                    return person.getFullName().toLowerCase().contains(keyword);
                                case "Mã định danh":
                                    return person.getDocumentCode().toLowerCase().contains(keyword);
                                case "Hộ chiếu":
                                    return person.getDocumentType().equals("Hộ chiếu") &&
                                            person.getDocumentCode().toLowerCase().contains(keyword);
                                case "Mã phòng":
                                    return booking.getRoomId().toLowerCase().contains(keyword);
                                default:
                                    return true;
                            }
                        }).collect(Collectors.toList());

                // Add filtered results to table
                for (Person person : filteredPersons) {
                    tableModel.addRow(new Object[]{
                            booking.getUserName() != null ? booking.getUserName() : "Không có",
                            booking.getPhone(),
                            booking.getEmail(),
                            person.getFullName(),
                            person.getDocumentType() != null ? person.getDocumentType() : "Không có",
                            person.getDocumentCode() != null ? person.getDocumentCode() : "Không có",
                            booking.getRoomId(),
                            booking.getCheckIn().format(DATE_FORMATTER),
                            booking.getCheckOut().format(DATE_FORMATTER),
                            booking.getBookingId()
                    });
                    hasResults = true;
                }
            }

            // Check if no results were found
            if (!hasResults) {
                JOptionPane.showMessageDialog(panel,
                        "Không tìm thấy khách hàng phù hợp với từ khóa!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // Add button action
        btnThem.addActionListener(e -> showCustomerDialog(panel, tableModel, null));

        // Delete button action
//        btnXoa.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            String documentCode = (String) tableModel.getValueAt(selectedRow, 5);
//            String fullName = (String) tableModel.getValueAt(selectedRow, 3);
//            String bookingId = (String) tableModel.getValueAt(selectedRow, 9);
//
//            int confirm = JOptionPane.showConfirmDialog(panel,
//                    "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                try {
//                    bookingService.deletePerson(bookingId, documentCode, fullName);
//                    loadPersonData(LocalDateTime.now());
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(panel, "Lỗi khi xóa khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    ex.printStackTrace();
//                }
//            }
//        });

        btnXoa.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // Lấy thông tin từ tableModel
//            String fullName = (String) tableModel.getValueAt(selectedRow, 3); // Cột fullName
//            String documentCode = (String) tableModel.getValueAt(selectedRow, 5); // Cột documentCode
//            String bookingId = (String) tableModel.getValueAt(selectedRow, 9); // Cột bookingId
//
//            // Hiển thị thông báo xác nhận với chỉ số dòng để phân biệt trùng lặp
//            int confirm = JOptionPane.showConfirmDialog(panel,
//                    "Bạn có chắc muốn xóa khách hàng " + fullName + " (Mã tài liệu: " + documentCode + ", Dòng: " + (selectedRow + 1) + ")?",
//                    "Xác nhận", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                try {
//                    // Xóa bản ghi từ XML dựa trên chỉ số dòng và bookingId
//                    bookingService.deletePersonByRow(selectedRow, bookingId);
//                    // Tải lại dữ liệu vào bảng
//                    loadPersonData(LocalDateTime.now());
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(panel, "Lỗi khi xóa khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    ex.printStackTrace();
//                }
//            }

            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy thông tin từ tableModel
            String fullName = (String) tableModel.getValueAt(selectedRow, 3); // Cột fullName
            String documentCode = (String) tableModel.getValueAt(selectedRow, 5); // Cột documentCode
            String bookingId = (String) tableModel.getValueAt(selectedRow, 9); // Cột bookingId

            // Hiển thị thông báo xác nhận với chỉ số dòng để phân biệt trùng lặp
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Bạn có chắc muốn xóa khách hàng " + fullName + " (Mã tài liệu: " + documentCode + ", Dòng: " + (selectedRow + 1) + ")?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Xóa bản ghi từ XML dựa trên chỉ số dòng và bookingId
                    bookingService.deletePersonByRow(selectedRow, bookingId);
                    // Tải lại dữ liệu vào bảng
                    loadPersonData(LocalDateTime.now());
                    JOptionPane.showMessageDialog(panel, "Xóa khách hàng thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    // Hiển thị thông báo lỗi nhưng không in stack trace
                    JOptionPane.showMessageDialog(panel, "Lỗi khi xóa khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


//        btnSua.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            String documentCode = (String) tableModel.getValueAt(selectedRow, 5);
//            String fullName = (String) tableModel.getValueAt(selectedRow, 3);
//            String bookingId = (String) tableModel.getValueAt(selectedRow, 9);
//
//            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
//            if (bookingXML == null || bookingXML.getBookings() == null) {
//                JOptionPane.showMessageDialog(panel, "Không thể đọc dữ liệu khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            Booking booking = bookingXML.getBookings().stream()
//                    .filter(b -> b.getBookingId().equals(bookingId))
//                    .findFirst()
//                    .orElse(null);
//
//            if (booking != null) {
//                Person person = booking.getPersons().stream()
//                        .filter(p -> p.getDocumentCode().equals(documentCode) && p.getFullName().equals(fullName))
//                        .findFirst()
//                        .orElse(null);
//
//                if (person != null) {
//                    // Tạo form sửa thông tin
//                    JPanel editPanel = new JPanel(new GridLayout(4, 2, 5, 5));
//                    JTextField tfHoTen = new JTextField(person.getFullName(), 15);
//                    JTextField tfMa = new JTextField(person.getDocumentCode(), 15);
//                    JComboBox<String> cbLoai = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
//                    JComboBox<String> cbPhu = new JComboBox<>(new String[]{"Không xác định", "Dưới 14 tuổi"});
//
//                    // Tạo panel sử dụng CardLayout để chứa cbPhu và tfMa
//                    JPanel cardPanel = new JPanel(new CardLayout());
//                    cardPanel.add(cbPhu, "cbPhu");
//                    cardPanel.add(tfMa, "tfMa");
//                    CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
//
//                    // Thiết lập trạng thái ban đầu
//                    if ("Không có".equals(person.getDocumentType())) {
//                        cardLayout.show(cardPanel, "cbPhu");
//                    } else {
//                        cardLayout.show(cardPanel, "tfMa");
//                    }
//
//                    cbLoai.addActionListener(ev -> {
//                        if ("Không có".equals(cbLoai.getSelectedItem())) {
//                            cardLayout.show(cardPanel, "cbPhu");
//                        } else {
//                            cardLayout.show(cardPanel, "tfMa");
//                            tfMa.setEditable(true);
//                            tfMa.setText("");
//                        }
//                    });
//
//                    editPanel.add(new JLabel("Họ tên:"));
//                    editPanel.add(tfHoTen);
//                    editPanel.add(new JLabel("Loại giấy tờ:"));
//                    editPanel.add(cbLoai);
//                    editPanel.add(new JLabel("Chi tiết loại/Mã giấy tờ:"));
//                    editPanel.add(cardPanel);
//
//                    boolean validInput = false;
//                    while (!validInput) {
//                        int result = JOptionPane.showConfirmDialog(panel, editPanel, "Sửa thông tin khách hàng",
//                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//
//                        if (result == JOptionPane.OK_OPTION) {
//                            String newName = tfHoTen.getText().trim();
//                            String newType = (String) cbLoai.getSelectedItem();
//                            String newCode = "Không có".equals(newType) ? (String) cbPhu.getSelectedItem() : tfMa.getText().trim();
//
//                            if (!validateInput(newType, newCode)) {
//                                JOptionPane.showMessageDialog(panel, "Thông tin không hợp lệ! Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                                continue; // Tiếp tục hiển thị bảng sửa
//                            }
//
//                            person.setFullName(newName);
//                            person.setDocumentType(newType);
//                            person.setDocumentCode(newCode);
//
//                            FileUtils.writeToFile("bookings.xml", bookingXML);
//                            tableModel.setValueAt(newName, selectedRow, 3);
//                            tableModel.setValueAt(newType, selectedRow, 4);
//                            tableModel.setValueAt(newCode, selectedRow, 5);
//
//                            JOptionPane.showMessageDialog(panel, "Sửa thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                            validInput = true;
//                        } else {
//                            break; // Thoát nếu người dùng chọn Cancel
//                        }
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(panel, "Không tìm thấy khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(panel, "Booking không còn hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        });

        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String documentCode = (String) tableModel.getValueAt(selectedRow, 5);
            String fullName = (String) tableModel.getValueAt(selectedRow, 3);
            String bookingId = (String) tableModel.getValueAt(selectedRow, 9);

            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
            if (bookingXML == null || bookingXML.getBookings() == null) {
                JOptionPane.showMessageDialog(panel, "Không thể đọc dữ liệu khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Booking booking = bookingXML.getBookings().stream()
                    .filter(b -> b.getBookingId().equals(bookingId))
                    .findFirst()
                    .orElse(null);

            if (booking != null) {
                Person person = booking.getPersons().stream()
                        .filter(p -> p.getDocumentCode().equals(documentCode) && p.getFullName().equals(fullName))
                        .findFirst()
                        .orElse(null);

                if (person != null) {
                    // Tạo form sửa thông tin
                    JPanel editPanel = new JPanel(new GridLayout(4, 2, 5, 5));
                    JTextField tfHoTen = new JTextField(person.getFullName(), 15);
                    JTextField tfMa = new JTextField(person.getDocumentCode(), 15);
                    JComboBox<String> cbLoai = new JComboBox<>(new String[]{"Không có", "Mã định danh", "Hộ chiếu"});
                    JComboBox<String> cbPhu = new JComboBox<>(new String[]{"Không xác định", "Dưới 14 tuổi"});

                    // Tạo panel sử dụng CardLayout để chứa cbPhu và tfMa
                    JPanel cardPanel = new JPanel(new CardLayout());
                    cardPanel.add(cbPhu, "cbPhu");
                    cardPanel.add(tfMa, "tfMa");
                    CardLayout cardLayout = (CardLayout) cardPanel.getLayout();

                    // Thiết lập trạng thái ban đầu
                    if ("Không có".equals(person.getDocumentType())) {
                        cardLayout.show(cardPanel, "cbPhu");
                    } else {
                        cardLayout.show(cardPanel, "tfMa");
                    }

                    cbLoai.addActionListener(ev -> {
                        if ("Không có".equals(cbLoai.getSelectedItem())) {
                            cardLayout.show(cardPanel, "cbPhu");
                        } else {
                            cardLayout.show(cardPanel, "tfMa");
                            tfMa.setEditable(true);
                            tfMa.setText("");
                        }
                    });

                    editPanel.add(new JLabel("Họ tên:"));
                    editPanel.add(tfHoTen);
                    editPanel.add(new JLabel("Loại giấy tờ:"));
                    editPanel.add(cbLoai);
                    editPanel.add(new JLabel("Chi tiết loại/Mã giấy tờ:"));
                    editPanel.add(cardPanel);

                    boolean validInput = false;
                    while (!validInput) {
                        int result = JOptionPane.showConfirmDialog(panel, editPanel, "Sửa thông tin khách hàng",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (result == JOptionPane.OK_OPTION) {
                            String newName = tfHoTen.getText().trim();
                            String newType = (String) cbLoai.getSelectedItem();
                            String newCode = "Không có".equals(newType) ? (String) cbPhu.getSelectedItem() : tfMa.getText().trim();

                            if (!validateInput(newType, newCode, panel)) {
                                continue; // Tiếp tục hiển thị bảng sửa
                            }

                            person.setFullName(newName);
                            person.setDocumentType(newType);
                            person.setDocumentCode(newCode);

                            FileUtils.writeToFile("bookings.xml", bookingXML);
                            tableModel.setValueAt(newName, selectedRow, 3);
                            tableModel.setValueAt(newType, selectedRow, 4);
                            tableModel.setValueAt(newCode, selectedRow, 5);

                            JOptionPane.showMessageDialog(panel, "Sửa thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            validInput = true;
                        } else {
                            break; // Thoát nếu người dùng chọn Cancel
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Không tìm thấy khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Booking không còn hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void loadPersonData(LocalDateTime currentTime) {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            bookingXML = new BookingXML();
            bookingXML.setBookings(new ArrayList<>());
        }

        List<Booking> activeBookings = bookingXML.getBookings().stream()
                //.filter(b -> !b.getCheckIn().isAfter(currentTime) && b.getCheckOut().isAfter(currentTime))
                .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Booking booking : activeBookings) {
            if ("Check-in".equalsIgnoreCase(booking.getStatus())) {
                for (Person person : booking.getPersons()) {
                    tableModel.addRow(new Object[]{
                            booking.getUserName() != null ? booking.getUserName() : "Không có",
                            booking.getPhone(),
                            booking.getEmail(),
                            person.getFullName(),
                            person.getDocumentType() != null ? person.getDocumentType() : "Không có",
                            person.getDocumentCode() != null ? person.getDocumentCode() : "Không có",
                            booking.getRoomId(),
                            booking.getCheckIn().format(DATE_FORMATTER),
                            booking.getCheckOut().format(DATE_FORMATTER),
                            booking.getBookingId()
                    });
                }
            }
        }
    }


    private static void showCustomerDialog(JPanel parent, DefaultTableModel model, AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<String, Person>> editData) {
        Dimension inputSize = new Dimension(250, 25); // hoặc bạn tùy chỉnh số pixel

        JTextField tfHoTen = new JTextField();
        tfHoTen.setPreferredSize(inputSize);

        JTextField tfDinhDanh = new JTextField();
        tfDinhDanh.setPreferredSize(inputSize);

        JComboBox<String> cbDinhDanh = new JComboBox<>(new String[]{"Không xác định", "Dưới 14 tuổi"});
        cbDinhDanh.setPreferredSize(inputSize);

        JComboBox<String> cbDocType = new JComboBox<>(new String[]{"Mã định danh", "Hộ chiếu", "Không có"});
        cbDocType.setPreferredSize(inputSize);


        String bookingId = null;
        if (editData != null) {
            Person person = editData.getValue().getValue();
            bookingId = editData.getValue().getKey();
            tfHoTen.setText(person.getFullName());
            tfDinhDanh.setText(person.getDocumentCode());
            cbDocType.setSelectedItem(person.getDocumentType() != null ? person.getDocumentType() : "Mã định danh");
        }

    // Create a panel with CardLayout to switch between text field and combo box
        JPanel documentCodePanel = new JPanel(new CardLayout());
        documentCodePanel.add(tfDinhDanh, "TextField");
        documentCodePanel.add(cbDinhDanh, "ComboBox");

    // Show appropriate input based on documentType
        CardLayout cl = (CardLayout) documentCodePanel.getLayout();
        if (editData == null && cbDocType.getSelectedItem().equals("Không có")) {
            cl.show(documentCodePanel, "ComboBox");
        } else {
            cl.show(documentCodePanel, "TextField");
        }

    // Add listener to switch input field based on documentType
        if (editData == null) {
            cbDocType.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().equals("Không có")) {
                        cl.show(documentCodePanel, "ComboBox");
                    } else {
                        cl.show(documentCodePanel, "TextField");
                    }
                }
            });
        }

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;

        inputPanel.add(new JLabel("Họ tên:"), gbc); gbc.gridx = 1;
        inputPanel.add(tfHoTen, gbc); gbc.gridy++; gbc.gridx = 0;

        inputPanel.add(new JLabel("Loại giấy tờ:"), gbc); gbc.gridx = 1;
        inputPanel.add(cbDocType, gbc); gbc.gridy++; gbc.gridx = 0;

        inputPanel.add(new JLabel("Mã giấy tờ:"), gbc); gbc.gridx = 1;
        inputPanel.add(documentCodePanel, gbc);

        JComboBox<String> cbBookingId = null;
        if (editData == null) {
            BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
            if (bookingXML == null || bookingXML.getBookings() == null) {
                bookingXML = new BookingXML();
                bookingXML.setBookings(new ArrayList<>());
            }
            List<String> bookingIds = bookingXML.getBookings().stream()
                    .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                    .map(Booking::getBookingId)
                    .collect(Collectors.toList());

            if (bookingIds.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Không có booking đang hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cbBookingId = new JComboBox<>(bookingIds.toArray(new String[0]));
            gbc.gridy++; gbc.gridx = 0;
            inputPanel.add(new JLabel("Mã booking:"), gbc); gbc.gridx = 1;
            inputPanel.add(cbBookingId, gbc);
        }

        boolean validInput = false;
        while (!validInput) {
            int result = JOptionPane.showConfirmDialog(parent, inputPanel,
                    editData == null ? "Thêm khách hàng" : "Sửa thông tin",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return; // User canceled, exit the dialog
            }

            String fullName = tfHoTen.getText().trim();
            String documentCode = editData == null && cbDocType.getSelectedItem().equals("Không có")
                    ? (String) cbDinhDanh.getSelectedItem()
                    : tfDinhDanh.getText().trim();
            String documentType = (String) cbDocType.getSelectedItem();

            // Check for empty fields
            if (fullName.isEmpty() || documentCode.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

        // Check document code format for Mã định danh and Hộ chiếu
            if (editData == null) {
                if (documentType.equals("Mã định danh") && !documentCode.matches("^\\d{12}$")) {
                    JOptionPane.showMessageDialog(parent,
                            "Mã định danh phải chứa đúng 12 chữ số!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (documentType.equals("Hộ chiếu") && !documentCode.matches("^[A-Za-z0-9]{8}$")) {
                    JOptionPane.showMessageDialog(parent,
                            "Hộ chiếu phải chứa đúng 8 ký tự (chữ cái hoặc số)!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }
            }

        // Check number of people and duplicate document code for adding a new person
            if (editData == null) {
                BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
                if (bookingXML == null || bookingXML.getBookings() == null) {
                    bookingXML = new BookingXML();
                    bookingXML.setBookings(new ArrayList<>());
                }
                String selectedBookingId = (String) cbBookingId.getSelectedItem();

            // Check number of people
                Optional<Booking> bookingOpt = bookingXML.getBookings().stream()
                        .filter(b -> b.getBookingId().equals(selectedBookingId))
                        .findFirst();
                if (bookingOpt.isPresent()) {
                    Booking booking = bookingOpt.get();
                    List<Person> persons = booking.getPersons();
                    int personCount = (persons != null) ? persons.size() : 0;
                    if (personCount >= 4) {
                        JOptionPane.showMessageDialog(parent,
                                "Số lượng khách hàng của booking này đã đạt tối đa (4 người)!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                }

            // Check for duplicate document code for Mã định danh or Hộ chiếu
                if (!documentType.equals("Không có")) {
                    boolean isDuplicate = bookingXML.getBookings().stream()
                            .filter(b -> b.getPersons() != null)
                            .flatMap(b -> b.getPersons().stream())
                            .anyMatch(p -> documentCode.equals(p.getDocumentCode()) &&
                                    !p.getDocumentType().equals("Không có"));
                    if (isDuplicate) {
                        JOptionPane.showMessageDialog(parent,
                                "Mã giấy tờ đã tồn tại!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                }
            }

            Person person = new Person();
            person.setFullName(fullName);
            person.setDocumentCode(documentCode);
            person.setDocumentType(documentType);

            try {
                if (editData == null) {
                    bookingService.addPerson((String) cbBookingId.getSelectedItem(), person);
                } else {
                    String oldDocumentCode = (String) model.getValueAt(editData.getKey(), 5);
                    String oldFullName = (String) model.getValueAt(editData.getKey(), 3);
                    bookingService.updatePerson(bookingId, oldDocumentCode, oldFullName, person);
                }
                loadPersonData(LocalDateTime.now());
                validInput = true; // Success, exit the loop
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                continue; // Keep dialog open for retry
            }
        }
    }
    private static boolean validateInput(String documentType, String documentCode, Component parent) {
        if (documentType == null || documentCode == null) {
            JOptionPane.showMessageDialog(parent,
                    "Thông tin không được để trống!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if ("Mã định danh".equals(documentType)) {
            if (!documentCode.matches("^\\d{12}$")) {
                JOptionPane.showMessageDialog(parent,
                        "Mã định danh phải chứa đúng 12 chữ số!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if ("Hộ chiếu".equals(documentType)) {
            if (!documentCode.matches("^[A-Za-z0-9]{8}$")) {
                JOptionPane.showMessageDialog(parent,
                        "Hộ chiếu phải chứa đúng 8 ký tự (chữ cái hoặc số)!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if ("Không có".equals(documentType)) {
            if (!"Không xác định".equals(documentCode) && !"Dưới 14 tuổi".equals(documentCode)) {
                JOptionPane.showMessageDialog(parent,
                        "Vui lòng chọn một giá trị hợp lệ cho trường hợp không có giấy tờ!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Loại giấy tờ không hợp lệ!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

