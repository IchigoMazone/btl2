package org.example.action;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.entity.UserInfo;
import org.example.service.UserInfoService;
import java.util.List;

public class CheckLogin {
    public static String validateUsername(String userName) {
        if (userName.length() < 8 || userName.length() > 20) {
            return "Tên đăng nhập phải từ 8–20 ký tự.";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự.";
        }
        return null;
    }

    public static String checkUser(String fileName, String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin!";
        }

        String userError = validateUsername(userName);
        if (userError != null) {
            return userError;
        }

        String passError = validatePassword(password);
        if (passError != null) {
            return passError;
        }

        List<UserInfo> list = UserInfoService.readAllUsers(fileName);
        if (list.isEmpty()) {
            return "Tài khoản hoặc mật khẩu không chính xác.";
        }

        for (UserInfo info : list) {
            if (info.getUserName().equals(userName)
                    && info.getPassword().equals(DigestUtils.sha256Hex(password))) {
                return null;
            }
        }
        return "Tài khoản hoặc mật khẩu không chính xác.";
    }
}