package org.example.controller;

import org.example.entity.HistoryEntry;
import org.example.entity.Request;
import org.example.entity.RequestXML;
import org.example.service.RequestService;
import org.example.utils.FileUtils;
import org.example.service.NotificationService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RequestResetController {
    public static void updateExpiredRequests() {
        RequestXML requestXML = FileUtils.readFromFile("requests.xml", RequestXML.class);
        List<Request> allRequests = requestXML.getRequests();

        for (Request req : allRequests) {
            // Kiểm tra trạng thái của Request
            if (req.getStatus() == null || "Hết hiệu lực".equalsIgnoreCase(req.getStatus().trim())) {
                continue;
            }

            // Chỉ xử lý các trạng thái được yêu cầu
            String requestStatus = req.getStatus().trim();
            if (!("Đã đọc".equalsIgnoreCase(requestStatus) ||
                    "Gửi yêu cầu".equalsIgnoreCase(requestStatus) ||
                    "Gửi yêu cầu hủy".equalsIgnoreCase(requestStatus))) {
                continue;
            }

            // Kiểm tra checkIn để tránh NullPointerException
            if (req.getCheckIn() == null) {
                continue;
            }

            // Tính thời gian từ hiện tại đến check-in
            long minutesUntilCheckIn = Duration.between(LocalDateTime.now(), req.getCheckIn()).toMinutes();

            // Xử lý dựa trên trạng thái của Request
            if ("Gửi yêu cầu".equalsIgnoreCase(requestStatus) && minutesUntilCheckIn < 180) {
                RequestService.updateStatus(req.getRequestId(), "Hết hiệu lực");
                NotificationService.createNotification(
                        "BK00000001",
                        req.getRequestId(),
                        req.getUserName(),
                        "Đã bị hủy",
                        "Đã gửi"
                );
            } else if ("Gửi yêu cầu hủy".equalsIgnoreCase(requestStatus) && minutesUntilCheckIn < 60) {
                RequestService.updateStatus(req.getRequestId(), "Hết hiệu lực");
                NotificationService.createNotification(
                        "BK00000001",
                        req.getRequestId(),
                        req.getUserName(),
                        "Đã bị hủy",
                        "Đã gửi"
                );
            } else if ("Đã đọc".equalsIgnoreCase(requestStatus)) {
                List<HistoryEntry> histories = req.getHistory();
                if (histories != null && !histories.isEmpty()) {
                    // Lấy lịch sử gần nhất (entry cuối cùng)
                    HistoryEntry latestHistory = histories.get(histories.size() - 1);
                    if (latestHistory.getStatus() != null) {
                        // Kiểm tra trạng thái trước đó (nếu có)
                        if (histories.size() > 1) {
                            HistoryEntry previous = histories.get(histories.size() - 2);
                            String previousStatus = previous.getStatus() != null ? previous.getStatus().trim() : "";
                            if ("Gửi yêu cầu".equalsIgnoreCase(previousStatus) && minutesUntilCheckIn < 180) {
                                RequestService.updateStatus(req.getRequestId(), "Hết hiệu lực");
                                NotificationService.createNotification(
                                        "BK00000001",
                                        req.getRequestId(),
                                        req.getUserName(),
                                        "Đã bị hủy",
                                        "Đã gửi"
                                );
                            } else if ("Gửi yêu cầu hủy".equalsIgnoreCase(previousStatus) && minutesUntilCheckIn < 60) {
                                RequestService.updateStatus(req.getRequestId(), "Hết hiệu lực");
                                NotificationService.createNotification(
                                        "BK0000000X",
                                        req.getRequestId(),
                                        req.getUserName(),
                                        "Đã bị hủy",
                                        "Đã gửi"
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}