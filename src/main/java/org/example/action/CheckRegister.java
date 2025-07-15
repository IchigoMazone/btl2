package org.example.action;

import org.example.entity.UserInfo;
import org.example.service.UserInfoService;
import java.util.List;

public class CheckRegister {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static String validateFullName(String fullName) {
        if (fullName.length() < 8) {
            return "Họ tên phải có ít nhất 8 ký tự.";
        }
        if (!fullName.matches("^[a-zA-Z ]+$")) {
            return "Họ tên chỉ được chứa chữ cái và khoảng trắng.";
        }
        return null;
    }

    public static String validateUsername(String userName) {
        if (userName.length() < 8 || userName.length() > 20) {
            return "Tên đăng nhập phải từ 8–20 ký tự.";
        }
        if (!userName.matches("^[a-z0-9]+$")) {
            return "Tên đăng nhập chỉ được chứa chữ thường và số.";
        }
        if (!userName.matches(".*[a-z].*") || !userName.matches(".*[0-9].*")) {
            return "Tên đăng nhập phải chứa cả chữ và số.";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự.";
        }
        if (!password.matches("^[a-z0-9]+$")) {
            return "Mật khẩu chỉ được chứa chữ thường và số.";
        }
        if (!password.matches(".*[a-z].*") || !password.matches(".*[0-9].*")) {
            return "Mật khẩu phải chứa cả chữ và số.";
        }
        return null;
    }

    public static String validateConfirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu nhập lại không khớp.";
        }
        return null;
    }

    public static String validateEmail(String email) {
        if (!isValidEmail(email)) {
            return "Email không hợp lệ.";
        }
        return null;
    }

    public static String validatePhone(String phone) {
        if (!phone.matches("\\d+")) {
            return "Số điện thoại chỉ được chứa chữ số.";
        }
        if (!phone.matches("0\\d{9}")) {
            return "Số điện thoại phải có 10 số và bắt đầu bằng 0.";
        }
        return null;
    }

    public static String usernameExists(String userName) {
        List<UserInfo> list = UserInfoService.readAllUsers("userinfos.xml");
        for (UserInfo info : list) {
            if (info.getUserName().equals(userName)) {
                return "Tài khoản đã tồn tại.";
            }
        }
        return null;
    }

    public static String checkRegis(
            String fullName,
            String userName,
            String password,
            String email,
            String phone,
            String confirmPassword
    ) {

        if (fullName.isEmpty() ||
                userName.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty() ||
                email.isEmpty() ||
                phone.isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin!";
        }

        String nameError = validateFullName(fullName);
        if (nameError != null) {
            return nameError;
        }

        String userError = validateUsername(userName);
        if (userError != null) {
            return userError;
        }

        String userExists = usernameExists(userName);
        if (userExists != null) {
            return userExists;
        }

        String passError = validatePassword(password);
        if (passError != null) {
            return passError;
        }

        String emailError = validateEmail(email);
        if (emailError != null) {
            return emailError;
        }

        String phoneError = validatePhone(phone);
        if (phoneError != null) {
            return phoneError;
        }

        String confirmError = validateConfirmPassword(password, confirmPassword);
        if (confirmError != null) {
            return confirmError;
        }

        return null;
    }
}