package org.example.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.action.CheckAccount;
import org.example.service.UserInfoService;
import org.example.view.PasswordResetView;

public class PasswordResetController {

    public static void handlePasswordReset(PasswordResetView view) {
        String password = view.getPassword();
        String confirmPassword = view.getConfirmPassword();
        String username = view.getUsername();

        String error = CheckAccount.validateNewPassword(password, confirmPassword);
        if (error != null) {
            view.showError(error);
        } else {
            UserInfoService.updatePassword("userinfos.xml", username, DigestUtils.sha256Hex(password));
            view.clearFields();
            view.clearError();
            view.getMainFrame().showFinishPanel();
        }
    }
}
