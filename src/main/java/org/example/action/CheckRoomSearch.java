//package org.example.action;
//
//import org.example.service.RoomService;
//
//import java.io.File;
//
//public class CheckRoomSearch {
//
//    /**
//     * Kiểm tra dữ liệu tìm kiếm phòng từ giao diện RoomView.
//     * @param tieuChi Tiêu chí tìm kiếm ("Trạng thái phòng", "Mã phòng", "Số phòng", "Giá phòng")
//     * @param tuKhoa Từ khóa nhập vào (hoặc trạng thái từ JComboBox)
//     * @param loaiPhong Loại phòng ("Tất cả", "Phòng đơn", "Phòng đôi", "Phòng gia đình", "Phòng đặc biệt")
//     * @return null nếu hợp lệ, chuỗi lỗi nếu không hợp lệ
//     */
//    public static String validateSearch(String tieuChi, String tuKhoa, String loaiPhong) {
//        // Kiểm tra tiêu chí tìm kiếm
//        if (tieuChi == null || tieuChi.isEmpty()) {
//            return "Vui lòng chọn tiêu chí tìm kiếm. Vui lòng tìm kiếm lại.";
//        }
//        if (!tieuChi.equals("Trạng thái phòng") && !tieuChi.equals("Mã phòng") &&
//                !tieuChi.equals("Số phòng") && !tieuChi.equals("Giá phòng")) {
//            return "Tiêu chí tìm kiếm không hợp lệ. Vui lòng tìm kiếm lại.";
//        }
//
//        // Kiểm tra loại phòng
//        if (loaiPhong == null || loaiPhong.isEmpty()) {
//            return "Vui lòng chọn loại phòng. Vui lòng tìm kiếm lại.";
//        }
//        if (!loaiPhong.equals("Tất cả") && !loaiPhong.equals("Phòng đơn") &&
//                !loaiPhong.equals("Phòng đôi") && !loaiPhong.equals("Phòng gia đình") &&
//                !loaiPhong.equals("Phòng đặc biệt")) {
//            return "Loại phòng không hợp lệ. Vui lòng tìm kiếm lại.";
//        }
//
//        // Kiểm tra từ khóa
//        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
//            return "Vui lòng nhập từ khóa tìm kiếm. Vui lòng tìm kiếm lại.";
//        }
//
//        // Kiểm tra từ khóa theo tiêu chí
//        if (tieuChi.equals("Trạng thái phòng")) {
//            if (!tuKhoa.equals("Đang trống") && !tuKhoa.equals("Đang sử dụng")) {
//                return "Trạng thái phòng phải là 'Đang trống' hoặc 'Đang sử dụng'. Vui lòng tìm kiếm lại.";
//            }
//        } else if (tieuChi.equals("Số phòng")) {
//            if (!tuKhoa.matches("\\d+")) {
//                return "Số phòng phải là số (ví dụ: 401). Vui lòng tìm kiếm lại.";
//            }
//        } else if (tieuChi.equals("Giá phòng")) {
//            String cleanedTuKhoa = tuKhoa.replace(",", "").trim();
//            if (cleanedTuKhoa.contains("-")) {
//                // Kiểm tra khoảng giá
//                String[] range = cleanedTuKhoa.split("-");
//                if (range.length != 2) {
//                    return "Định dạng khoảng giá không hợp lệ (ví dụ: 300000-500000). Vui lòng tìm kiếm lại.";
//                }
//                try {
//                    double minPrice = Double.parseDouble(range[0].trim());
//                    double maxPrice = Double.parseDouble(range[1].trim());
//                    if (minPrice < 0 || maxPrice < 0) {
//                        return "Giá phòng không được âm. Vui lòng tìm kiếm lại.";
//                    }
//                    if (minPrice > maxPrice) {
//                        return "Giá tối thiểu phải nhỏ hơn hoặc bằng giá tối đa. Vui lòng tìm kiếm lại.";
//                    }
//                } catch (NumberFormatException e) {
//                    return "Định dạng khoảng giá không hợp lệ (ví dụ: 300000-500000). Vui lòng tìm kiếm lại.";
//                }
//            } else {
//                // Kiểm tra giá đơn
//                try {
//                    double price = Double.parseDouble(cleanedTuKhoa);
//                    if (price < 0) {
//                        return "Giá phòng không được âm. Vui lòng tìm kiếm lại.";
//                    }
//                } catch (NumberFormatException e) {
//                    return "Giá phòng phải là một số hợp lệ. Vui lòng tìm kiếm lại.";
//                }
//            }
//        } else {
//            // Mã phòng
//            if (tuKhoa.trim().length() > 50) {
//                return "Từ khóa tìm kiếm không được dài quá 50 ký tự. Vui lòng tìm kiếm lại.";
//            }
//        }
//
//        return null; // Dữ liệu hợp lệ
//    }
//
//    /**
//     * Kiểm tra dữ liệu khi thêm phòng mới.
//     * @param roomId Mã phòng
//     * @param roomNumber Số phòng
//     * @param description Mô tả phòng
//     * @param type Loại phòng
//     * @param priceStr Giá phòng (chuỗi)
//     * @param roomService Dịch vụ phòng để kiểm tra trùng lặp
//     * @return null nếu hợp lệ, chuỗi lỗi nếu không hợp lệ
//     */
//    public static String validateAddRoom(String roomId, String roomNumber, String description, String type, String priceStr, RoomService roomService) {
//        if (roomId == null || roomId.isEmpty() || roomNumber == null || roomNumber.isEmpty() ||
//                description == null || description.isEmpty() || type == null || priceStr == null || priceStr.isEmpty()) {
//            return "Vui lòng nhập đầy đủ thông tin phòng.";
//        }
//
//        if (!roomId.matches("R\\d+")) {
//            return "Mã phòng phải bắt đầu bằng 'R' và theo sau là số (ví dụ: R016).";
//        }
//
//        if (!roomNumber.matches("\\d+")) {
//            return "Số phòng phải là số (ví dụ: 401).";
//        }
//
//        if (roomService.findRoomById(roomId) != null) {
//            return "Mã phòng " + roomId + " đã tồn tại.";
//        }
//
//        if (!type.equals("Phòng đơn") && !type.equals("Phòng đôi") &&
//                !type.equals("Phòng gia đình") && !type.equals("Phòng đặc biệt")) {
//            return "Loại phòng không hợp lệ.";
//        }
//
//        try {
//            double price = Double.parseDouble(priceStr.replace(",", ""));
//            if (price < 0) {
//                return "Giá phòng không được âm.";
//            }
//        } catch (NumberFormatException e) {
//            return "Giá phòng phải là một số hợp lệ.";
//        }
//
//        return null; // Dữ liệu hợp lệ
//    }
//
//    /**
//     * Kiểm tra dữ liệu khi sửa phòng.
//     * @param roomId Mã phòng
//     * @param roomNumber Số phòng
//     * @param description Mô tả phòng
//     * @param type Loại phòng
//     * @param priceStr Giá phòng (chuỗi)
//     * @param roomService Dịch vụ phòng để kiểm tra phòng tồn tại
//     * @return null nếu hợp lệ, chuỗi lỗi nếu không hợp lệ
//     */
//    public static String validateEditRoom(String roomId, String roomNumber, String description, String type, String priceStr, RoomService roomService) {
//        if (roomId == null || roomId.isEmpty()) {
//            return "Mã phòng không được để trống.";
//        }
//
//        if (roomService.findRoomById(roomId) == null) {
//            return "Không tìm thấy phòng " + roomId + ".";
//        }
//
//        if (roomNumber == null || roomNumber.isEmpty() || description == null || description.isEmpty() ||
//                type == null || priceStr == null || priceStr.isEmpty()) {
//            return "Vui lòng nhập đầy đủ thông tin phòng.";
//        }
//
//        if (!roomNumber.matches("\\d+")) {
//            return "Số phòng phải là số (ví dụ: 401).";
//        }
//
//        if (!type.equals("Phòng đơn") && !type.equals("Phòng đôi") &&
//                !type.equals("Phòng gia đình") && !type.equals("Phòng đặc biệt")) {
//            return "Loại phòng không hợp lệ.";
//        }
//
//        try {
//            double price = Double.parseDouble(priceStr.replace(",", ""));
//            if (price < 0) {
//                return "Giá phòng không được âm.";
//            }
//        } catch (NumberFormatException e) {
//            return "Giá phòng phải là một số hợp lệ.";
//        }
//
//        return null; // Dữ liệu hợp lệ
//    }
//
//    /**
//     * Kiểm tra dữ liệu khi xóa phòng.
//     * @param selectedRow Hàng được chọn trong bảng
//     * @param roomId Mã phòng
//     * @param isRoomInUse Hàm kiểm tra phòng đang sử dụng
//     * @return null nếu hợp lệ, chuỗi lỗi nếu không hợp lệ
//     */
//    public static String validateDeleteRoom(int selectedRow, String roomId, boolean isRoomInUse) {
//        if (selectedRow == -1) {
//            return "Vui lòng chọn một phòng để xóa!";
//        }
//
//        if (isRoomInUse) {
//            return "Phòng " + roomId + " đang được đặt, không thể xóa!";
//        }
//
//        return null; // Dữ liệu hợp lệ
//    }
//
//    /**
//     * Kiểm tra file bookings.xml.
//     * @param bookingFilePath Đường dẫn file bookings.xml
//     * @return null nếu hợp lệ, chuỗi lỗi nếu không hợp lệ
//     */
//    public static String validateBookingsFile(String bookingFilePath) {
//        File file = new File(bookingFilePath);
//        if (!file.exists()) {
//            return "Không tìm thấy file " + bookingFilePath + "!";
//        }
//        return null; // File hợp lệ
//    }
//}


package org.example.action;

import org.example.service.RoomService;
import java.io.File;

public class CheckRoomSearch {

    // Kiểm tra dữ liệu tìm kiếm phòng
    public static String validateSearch(String tieuChi, String tuKhoa, String loaiPhong) {
        if (tieuChi == null || tieuChi.isEmpty()) {
            return "Vui lòng chọn tiêu chí tìm kiếm. Vui lòng tìm kiếm lại.";
        }

        if (!tieuChi.equals("Trạng thái phòng") && !tieuChi.equals("Mã phòng") &&
                !tieuChi.equals("Số phòng") && !tieuChi.equals("Giá phòng")) {
            return "Tiêu chí tìm kiếm không hợp lệ. Vui lòng tìm kiếm lại.";
        }

        if (loaiPhong == null || loaiPhong.isEmpty()) {
            return "Vui lòng chọn loại phòng. Vui lòng tìm kiếm lại.";
        }

        if (!loaiPhong.equals("Tất cả") && !loaiPhong.equals("Phòng đơn") &&
                !loaiPhong.equals("Phòng đôi") && !loaiPhong.equals("Phòng gia đình") &&
                !loaiPhong.equals("Phòng đặc biệt")) {
            return "Loại phòng không hợp lệ. Vui lòng tìm kiếm lại.";
        }

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return "Vui lòng nhập từ khóa tìm kiếm. Vui lòng tìm kiếm lại.";
        }

        if (tieuChi.equals("Trạng thái phòng")) {
            if (!tuKhoa.equals("Đang trống") && !tuKhoa.equals("Đang sử dụng")) {
                return "Trạng thái phòng phải là 'Đang trống' hoặc 'Đang sử dụng'. Vui lòng tìm kiếm lại.";
            }
        } else if (tieuChi.equals("Số phòng")) {
            if (!tuKhoa.matches("\\d+")) {
                return "Số phòng phải là số (ví dụ: 401). Vui lòng tìm kiếm lại.";
            }
        } else if (tieuChi.equals("Giá phòng")) {
            String cleanedTuKhoa = tuKhoa.replace(",", "").trim();
            if (cleanedTuKhoa.contains("-")) {
                String[] range = cleanedTuKhoa.split("-");
                if (range.length != 2) {
                    return "Định dạng khoảng giá không hợp lệ (ví dụ: 300000-500000). Vui lòng tìm kiếm lại.";
                }
                try {
                    double minPrice = Double.parseDouble(range[0].trim());
                    double maxPrice = Double.parseDouble(range[1].trim());
                    if (minPrice < 0 || maxPrice < 0) {
                        return "Giá phòng không được âm. Vui lòng tìm kiếm lại.";
                    }
                    if (minPrice > maxPrice) {
                        return "Giá tối thiểu phải nhỏ hơn hoặc bằng giá tối đa. Vui lòng tìm kiếm lại.";
                    }
                } catch (NumberFormatException e) {
                    return "Định dạng khoảng giá không hợp lệ (ví dụ: 300000-500000). Vui lòng tìm kiếm lại.";
                }
            } else {
                try {
                    double price = Double.parseDouble(cleanedTuKhoa);
                    if (price < 0) {
                        return "Giá phòng không được âm. Vui lòng tìm kiếm lại.";
                    }
                } catch (NumberFormatException e) {
                    return "Giá phòng phải là một số hợp lệ. Vui lòng tìm kiếm lại.";
                }
            }
        } else {
            if (tuKhoa.trim().length() > 50) {
                return "Từ khóa tìm kiếm không được dài quá 50 ký tự. Vui lòng tìm kiếm lại.";
            }
        }

        return null;
    }

    // Kiểm tra khi thêm phòng
    public static String validateAddRoom(String roomId, String roomNumber, String description,
                                         String type, String priceStr, RoomService roomService) {
        if (roomId == null || roomId.isEmpty() || roomNumber == null || roomNumber.isEmpty() ||
                description == null || description.isEmpty() || type == null || priceStr == null || priceStr.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin phòng.";
        }

        if (!roomId.matches("R\\d+")) {
            return "Mã phòng phải bắt đầu bằng 'R' và theo sau là số (ví dụ: R016).";
        }

        if (!roomNumber.matches("\\d+")) {
            return "Số phòng phải là số (ví dụ: 401).";
        }

        if (roomService.findRoomById(roomId) != null) {
            return "Mã phòng " + roomId + " đã tồn tại.";
        }

        if (!type.equals("Phòng đơn") && !type.equals("Phòng đôi") &&
                !type.equals("Phòng gia đình") && !type.equals("Phòng đặc biệt")) {
            return "Loại phòng không hợp lệ.";
        }

        try {
            double price = Double.parseDouble(priceStr.replace(",", ""));
            if (price < 0) {
                return "Giá phòng không được âm.";
            }
        } catch (NumberFormatException e) {
            return "Giá phòng phải là một số hợp lệ.";
        }

        return null;
    }

    // Kiểm tra khi sửa phòng
    public static String validateEditRoom(String roomId, String roomNumber, String description,
                                          String type, String priceStr, RoomService roomService) {
        if (roomId == null || roomId.isEmpty()) {
            return "Mã phòng không được để trống.";
        }

        if (roomService.findRoomById(roomId) == null) {
            return "Không tìm thấy phòng " + roomId + ".";
        }

        if (roomNumber == null || roomNumber.isEmpty() || description == null || description.isEmpty() ||
                type == null || priceStr == null || priceStr.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin phòng.";
        }

        if (!roomNumber.matches("\\d+")) {
            return "Số phòng phải là số (ví dụ: 401).";
        }

        if (!type.equals("Phòng đơn") && !type.equals("Phòng đôi") &&
                !type.equals("Phòng gia đình") && !type.equals("Phòng đặc biệt")) {
            return "Loại phòng không hợp lệ.";
        }

        try {
            double price = Double.parseDouble(priceStr.replace(",", ""));
            if (price < 0) {
                return "Giá phòng không được âm.";
            }
        } catch (NumberFormatException e) {
            return "Giá phòng phải là một số hợp lệ.";
        }

        return null;
    }

    // Kiểm tra khi xóa phòng
    public static String validateDeleteRoom(int selectedRow, String roomId, boolean isRoomInUse) {
        if (selectedRow == -1) {
            return "Vui lòng chọn một phòng để xóa!";
        }

        if (isRoomInUse) {
            return "Phòng " + roomId + " đang được đặt, không thể xóa!";
        }

        return null;
    }

    // Kiểm tra file bookings.xml
    public static String validateBookingsFile(String bookingFilePath) {
        File file = new File(bookingFilePath);
        if (!file.exists()) {
            return "Không tìm thấy file " + bookingFilePath + "!";
        }
        return null;
    }
}
