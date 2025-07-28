package org.example.controller;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.entity.Person;
import org.example.service.BookingService;
import org.example.utils.FileUtils;
import org.example.view.CustomerView;
import org.example.action.CheckCustomer;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class CustomerController {

    private BookingService bookingService;

    public CustomerController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void loadPersonData() {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            bookingXML = new BookingXML();
            bookingXML.setBookings(new ArrayList<>());
        }

        List<Booking> activeBookings = bookingXML.getBookings().stream()
                .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                .toList();

        CustomerView.tableModel.setRowCount(0);
        for (Booking booking : activeBookings) {
            if ("Check-in".equalsIgnoreCase(booking.getStatus())) {
                for (Person person : booking.getPersons()) {
                    CustomerView.tableModel.addRow(new Object[]{
                            booking.getUserName() != null ? booking.getUserName() : "Không có",
                            booking.getPhone(),
                            booking.getEmail(),
                            person.getFullName(),
                            person.getDocumentType() != null ? person.getDocumentType() : "Không có",
                            person.getDocumentCode() != null ? person.getDocumentCode() : "Không có",
                            booking.getRoomId(),
                            booking.getCheckIn().format(CustomerView.DATE_FORMATTER),
                            booking.getCheckOut().format(CustomerView.DATE_FORMATTER),
                            booking.getBookingId()
                    });
                }
            }
        }
    }

    public void handleSearch(JPanel panel, JComboBox<String> cbTieuChi, JTextField tfTuKhoa) {
        String keyword = tfTuKhoa.getText().trim().toLowerCase();
        String searchType = (String) cbTieuChi.getSelectedItem();

        if (!CheckCustomer.validateSearchInput(panel, searchType, keyword)) return;

        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            JOptionPane.showMessageDialog(panel,
                    "Không thể đọc dữ liệu khách hàng!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Booking> activeBookings = bookingXML.getBookings().stream()
                .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                .toList();

        CustomerView.tableModel.setRowCount(0);
        boolean hasResults = false;

        for (Booking booking : activeBookings) {
            List<Person> filteredPersons = CheckCustomer.filterPersons(booking, searchType, keyword);

            for (Person person : filteredPersons) {
                CustomerView.tableModel.addRow(new Object[]{
                        booking.getUserName() != null ? booking.getUserName() : "Không có",
                        booking.getPhone(),
                        booking.getEmail(),
                        person.getFullName(),
                        person.getDocumentType() != null ? person.getDocumentType() : "Không có",
                        person.getDocumentCode() != null ? person.getDocumentCode() : "Không có",
                        booking.getRoomId(),
                        booking.getCheckIn().format(CustomerView.DATE_FORMATTER),
                        booking.getCheckOut().format(CustomerView.DATE_FORMATTER),
                        booking.getBookingId()
                });
                hasResults = true;
            }
        }

        if (!hasResults) {
            JOptionPane.showMessageDialog(panel,
                    "Không tìm thấy khách hàng phù hợp với từ khóa!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void handleAdd(JPanel parent, DefaultTableModel model) {
        CheckCustomer.showCustomerDialog(parent, model, null, bookingService);
    }

    public void handleDelete(JPanel panel, DefaultTableModel model, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fullName = (String) model.getValueAt(selectedRow, 3);
        String documentCode = (String) model.getValueAt(selectedRow, 5);
        String bookingId = (String) model.getValueAt(selectedRow, 9);

        int confirm = JOptionPane.showConfirmDialog(panel,
                "Bạn có chắc muốn xóa khách hàng " + fullName + " (Mã tài liệu: " + documentCode + ", Dòng: " + (selectedRow + 1) + ")?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bookingService.deletePersonByRow(selectedRow, bookingId);
                loadPersonData();
                JOptionPane.showMessageDialog(panel, "Xóa khách hàng thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Lỗi khi xóa khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleEdit(JPanel panel, DefaultTableModel model, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String documentCode = (String) model.getValueAt(selectedRow, 5);
        String fullName = (String) model.getValueAt(selectedRow, 3);
        String bookingId = (String) model.getValueAt(selectedRow, 9);

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
                AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<String, Person>> editData =
                        new AbstractMap.SimpleEntry<>(selectedRow, new AbstractMap.SimpleEntry<>(bookingId, person));
                CheckCustomer.showCustomerDialog(panel, model, editData, bookingService);
            } else {
                JOptionPane.showMessageDialog(panel, "Không tìm thấy khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Booking không còn hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}