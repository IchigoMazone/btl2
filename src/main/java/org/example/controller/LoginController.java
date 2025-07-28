package org.example.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.action.CheckLogin;
import org.example.entity.User;
import org.example.view.LoginView;

public class LoginController {
    private static String USERNAME = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";
    private static String PASSWORD = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";

    public static void handleLogin(LoginView view) {
        User user = view.getUser();
        String error = CheckLogin.checkUser("userinfos.xml", user.getUserName(), user.getPassword());

        if (DigestUtils.sha256Hex(user.getUserName()).equals(USERNAME)
                && DigestUtils.sha256Hex(user.getPassword()).equals(PASSWORD)) {
            view.getMainFrame().showAdminContainerPanel();
        } else if (error != null) {
            view.showError(error);
        } else {
            view.getMainFrame().showUserContainerPanel(user.getUserName());
        }
    }

    public static void openRegister(LoginView view) {
        view.getMainFrame().showRegisterPanel();
    }
    public static void openForgotPassword(LoginView view) {
        view.getMainFrame().showForgotPasswordPanel();
    }
}