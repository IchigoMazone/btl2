package org.example.action;

import org.example.entity.UserInfo;
import org.example.service.UserInfoService;
import java.util.List;

public class CheckAccount {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static String checkForgotPassword(String fileName, String userName, String email) {
        if (userName.isEmpty() || email.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin.";
        }
        if (userName.length() < 8 || userName.length() > 20) {
            return "Tên đăng nhập phải từ 8–20 ký tự.";
        }
        if (!userName.matches("^[a-z0-9]+$")) {
            return "Tên đăng nhập chỉ được chứa chữ thường và số.";
        }
        if (!isValidEmail(email)) {
            return "Email không hợp lệ.";
        }

        List<UserInfo> list = UserInfoService.readAllUsers(fileName);
        if (list.isEmpty()) {
            return "Tài khoản hoặc email không chính xác.";
        }

        for (UserInfo info : list) {
            if (info.getUserName().equals(userName) && info.getEmail().equals(email)) {
                return null;
            }
        }
        return "Tài khoản hoặc email không chính xác.";
    }

    public static String validateNewPassword(String password, String confirmPassword) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin.";
        }
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự.";
        }
        if (!password.matches("^[a-z0-9]+$")) {
            return "Mật khẩu chỉ được chứa chữ thường và số.";
        }
        if (!password.matches(".*[a-z].*") || !password.matches(".*[0-9].*")) {
            return "Mật khẩu phải chứa cả chữ và số.";
        }
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu nhập lại không khớp.";
        }
        return null;
    }
}