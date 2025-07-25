
package org.example.controller;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.entity.Room;
import org.example.service.RoomService;
import org.example.action.CheckRoomSearch;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomController {

    private final RoomService roomService;
    private final DefaultTableModel tableModel;
    private final JTextField tfTuKhoa;
    private final JComboBox<String> cbTrangThai;
    private final JComboBox<String> cbTieuChi;
    private final JComboBox<String> cbLoaiPhong;
    private final JTable table;
    private final Booking[] bookings = loadBookings("bookings.xml");

    public RoomController(DefaultTableModel tableModel, JTextField tfTuKhoa, JComboBox<String> cbTrangThai,
                          JComboBox<String> cbTieuChi, JComboBox<String> cbLoaiPhong, JTable table) {
        this.roomService = new RoomService("rooms.xml");
        this.tableModel = tableModel;
        this.tfTuKhoa = tfTuKhoa;
        this.cbTrangThai = cbTrangThai;
        this.cbTieuChi = cbTieuChi;
        this.cbLoaiPhong = cbLoaiPhong;
        this.table = table;
    }

    private Booking[] loadBookings(String bookingFilePath) {
        String error = CheckRoomSearch.validateBookingsFile(bookingFilePath);
        if (error != null) {
            JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new Booking[0];
        }

        try {
            File file = new File(bookingFilePath);
            JAXBContext context = JAXBContext.newInstance(BookingXML.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            BookingXML bookingData = (BookingXML) unmarshaller.unmarshal(file);
            List<Booking> bookingList = bookingData.getBookings();
            return bookingList.toArray(new Booking[0]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi đọc file bookings.xml: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new Booking[0];
        }
    }

    public void handleSearch() {
        String tieuChi = (String) cbTieuChi.getSelectedItem();
        String loaiPhong = (String) cbLoaiPhong.getSelectedItem();
        String tuKhoa = tieuChi.equals("Trạng thái phòng") ?
                cbTrangThai.getSelectedItem().toString() : tfTuKhoa.getText().trim();

        String error = CheckRoomSearch.validateSearch(tieuChi, tuKhoa, loaiPhong);
        if (error != null) {
            JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        List<Room> rooms = roomService.getAllRooms();
        Map<String, String> roomStatusMap = getRoomStatusMap("bookings.xml");
        for (Room room : rooms) {
            String roomId = room.getRoomId();
            String soPhong = room.getRoomNumber();
            String kieuPhong = room.getType();
            String giaTien = String.format("%,.0f", room.getPrice());
            String trangThai = roomStatusMap.getOrDefault(roomId, "Đang trống");
            String moTa = room.getDescription();

            boolean matchTieuChi;
            if (tieuChi.equals("Trạng thái phòng")) {
                matchTieuChi = trangThai.equalsIgnoreCase(tuKhoa);
            } else if (tieuChi.equals("Giá phòng")) {
                try {
                    if (tuKhoa.contains("-")) {
                        String[] range = tuKhoa.split("-");
                        double minPrice = Double.parseDouble(range[0].trim().replace(",", ""));
                        double maxPrice = Double.parseDouble(range[1].trim().replace(",", ""));
                        matchTieuChi = room.getPrice() >= minPrice && room.getPrice() <= maxPrice;
                    } else {
                        double inputPrice = Double.parseDouble(tuKhoa.replace(",", ""));
                        matchTieuChi = Math.abs(room.getPrice() - inputPrice) < 0.01;
                    }
                } catch (NumberFormatException ex) {
                    matchTieuChi = false;
                }
            } else {
                String value = tieuChi.equals("Mã phòng") ? roomId : soPhong;
                matchTieuChi = tuKhoa.isEmpty() || value.toLowerCase().contains(tuKhoa.toLowerCase());
            }

            boolean matchLoaiPhong = loaiPhong.equals("Tất cả") || kieuPhong.equalsIgnoreCase(loaiPhong);
            if (matchTieuChi && matchLoaiPhong) {
                tableModel.addRow(new Object[]{roomId, soPhong, kieuPhong, giaTien, trangThai, moTa});
            }
        }
    }

    public void handleAdd() {
        JTextField tfRoomId = new JTextField(10);
        JTextField tfRoomNumber = new JTextField(10);
        JTextField tfDescription = new JTextField(20);
        JComboBox<String> cbType = new JComboBox<>(new String[]{
                "Phòng đơn", "Phòng đôi", "Phòng gia đình", "Phòng đặc biệt"
        });
        JTextField tfPrice = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        dialogPanel.add(new JLabel("Mã phòng:"));
        dialogPanel.add(tfRoomId);
        dialogPanel.add(new JLabel("Số phòng:"));
        dialogPanel.add(tfRoomNumber);
        dialogPanel.add(new JLabel("Mô tả:"));
        dialogPanel.add(tfDescription);
        dialogPanel.add(new JLabel("Loại phòng:"));
        dialogPanel.add(cbType);
        dialogPanel.add(new JLabel("Giá phòng:"));
        dialogPanel.add(tfPrice);

        while (true) {
            int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Thêm phòng mới",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return; // Người dùng nhấn Cancel hoặc đóng dialog
            }

            String roomId = tfRoomId.getText().trim();
            String roomNumber = tfRoomNumber.getText().trim();
            String description = tfDescription.getText().trim();
            String type = (String) cbType.getSelectedItem();
            String priceStr = tfPrice.getText().trim();

            String error = CheckRoomSearch.validateAddRoom(roomId, roomNumber, description, type, priceStr, roomService);
            if (error != null) {
                JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue; // Giữ dialog mở để nhập lại
            }

            try {
                double price = Double.parseDouble(priceStr.replace(",", ""));
                Room newRoom = new Room();
                newRoom.setRoomId(roomId);
                newRoom.setRoomNumber(roomNumber);
                newRoom.setDescription(description);
                newRoom.setType(type);
                newRoom.setPrice(price);
                roomService.addRoom(newRoom);
                loadRoomData();
                JOptionPane.showMessageDialog(null, "Thêm phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                break; // Thoát vòng lặp khi thêm thành công
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue; // Giữ dialog mở nếu có lỗi từ roomService
            }
        }
    }

    public void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một phòng để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String roomId = (String) tableModel.getValueAt(selectedRow, 0);
        Room room = roomService.findRoomById(roomId);
        if (room == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy phòng với mã: " + roomId, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField tfRoomId = new JTextField(room.getRoomId(), 15); // Đồng bộ setColumns
        tfRoomId.setEditable(false);
        JTextField tfRoomNumber = new JTextField(room.getRoomNumber(), 15); // Đồng bộ setColumns
        JTextField tfDescription = new JTextField(room.getDescription(), 15); // Đồng bộ setColumns
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Phòng đơn", "Phòng đôi", "Phòng gia đình", "Phòng đặc biệt"});
        cbType.setSelectedItem(room.getType());
        cbType.setPreferredSize(new Dimension(150, 25)); // Đặt kích thước tương ứng với JTextField
        JTextField tfPrice = new JTextField(String.format("%,.0f", room.getPrice()), 15); // Đồng bộ setColumns

        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 15, 15)); // Tăng khoảng cách giữa các ô
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // Padding lề trái 20px
        dialogPanel.add(new JLabel("Mã phòng:"));
        dialogPanel.add(tfRoomId);
        dialogPanel.add(new JLabel("Số phòng:"));
        dialogPanel.add(tfRoomNumber);
        dialogPanel.add(new JLabel("Mô tả:"));
        dialogPanel.add(tfDescription);
        dialogPanel.add(new JLabel("Loại phòng:"));
        dialogPanel.add(cbType);
        dialogPanel.add(new JLabel("Giá phòng:"));
        dialogPanel.add(tfPrice);

        while (true) {
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(dialogPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnOk = new JButton("OK");
            JButton btnCancel = new JButton("Hủy");
            styleDialogButton(btnOk, new Color(0, 153, 76));    // Xanh lá
            styleDialogButton(btnCancel, new Color(200, 55, 60)); // Đỏ
            buttonPanel.add(btnOk);
            buttonPanel.add(btnCancel);

            JDialog dialog = new JDialog((Frame) null, "Sửa phòng", true);
            dialog.setContentPane(contentPanel);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setSize(500, 350); // Tăng kích thước dialog lên 500x350
            dialog.setLocationRelativeTo(null);

            final boolean[] isConfirmed = {false};
            btnOk.addActionListener(e -> {
                String roomNumber = tfRoomNumber.getText().trim();
                String description = tfDescription.getText().trim();
                String type = (String) cbType.getSelectedItem();
                String priceStr = tfPrice.getText().trim();

                String error = CheckRoomSearch.validateEditRoom(roomId, roomNumber, description, type, priceStr, roomService);
                if (error != null) {
                    JOptionPane.showMessageDialog(dialog, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(dialog,
                        "Bạn có chắc chắn muốn sửa thông tin phòng này?",
                        "Xác nhận sửa", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        double price = Double.parseDouble(priceStr.replace(",", ""));
                        Room updatedRoom = new Room();
                        updatedRoom.setRoomId(roomId);
                        updatedRoom.setRoomNumber(roomNumber);
                        updatedRoom.setDescription(description);
                        updatedRoom.setType(type);
                        updatedRoom.setPrice(price);
                        roomService.updateRoom(updatedRoom);
                        loadRoomData();
                        JOptionPane.showMessageDialog(dialog, "Sửa phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        isConfirmed[0] = true;
                        dialog.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Giá phòng phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi sửa phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnCancel.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);

            if (isConfirmed[0]) {
                break; // Thoát vòng lặp nếu sửa thành công
            }
            if (!dialog.isVisible()) {
                break; // Thoát nếu người dùng nhấn Cancel hoặc đóng dialog
            }
        }
    }

    private void styleDialogButton(JButton btn, Color bgColor) {
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBackground(bgColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
    }

    public void handleDelete() {
        int selectedRow = table.getSelectedRow();
        String roomId = selectedRow != -1 ? (String) tableModel.getValueAt(selectedRow, 0) : null;

        String error = CheckRoomSearch.validateDeleteRoom(selectedRow, roomId, isRoomInUse(roomId));
        if (error != null) {
            JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Trả về để người dùng chọn lại trên bảng
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa phòng " + roomId + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return; // Người dùng nhấn No hoặc đóng dialog
        }

        try {
            roomService.deleteRoom(roomId);
            loadRoomData();
            JOptionPane.showMessageDialog(null, "Xóa phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadRoomData() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomService.getAllRooms();
        Map<String, String> roomStatusMap = getRoomStatusMap("bookings.xml");
        for (Room room : rooms) {
            String roomId = room.getRoomId();
            String soPhong = room.getRoomNumber();
            String kieuPhong = room.getType();
            String giaTien = String.format("%,.0f", room.getPrice());
            String trangThai = roomStatusMap.getOrDefault(roomId, "Đang trống");
            String moTa = room.getDescription();
            tableModel.addRow(new Object[]{roomId, soPhong, kieuPhong, giaTien, trangThai, moTa});
        }
    }

    private String getStatus(String roomId) {
        return isRoomInUse(roomId) ? "Đang sử dụng" : "Đang trống";
    }

    private boolean isRoomInUse(String roomId) {
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : bookings) {
            if (booking.getRoomId().equals(roomId)) {
                try {
                    LocalDateTime checkIn = booking.getCheckIn();
                    LocalDateTime checkOut = booking.getCheckOut();
                    String status = booking.getStatus();

                    // Giả định check-out vào cuối ngày (23:59:59) để phù hợp với bookings.xml
                    LocalDateTime endOfDay = checkOut.toLocalDate().atTime(23, 59, 59);
                    if (("Đã đặt".equalsIgnoreCase(status) || "Check-in".equalsIgnoreCase(status)) &&
                            (checkIn.isEqual(now) || checkIn.isBefore(now)) &&
                            (checkOut.isEqual(now) || endOfDay.isAfter(now))) {
                        return true;
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public static Map<String, String> getRoomStatusMap(String bookingFilePath) {
        Map<String, String> roomStatusMap = new HashMap<>();
        String error = CheckRoomSearch.validateBookingsFile(bookingFilePath);
        if (error != null) {
            JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return roomStatusMap;
        }

        try {
            File file = new File(bookingFilePath);
            JAXBContext context = JAXBContext.newInstance(BookingXML.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            BookingXML bookingData = (BookingXML) unmarshaller.unmarshal(file);
            LocalDateTime now = LocalDateTime.now();

            for (Booking booking : bookingData.getBookings()) {
                String roomId = booking.getRoomId();
                try {
                    LocalDateTime checkIn = booking.getCheckIn();
                    LocalDateTime checkOut = booking.getCheckOut();
                    String status = booking.getStatus();

                    // Giả định check-out vào cuối ngày (23:59:59) để phù hợp với bookings.xml
                    LocalDateTime endOfDay = checkOut.toLocalDate().atTime(23, 59, 59);
                    if (("Đã đặt".equalsIgnoreCase(status) || "Check-in".equalsIgnoreCase(status)) &&
                            (checkIn.isEqual(now) || checkIn.isBefore(now)) &&
                            (checkOut.isEqual(now) || endOfDay.isAfter(now))) {
                        roomStatusMap.put(roomId, "Đang sử dụng");

                    } else {
                        roomStatusMap.putIfAbsent(roomId, "Đang trống");
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        return roomStatusMap;
    }
}

