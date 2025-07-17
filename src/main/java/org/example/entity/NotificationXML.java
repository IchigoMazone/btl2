package org.example.entity;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Notifications")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificationXML {

    @XmlElement(name = "Notification")
    private List<Notification> notifications = new ArrayList<>();

    public NotificationXML() {}

    public NotificationXML(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}

