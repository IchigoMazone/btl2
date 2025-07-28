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

public class RequestResetController {
    public static void updateExpiredRequests() {
        RequestXML requestXML = FileUtils.readFromFile("requests.xml", RequestXML.class);
        List<Request> allRequests = requestXML.getRequests();

        for (Request req : allRequests) {
            if (req.getStatus() == null || "Hết hiệu lực".equalsIgnoreCase(req.getStatus().trim())) {
                continue;
            }

            String requestStatus = req.getStatus().trim();
            if (!("Đã đọc".equalsIgnoreCase(requestStatus) ||
                    "Gửi yêu cầu".equalsIgnoreCase(requestStatus) ||
                    "Gửi yêu cầu hủy".equalsIgnoreCase(requestStatus))) {
                continue;
            }


            if (req.getCheckIn() == null) {
                continue;
            }

            long minutesUntilCheckIn = Duration.between(LocalDateTime.now(), req.getCheckIn()).toMinutes();

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
                    HistoryEntry latestHistory = histories.get(histories.size() - 1);
                    if (latestHistory.getStatus() != null) {
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