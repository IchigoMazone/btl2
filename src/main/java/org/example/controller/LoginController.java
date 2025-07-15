package org.example.controller;

import org.example.action.CheckLogin;
import org.example.entity.User;
import org.example.view.LoginView;


public class LoginController {

    public static void handleLogin(LoginView view) {
        User user = view.getUser();
        String error = CheckLogin.checkUser("userinfos.xml", user.getUserName(), user.getPassword());

        if (user.getUserName().equals("admin") && user.getPassword().equals("123456")) {
            view.getMainFrame().showAdminContainerPanel(user.getUserName());
        }

        else if (error != null) {
            view.showError(error);
        }

        else {
            view.getMainFrame().showUserContainerPanel(user.getUserName());
        }
    }

    public static void openRegister(LoginView view) {
        view.getMainFrame().showRegisterPanel();
    }

    // Mở trang Quên mật khẩu
    public static void openForgotPassword(LoginView view) {
        view.getMainFrame().showForgotPasswordPanel();
    }
}