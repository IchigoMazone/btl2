package org.example.entity;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;


@XmlRootElement(name = "UserInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfoXML {

    @XmlElement(name = "UserInfo")
    private List<UserInfo> userInfos;


    public UserInfoXML() {}

    public UserInfoXML(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}