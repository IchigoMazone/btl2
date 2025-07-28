
package org.example.controller;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.entity.Room;
import org.example.service.RoomService;
import org.example.action.CheckRoomSearch;
import java.util.ArrayList;
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

    public boolean isRoomIdExists(String roomId) {
        if (roomId == null) {
            return false;
        }
        return roomService.getAllRooms().stream().anyMatch(room -> roomId.equals(room.getRoomId()));
    }

    public boolean isRoomNumberExists(String roomNumber) {
        if (roomNumber == null) {
            return false;
        }
        return roomService.getAllRooms().stream().anyMatch(room -> roomNumber.equals(room.getRoomNumber()));
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

        if (!tieuChi.equals("Trạng thái phòng") && tuKhoa.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập từ khóa tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tieuChi.equals("Giá phòng")) {
            String cleanedTuKhoa = tuKhoa.replace(" ", "").replace(",", "");

            if (!tuKhoa.matches("^\\d{1,3}(,\\d{3})*(\\s*-\\s*\\d{1,3}(,\\d{3})*)?$")) {
                JOptionPane.showMessageDialog(null, "Giá phòng không đúng định dạng! Ví dụ hợp lệ: 350,000 hoặc 350,000 - 500,000", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cleanedTuKhoa.contains("-")) {
                String[] range = cleanedTuKhoa.split("-");
                if (range.length != 2 || !isNumeric(range[0].trim()) || !isNumeric(range[1].trim())) {
                    JOptionPane.showMessageDialog(null, "Khoảng giá không hợp lệ! Nhập theo định dạng '350,000 - 500,000'", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double min = Double.parseDouble(range[0].trim());
                double max = Double.parseDouble(range[1].trim());
                if (min >= max) {
                    JOptionPane.showMessageDialog(null, "Giá min phải nhỏ hơn giá max!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                if (!isNumeric(cleanedTuKhoa)) {
                    JOptionPane.showMessageDialog(null, "Giá phòng phải là số hợp lệ! Ví dụ: 350,000", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else if (tieuChi.equals("Mã phòng")) {
            if (!tuKhoa.matches("R\\d{3,4}")) {
                JOptionPane.showMessageDialog(null, "Mã phòng phải bắt đầu bằng 'R' và theo sau là 3 đến 4 chữ số! Ví dụ: R100, R1056", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (tieuChi.equals("Số phòng")) {
            if (!tuKhoa.matches("\\d+") || tuKhoa.length() > 5 || Integer.parseInt(tuKhoa) <= 100) {
                JOptionPane.showMessageDialog(null, "Số phòng phải là số nguyên dương lớn hơn 100, tối đa 5 chữ số! Ví dụ: 1000", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        tableModel.setRowCount(0);
        List<Room> rooms = roomService.getAllRooms();
        Map<String, String> roomStatusMap = getRoomStatusMap("bookings.xml");
        boolean hasResult = false;

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
                    String cleanedTuKhoa = tuKhoa.replace(" ", "").replace(",", "");
                    if (cleanedTuKhoa.contains("-")) {
                        String[] range = cleanedTuKhoa.split("-");
                        double minPrice = Double.parseDouble(range[0].trim());
                        double maxPrice = Double.parseDouble(range[1].trim());
                        matchTieuChi = room.getPrice() >= minPrice && room.getPrice() <= maxPrice;
                    } else {
                        double inputPrice = Double.parseDouble(cleanedTuKhoa);
                        matchTieuChi = Math.abs(room.getPrice() - inputPrice) < 0.01;
                    }
                } catch (NumberFormatException ex) {
                    matchTieuChi = false;
                }
            } else if (tieuChi.equals("Mã phòng")) {
                matchTieuChi = roomId.toLowerCase().contains(tuKhoa.toLowerCase());
            } else if (tieuChi.equals("Số phòng")) {
                matchTieuChi = soPhong.toLowerCase().contains(tuKhoa.toLowerCase());
            } else if (tieuChi.equals("Loại phòng")) {
                matchTieuChi = kieuPhong.toLowerCase().contains(tuKhoa.toLowerCase());
            } else if (tieuChi.equals("Mô tả")) {
                matchTieuChi = moTa.toLowerCase().contains(tuKhoa.toLowerCase());
            } else {
                matchTieuChi = false;
            }

            boolean matchLoaiPhong = loaiPhong.equals("Tất cả") || kieuPhong.equalsIgnoreCase(loaiPhong);
            if (matchTieuChi && matchLoaiPhong) {
                tableModel.addRow(new Object[]{roomId, soPhong, kieuPhong, giaTien, trangThai, moTa});
                hasResult = true;
            }
        }

        if (!hasResult) {
            JOptionPane.showMessageDialog(null, "Không có phòng nào khớp với tiêu chí tìm kiếm!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
                return;
            }

            String roomId = tfRoomId.getText().trim();
            String roomNumber = tfRoomNumber.getText().trim();
            String description = tfDescription.getText().trim();
            String type = (String) cbType.getSelectedItem();
            String priceStr = tfPrice.getText().trim();

            if (roomId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã phòng không được để trống",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!roomId.matches("^R\\d{3,4}$")) {
                JOptionPane.showMessageDialog(null,
                        "Mã phòng phải bắt đầu bằng 'R' và theo sau là 3 đến 4 chữ số! Ví dụ: R100, R1056",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (isRoomIdExists(roomId)) {
                JOptionPane.showMessageDialog(null, "Mã phòng đã tồn tại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (roomNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Số phòng không được để trống",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!roomNumber.matches("^\\d+$") || roomNumber.length() > 5) {
                JOptionPane.showMessageDialog(null,
                        "Số phòng phải là số nguyên dương, tối đa 5 chữ số!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            try {
                int roomNum = Integer.parseInt(roomNumber);
                if (roomNum <= 100) {
                    JOptionPane.showMessageDialog(null,
                            "Số phòng phải lớn hơn 100! Ví dụ: 1000",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Số phòng phải là số nguyên hợp lệ!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (isRoomNumberExists(roomNumber)) {
                JOptionPane.showMessageDialog(null, "Số phòng đã tồn tại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mô tả không được để trống",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (description.length() > 200) {
                JOptionPane.showMessageDialog(null,
                        "Mô tả không được vượt quá 200 ký tự",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Giá phòng không được để trống",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!priceStr.matches("^\\d{1,3}(,\\d{3})*$")) {
                JOptionPane.showMessageDialog(null,
                        "Giá phòng phải có định dạng đúng, ví dụ: 350,000 hoặc 1,000,000",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            try {
                double price = Double.parseDouble(priceStr.replace(",", ""));
                if (price < 1000 || price > 100000000) {
                    JOptionPane.showMessageDialog(null,
                            "Giá phòng phải từ 0 đến 100,000,000 VND",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                Room newRoom = new Room();
                newRoom.setRoomId(roomId);
                newRoom.setRoomNumber(roomNumber);
                newRoom.setDescription(description);
                newRoom.setType(type);
                newRoom.setPrice(price);
                roomService.addRoom(newRoom);
                loadRoomData();
                JOptionPane.showMessageDialog(null, "Thêm phòng thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Giá phòng phải là số hợp lệ, ví dụ: 350,000",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Lỗi: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
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

        JTextField tfRoomId = new JTextField(room.getRoomId(), 10);
        tfRoomId.setEditable(false);
        JTextField tfRoomNumber = new JTextField(room.getRoomNumber(), 10);
        JTextField tfDescription = new JTextField(room.getDescription(), 20);
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Phòng đơn", "Phòng đôi", "Phòng gia đình", "Phòng đặc biệt"});
        cbType.setSelectedItem(room.getType());
        JTextField tfPrice = new JTextField(String.format("%,.0f", room.getPrice()), 10);

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
            int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Sửa phòng",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String roomNumber = tfRoomNumber.getText().trim();
            String description = tfDescription.getText().trim();
            String type = (String) cbType.getSelectedItem();
            String priceStr = tfPrice.getText().trim();

            if (roomNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Số phòng không được để trống",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!roomNumber.matches("^\\d+$") || roomNumber.length() > 5) {
                JOptionPane.showMessageDialog(null,
                        "Số phòng phải là số nguyên dương, tối đa 5 chữ số!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            try {
                int roomNum = Integer.parseInt(roomNumber);
                if (roomNum <= 100) {
                    JOptionPane.showMessageDialog(null,
                            "Số phòng phải lớn hơn 100! Ví dụ: 1000",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Số phòng phải là số nguyên hợp lệ!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!roomNumber.equals(room.getRoomNumber()) && isRoomNumberExists(roomNumber)) {
                JOptionPane.showMessageDialog(null, "Số phòng đã tồn tại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mô tả không được để trống",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (description.length() > 200) {
                JOptionPane.showMessageDialog(null,
                        "Mô tả không được vượt quá 200 ký tự",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Giá phòng không được để trống",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!priceStr.matches("^\\d{1,3}(,\\d{3})*$")) {
                JOptionPane.showMessageDialog(null,
                        "Giá phòng phải có định dạng đúng, ví dụ: 350,000 hoặc 1,000,000",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            try {
                double price = Double.parseDouble(priceStr.replace(",", ""));
                if (price < 0 || price > 100000000) {
                    JOptionPane.showMessageDialog(null,
                            "Giá phòng phải từ 0 đến 100,000,000 VND",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                Room updatedRoom = new Room();
                updatedRoom.setRoomId(roomId);
                updatedRoom.setRoomNumber(roomNumber);
                updatedRoom.setDescription(description);
                updatedRoom.setType(type);
                updatedRoom.setPrice(price);
                roomService.updateRoom(updatedRoom);
                loadRoomData();
                JOptionPane.showMessageDialog(null, "Sửa phòng thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Giá phòng phải là số hợp lệ, ví dụ: 350,000",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Lỗi: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
    }


    public void handleDelete() {
        int selectedRow = table.getSelectedRow();
        String roomId = selectedRow != -1 ? (String) tableModel.getValueAt(selectedRow, 0) : null;

        String error = CheckRoomSearch.validateDeleteRoom(selectedRow, roomId, isRoomInUse(roomId));
        if (error != null) {
            JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa phòng " + roomId + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
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

