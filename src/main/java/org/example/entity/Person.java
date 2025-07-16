//package org.example.entity;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class Person {
//    private String name;
//    private String cccd;
//
//    public Person() {
//    }
//
//    public Person(String name, String cccd) {
//        this.name = name;
//        this.cccd = cccd;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
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


package org.example.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
    private String name;         // Họ tên
    private String loaiGiayTo;   // "CCCD", "Hộ chiếu", hoặc "Không có"
    private String maGiayTo;     // Mã giấy tờ (nếu có)

    public Person() {
    }

    public Person(String name, String loaiGiayTo, String maGiayTo) {
        this.name = name;
        this.loaiGiayTo = loaiGiayTo;
        this.maGiayTo = maGiayTo;
    }

    // === Getter & Setter ===

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", loaiGiayTo='" + loaiGiayTo + '\'' +
                ", maGiayTo='" + maGiayTo + '\'' +
                '}';
    }
}
