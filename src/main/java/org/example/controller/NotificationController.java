package org.example.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.example.entity.Notification;
import org.example.entity.NotificationXML;
import org.example.service.NotificationService;
import org.example.utils.FileUtils;

public class NotificationController {
    private static final String XML_PATH = "notifications.xml";

    public List<Notification> getUserNotifications(String username) {
        NotificationXML notificationXML = FileUtils.readFromFile(XML_PATH, NotificationXML.class);
        if (notificationXML == null || notificationXML.getNotifications() == null) return null;
        return notificationXML.getNotifications().stream()
                .filter(n -> username.equalsIgnoreCase(n.getUserName())
                        && ("Đã gửi".equalsIgnoreCase(n.getStatus()) || "Đã đọc".equalsIgnoreCase(n.getStatus())))
                .collect(Collectors.toList());
    }

    public void markNotificationAsRead(Notification notification) {
        NotificationService.updateStatus(
                notification.getBookingId(),
                notification.getRequestId(),
                notification.getUserName(),
                notification.getContent(),
                "Đã đọc"
        );
    }

    public void reloadNotifications(List<Notification> targetList, String username) {
        NotificationXML updatedXML = FileUtils.readFromFile(XML_PATH, NotificationXML.class);
        if (updatedXML != null) {
            targetList.clear();
            targetList.addAll(updatedXML.getNotifications().stream()
                    .filter(n -> username.equalsIgnoreCase(n.getUserName())
                            && ("Đã gửi".equalsIgnoreCase(n.getStatus()) || "Đã đọc".equalsIgnoreCase(n.getStatus())))
                    .collect(Collectors.toList()));
        }
    }
}