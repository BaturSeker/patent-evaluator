package com.patent.evaluator.service.api.password;

import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.SetNewPasswordRequest;
import com.patent.evaluator.dto.UserRequest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PasswordRules {
    void passwordValidation(UserRequest userRequest);

    boolean isPasswordValid(String password);

    boolean isUsernamePasswordValid(String userName, String password);

    void setIsTemproraryPassword(Users users) throws NoSuchAlgorithmException;

    void setIsTemproraryPassword(List<Users> users) throws NoSuchAlgorithmException;

    void setNewPassword(SetNewPasswordRequest setNewPasswordRequest) throws NoSuchAlgorithmException;

    void resetPassword(Long userId) throws NoSuchAlgorithmException;

    void basePasswordRules(String password);

    void checkSlangWord(String password);
}
