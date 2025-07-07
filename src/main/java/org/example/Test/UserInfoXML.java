package org.example.Test;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "UserInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfoXML {

    @XmlElement(name = "UserInfo")
    private List<UserInfo> users = new ArrayList<>();

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }
}
