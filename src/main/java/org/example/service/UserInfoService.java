package org.example.service;

import org.example.entity.UserInfo;
import org.example.entity.UserInfoXML;
import org.example.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class UserInfoService {

    public static void saveUser(UserInfo newUser, String fileName) {
        UserInfoXML data = (UserInfoXML) FileUtils.readFromFile(fileName, UserInfoXML.class);
        List<UserInfo> list;
        if (data == null || data.getUserInfos() == null) {
            list = new ArrayList<>();
        } else {
            list = data.getUserInfos();
        }
        list.add(0, newUser);
        UserInfoXML newData = new UserInfoXML();
        newData.setUserInfos(list);
        FileUtils.writeToFile(fileName, newData);
    }

    public static List<UserInfo> readAllUsers(String fileName) {
        UserInfoXML data = (UserInfoXML) FileUtils.readFromFile(fileName, UserInfoXML.class);
        if (data != null && data.getUserInfos() != null) {
            return data.getUserInfos();
        } else {
            return new ArrayList<>();
        }
    }

    public static void updatePassword(String fileName, String userName, String newPassword) {
        UserInfoXML data = (UserInfoXML) FileUtils.readFromFile(fileName, UserInfoXML.class);
        if (data != null && data.getUserInfos() != null) {
            for (UserInfo user : data.getUserInfos()) {
                if (user.getUserName().equals(userName)) {
                    user.setPassword(newPassword);
                    break;
                }
            }
            FileUtils.writeToFile(fileName, data);
        }
    }
    public static UserInfo getUserByUsername(String fileName, String username) {
        List<UserInfo> users = readAllUsers(fileName);
        for (UserInfo user : users) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static String getEmail(String fileName, String userName) {
        List<UserInfo> list = readAllUsers(fileName);
        for (UserInfo user : list) {
            if (user.getUserName().equals(userName)) {
                return user.getEmail();
            }
        }
        return "";
    }

    public static String getPhone(String fileName, String userName) {
        List<UserInfo> list = readAllUsers(fileName);
        for (UserInfo user : list) {
            if (user.getUserName().equals(userName)) {
                return user.getPhoneNumber();
            }
        }
        return "";
    }

    public static String getFullName(String fileName, String userName) {
        List<UserInfo> list = readAllUsers(fileName);
        for (UserInfo user : list) {
            if (user.getUserName().equals(userName)) {
                return user.getFullName();
            }
        }
        return "";
    }

}