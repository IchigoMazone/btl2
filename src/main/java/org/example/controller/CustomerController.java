package org.example.controller;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.entity.Person;
import org.example.service.BookingService;
import org.example.utils.FileUtils;
import org.example.view.CustomerView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerController {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String XML_PATH = "bookings.xml";
    private static BookingService bookingService;

    public static void setBookingService(BookingService service) {
        bookingService = service;
    }

    public void loadPersonData(DefaultTableModel tableModel) {
        BookingXML bookingXML = FileUtils.readFromFile(XML_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            bookingXML = new BookingXML();
            bookingXML.setBookings(new ArrayList<>());
        }

        List<Booking> activeBookings = bookingXML.getBookings().stream()
                .filter(b -> !b.getCheckIn().isAfter(LocalDateTime.now()) && b.getCheckOut().isAfter(LocalDateTime.now())
                        && "Check-in".equalsIgnoreCase(b.getStatus()))
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Booking booking : activeBookings) {
            for (Person person : booking.getPersons()) {
                tableModel.addRow(new Object[]{
                        booking.getUserName() != null ? booking.getUserName().toString() : "Không có",
                        booking.getPhone(),
                        booking.getEmail(),
                        person.getFullName(),
                        person.getDocumentType() != null ? person.getDocumentType().toString() : null,
                        person.getDocumentCode() != null ? person.getDocumentCode().toString() : null,
                        booking.getRoomId(),
                        booking.getCheckIn().format(DATE_FORMATTER),
                        booking.getCheckOut().format(DATE_FORMATTER),
                        booking.getBookingId()
                });
            }
        }
    }

    public void handleSearch(String keyword, String searchType, DefaultTableModel tableModel, JPanel parentPanel) {
        LocalDateTime currentTime = LocalDateTime.now();

        BookingXML bookingXML = FileUtils.readFromFile(XML_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            JOptionPane.showMessageDialog(parentPanel, "Không thể đọc dữ liệu khách hàng!", "Lỗi!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Booking> activeBookings = bookingXML.getBookings().stream()
                .filter(b -> !b.getCheckIn().isAfter(currentTime) && b.getCheckOut().isAfter(currentTime)
                        && "Check-in".equalsIgnoreCase(b.getStatus()))
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Booking booking : activeBookings) {
            List<Person> filteredPersons = booking.getPersons().stream()
                    .filter(person -> {
                        if (!keyword.isEmpty()) {
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
                                case "Mã phòng":
                                    return booking.getRoomId().toLowerCase().contains(keyword);
                            }
                        }
                        return true;
                    }).collect(Collectors.toList());

            for (Person person : filteredPersons) {
                tableModel.addRow(new Object[]{
                        booking.getUserName() != null ? booking.getUserName().toString() : "Không có",
                        booking.getPhone(),
                        booking.getEmail(),
                        person.getFullName(),
                        person.getDocumentType() != null ? person.getDocumentType().toString() : null,
                        person.getDocumentCode() != null ? person.getDocumentCode().toString() : null,
                        booking.getRoomId(),
                        booking.getCheckIn().format(DATE_FORMATTER),
                        booking.getCheckOut().format(DATE_FORMATTER),
                        booking.getBookingId()
                });
            }
        }
    }

    public void showCustomerDialog(JPanel parent, DefaultTableModel model, AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<String, Person>> editData) {
        List<String> bookingIds = getActiveBookingIds();
        if (editData == null && bookingIds.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Không có booking đang hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CustomerView.showCustomerDialog(parent, model, editData, this, bookingIds);
    }

    public void handleDialogSubmit(String fullName, String documentCode, String documentType,
                                   AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<String, Person>> editData,
                                   String selectedBookingId, DefaultTableModel model, JPanel parent) {
        if (fullName.isEmpty() || documentCode.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Person person = new Person();
        person.setFullName(fullName);
        person.setDocumentCode(documentCode);
        person.setDocumentType(documentType);

        try {
            if (editData == null) {
                bookingService.addPerson(selectedBookingId, person);
            } else {
                String bookingId = editData.getValue().getKey();
                String oldDocumentCode = (String) model.getValueAt(editData.getKey(), 5);
                String oldFullName = (String) model.getValueAt(editData.getKey(), 3);
                bookingService.updatePerson(bookingId, oldDocumentCode, oldFullName, person);
            }
            loadPersonData(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void handleDelete(int selectedRow, DefaultTableModel tableModel, JPanel parentPanel) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parentPanel, "Vui lòng chọn dòng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String documentCode = (String) tableModel.getValueAt(selectedRow, 5);
        String fullName = (String) tableModel.getValueAt(selectedRow, 3);
        String bookingId = (String) tableModel.getValueAt(selectedRow, 9);

        int confirm = JOptionPane.showConfirmDialog(parentPanel,
                "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bookingService.deletePerson(bookingId, documentCode, fullName);
                loadPersonData(tableModel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentPanel, "Lỗi khi xóa khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public void handleEdit(int selectedRow, DefaultTableModel tableModel, JPanel parentPanel) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parentPanel, "Vui lòng chọn dòng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String documentCode = (String) tableModel.getValueAt(selectedRow, 5);
        String fullName = (String) tableModel.getValueAt(selectedRow, 3);
        String bookingId = (String) tableModel.getValueAt(selectedRow, 9);

        BookingXML bookingXML = FileUtils.readFromFile(XML_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            JOptionPane.showMessageDialog(parentPanel, "Không thể đọc dữ liệu khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId) &&
                        !b.getCheckIn().isAfter(LocalDateTime.now()) &&
                        b.getCheckOut().isAfter(LocalDateTime.now()) &&
                        "Check-in".equalsIgnoreCase(b.getStatus()))
                .findFirst()
                .orElse(null);

        if (booking != null) {
            Person person = booking.getPersons().stream()
                    .filter(p -> p.getDocumentCode().equals(documentCode) && p.getFullName().equals(fullName))
                    .findFirst()
                    .orElse(null);
            if (person != null) {
                showCustomerDialog(parentPanel, tableModel, new AbstractMap.SimpleEntry<>(selectedRow,
                        new AbstractMap.SimpleEntry<>(bookingId, person)));
            } else {
                JOptionPane.showMessageDialog(parentPanel, "Không tìm thấy khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(parentPanel, "Booking không còn hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> getActiveBookingIds() {
        BookingXML bookingXML = FileUtils.readFromFile(XML_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            return new ArrayList<>();
        }
        return bookingXML.getBookings().stream()
                .filter(b -> !b.getCheckIn().isAfter(LocalDateTime.now()) &&
                        b.getCheckOut().isAfter(LocalDateTime.now()) &&
                        "Check-in".equalsIgnoreCase(b.getStatus()))
                .map(Booking::getBookingId)
                .collect(Collectors.toList());
    }
}