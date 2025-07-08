package org.example.Test;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class HistoryEntry {

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime timestamp;

    private String status;

    // === Constructor không tham số (bắt buộc cho JAXB) ===
    public HistoryEntry() {}

    // === Constructor có tham số ===
    public HistoryEntry(LocalDateTime timestamp, String status) {
        this.timestamp = timestamp;
        this.status = status;
    }

    // === Getter & Setter ===
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
