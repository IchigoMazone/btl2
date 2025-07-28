
package org.example.service;

import org.example.entity.Notification;
import org.example.entity.NotificationXML;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private static final String FILE_PATH = "notifications.xml";

    public static void createNotification(String bookingId, String requestId, String userName, String content, String status) {
        NotificationXML notificationXML = FileUtils.readFromFile(FILE_PATH, NotificationXML.class);
        if (notificationXML == null) {
            notificationXML = new NotificationXML();
        }

        List<Notification> list = notificationXML.getNotifications();
        if (list == null) {
            list = new ArrayList<>();
        }

        Notification notification = new Notification(
                bookingId,
                requestId,
                userName,
                content,
                LocalDateTime.now(),
                status
        );

        list.add(0, notification);
        notificationXML.setNotifications(list);
        FileUtils.writeToFile(FILE_PATH, notificationXML);
    }


    public static void updateStatus(String bookingId, String requestId, String userName, String content, String newStatus) {
        NotificationXML notificationXML = FileUtils.readFromFile(FILE_PATH, NotificationXML.class);
        if (notificationXML == null || notificationXML.getNotifications() == null) {
            System.out.println("Không thể đọc dữ liệu từ file hoặc danh sách thông báo rỗng.");
            return;
        }

        List<Notification> list = notificationXML.getNotifications();

        for (Notification n : list) {
            if (n.getBookingId().equalsIgnoreCase(bookingId) &&
                    n.getRequestId().equalsIgnoreCase(requestId) &&
                    n.getUserName().equalsIgnoreCase(userName) &&
                    n.getContent().equalsIgnoreCase(content)) {
                n.setStatus(newStatus);
                FileUtils.writeToFile(FILE_PATH, notificationXML);
                System.out.println("Đã cập nhật trạng thái thông báo.");
                return;
            }
        }

        System.out.println("Không tìm thấy thông báo phù hợp với bookingId, requestId, userName và content.");
    }
}