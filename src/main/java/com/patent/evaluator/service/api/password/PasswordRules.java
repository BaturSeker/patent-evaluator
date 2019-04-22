package com.patent.evaluator.service.api.password;

import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.password.SetNewPasswordRequestDto;
import com.patent.evaluator.dto.user.UserRequestDto;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PasswordRules {
    void passwordValidation(UserRequestDto userRequestDto);

    boolean isPasswordValid(String password);

    boolean isUsernamePasswordValid(String userName, String password);

    void setIsTemproraryPassword(Users users) throws NoSuchAlgorithmException;

    void setIsTemproraryPassword(List<Users> users) throws NoSuchAlgorithmException;

    void setNewPassword(SetNewPasswordRequestDto setNewPasswordRequestDto) throws NoSuchAlgorithmException;

    void resetPassword(Long userId) throws NoSuchAlgorithmException;

    void basePasswordRules(String password);

    void checkSlangWord(String password);
}
