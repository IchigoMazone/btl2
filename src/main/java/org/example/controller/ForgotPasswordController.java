package org.example.controller;

import org.example.action.CheckAccount;
import org.example.view.ForgotPasswordView;

public class ForgotPasswordController {

    public static void handleForgotPassword(ForgotPasswordView view) {
        String username = view.getUsername();
        String email = view.getEmail();

        String error = CheckAccount.checkForgotPassword("userinfos.xml", username, email);

        if (error != null) {
            view.showError(error);
        } else {
            view.getMainFrame().showPasswordResetPanel(username);
        }
    }
}
