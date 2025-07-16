//package org.example.Test;
//
//import javax.xml.bind.annotation.*;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class Person {
//
//    private String fullName;
//    private String cccd;
//
//    // === Constructor không tham số (bắt buộc cho JAXB) ===
//    public Person() {}
//
//    // === Constructor có tham số ===
//    public Person(String fullName, String cccd) {
//        this.fullName = fullName;
//        this.cccd = cccd;
//    }
//
//    // === Getter & Setter ===
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public String getCccd() {
//        return cccd;
//    }
//
//    public void setCccd(String cccd) {
//        this.cccd = cccd;
//    }
//}


package org.example.Test;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Person {

    private String fullName;
    private String loaiGiayTo;  // "CCCD", "Hộ chiếu", "Không có"
    private String maGiayTo;    // Mã số giấy tờ (nếu có)

    // === Constructor không tham số (bắt buộc cho JAXB) ===
    public Person() {}

    // === Constructor có tham số ===
    public Person(String fullName, String loaiGiayTo, String maGiayTo) {
        this.fullName = fullName;
        this.loaiGiayTo = loaiGiayTo;
        this.maGiayTo = maGiayTo;
    }

    // === Getter & Setter ===
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLoaiGiayTo() {
        return loaiGiayTo;
    }

    public void setLoaiGiayTo(String loaiGiayTo) {
        this.loaiGiayTo = loaiGiayTo;
    }

    public String getMaGiayTo() {
        return maGiayTo;
    }

    public void setMaGiayTo(String maGiayTo) {
        this.maGiayTo = maGiayTo;
    }

    @Override
    public String toString() {
        return "Person{" +
                "fullName='" + fullName + '\'' +
                ", loaiGiayTo='" + loaiGiayTo + '\'' +
                ", maGiayTo='" + maGiayTo + '\'' +
                '}';
    }
}
