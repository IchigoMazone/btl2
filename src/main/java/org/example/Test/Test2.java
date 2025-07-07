package org.example.Test;


import java.time.LocalDateTime;

public class Test2 {
    public static void main(String[] args) {
        // Gửi yêu cầu mới
        SetupRequest.createRequest(
                "ivy", "Ivy Taylor", "ivy@example.com", "0987999888", "123456789000",
                "112",
                LocalDateTime.of(2025, 7, 6, 14, 0),
                LocalDateTime.of(2025, 7, 8, 12, 0),
                2000000.0
        );

        String requestId = "REQ9E823"; // ← sửa lại đúng với ID thực tế bạn đã tạo

        // Nếu không chắc ID, bạn có thể đọc file XML và lấy ID cuối cùng.

        SetupRequest.reviewRequest(requestId, "admin01", true); // duyệt
        // SetupRequest.reviewRequest(requestId, "admin01", false); // từ chối

        // ✅ 3. Cập nhật trạng thái khác (ví dụ No-show)
        SetupRequest.updateStatus(requestId, "Xác nhận no-show", "admin01");

        // ✅ 4. Cập nhật trạng thái check-in
        SetupRequest.updateStatus(requestId, "Check-in", "admin01");

        // ✅ 5. Cập nhật trạng thái check-out
        SetupRequest.updateStatus(requestId, "Check-out", "admin01");

        System.out.println("✅ Test hoàn tất!");
    }
}
