package org.example.entity;

import org.example.utils.LocalDateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlRootElement(name = "Notification")
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification {

    private String bookingId;
    private String requestId;
    private String userName;
    private String content;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime time;

    private String status;

    public Notification() {
    }

    public Notification(String bookingId, String requestId, String userName, String content, LocalDateTime time, String status) {
        this.bookingId = bookingId;
        this.requestId = requestId;
        this.userName = userName;
        this.content = content;
        this.time = time;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
