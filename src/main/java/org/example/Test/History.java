package org.example.Test;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlAccessorType(XmlAccessType.FIELD)
public class History {

    private String type;
    private String by;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime time;

    // Getters và Setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }
    public void setBy(String by) {
        this.by = by;
    }

    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
