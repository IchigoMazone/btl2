package org.example.action;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.entity.Person;
import org.example.service.BookingService;
import org.example.utils.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CheckCustomer {
    /**
     * Kiểm tra dữ liệu nhập khi tìm kiếm khách hàng.
     */
    public static boolean validateSearchInput(JPanel panel, String searchType, String keyword) {
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(panel,
                    "Vui lòng nhập từ khóa tìm kiếm!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        switch (searchType) {
            case "Số điện thoại":
                if (!keyword.matches("0\\d{9}")) {
                    JOptionPane.showMessageDialog(panel,
                            "Số điện thoại phải chứa đúng 10 chữ số và bắt đầu bằng 0!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            case "Gmail":
                if (!keyword.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(panel,
                            "Email không đúng định dạng!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            case "Mã định danh":
                if (!keyword.matches("\\d{12}")) {
                    JOptionPane.showMessageDialog(panel,
                            "Mã định danh phải chứa đúng 12 chữ số!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            case "Hộ chiếu":
                if (!keyword.matches("[A-Za-z0-9]{8}")) {
                    JOptionPane.showMessageDialog(panel,
                            "Hộ chiếu phải chứa đúng 8 chữ cái hoặc số!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            case "Mã phòng":
                if (!keyword.matches("r\\d{3,4}")) {
                    JOptionPane.showMessageDialog(panel,
                            "Mã phòng phải bắt đầu bằng 'R' và có 3 đến 4 chữ số!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public static List<Person> filterPersons(Booking booking, String searchType, String keyword) {
        return booking.getPersons().stream()
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
    }

    public static void showCustomerDialog(JPanel parent, DefaultTableModel model,
                                          AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<String, Person>> editData,
                                          BookingService bookingService) {
        Dimension inputSize = new Dimension(250, 25);

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

        JPanel documentCodePanel = new JPanel(new CardLayout());
        documentCodePanel.add(tfDinhDanh, "TextField");
        documentCodePanel.add(cbDinhDanh, "ComboBox");

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
                    .toList();

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
                return;
            }

            String fullName = tfHoTen.getText().trim();
            String documentCode = editData == null && cbDocType.getSelectedItem().equals("Không có")
                    ? (String) cbDinhDanh.getSelectedItem()
                    : tfDinhDanh.getText().trim();
            String documentType = (String) cbDocType.getSelectedItem();

            if (fullName.isEmpty() || documentCode.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

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

            if (editData == null) {
                BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
                if (bookingXML == null || bookingXML.getBookings() == null) {
                    bookingXML = new BookingXML();
                    bookingXML.setBookings(new ArrayList<>());
                }
                String selectedBookingId = (String) cbBookingId.getSelectedItem();

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

                // CHỈ CHECK MÃ GIẤY TỜ TRONG 1 PHÒNG
                if (!documentType.equals("Không có")) {
                    // Lấy roomId của booking đang được chọn
                    if (bookingOpt.isPresent()) {
                        String roomId = bookingOpt.get().getRoomId();
                        // Kiểm tra trùng mã giấy tờ trong tất cả các booking cùng phòng
                        boolean isDuplicate = bookingXML.getBookings().stream()
                                .filter(b -> b.getRoomId().equals(roomId))
                                .filter(b -> b.getPersons() != null)
                                .flatMap(b -> b.getPersons().stream())
                                .anyMatch(p -> documentCode.equals(p.getDocumentCode()) && !p.getDocumentType().equals("Không có"));
                        if (isDuplicate) {
                            JOptionPane.showMessageDialog(parent,
                                    "Mã giấy tờ đã tồn tại trong phòng này!",
                                    "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
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
                // Reload table
                BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
                if (bookingXML == null || bookingXML.getBookings() == null) {
                    bookingXML = new BookingXML();
                    bookingXML.setBookings(new ArrayList<>());
                }
                List<Booking> activeBookings = bookingXML.getBookings().stream()
                        .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                        .toList();

                model.setRowCount(0);
                for (Booking booking : activeBookings) {
                    if ("Check-in".equalsIgnoreCase(booking.getStatus())) {
                        for (Person p : booking.getPersons()) {
                            model.addRow(new Object[]{
                                    booking.getUserName() != null ? booking.getUserName() : "Không có",
                                    booking.getPhone(),
                                    booking.getEmail(),
                                    p.getFullName(),
                                    p.getDocumentType() != null ? p.getDocumentType() : "Không có",
                                    p.getDocumentCode() != null ? p.getDocumentCode() : "Không có",
                                    booking.getRoomId(),
                                    booking.getCheckIn().format(org.example.view.CustomerView.DATE_FORMATTER),
                                    booking.getCheckOut().format(org.example.view.CustomerView.DATE_FORMATTER),
                                    booking.getBookingId()
                            });
                        }
                    }
                }
                validInput = true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}