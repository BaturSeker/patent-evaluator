package com.patent.evaluator.controller;


import com.patent.evaluator.constant.SuccessMessages;
import com.patent.evaluator.dto.SetNewPasswordRequestDto;
import com.patent.evaluator.dto.SuccessResponseDto;
import com.patent.evaluator.dto.UserRequestDto;
import com.patent.evaluator.service.api.password.PasswordRules;
import com.patent.evaluator.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/rest/password/")
public class PasswordController {

    private PasswordRules passwordRules;

    @GetMapping(value = "getPassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = null;
        password = passwordGenerator.generate(10);
        while (!passwordRules.isPasswordValid(password)) {
            password = passwordGenerator.generate(10);
        }
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    @PostMapping(value = "validatePassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity validatePassword(@Valid @RequestBody UserRequestDto userRequestDto) {
        passwordRules.passwordValidation(userRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.VALIDATE_PASSWORD_TITLE, SuccessMessages.VALIDATE_PASSWORD_MESSAGE), HttpStatus.OK);

    }

    @PutMapping(value = "setNewPassword/{userId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity setNewPassword(@PathVariable Long userId, @Valid @RequestBody SetNewPasswordRequestDto setNewPasswordRequestDto) throws NoSuchAlgorithmException {
        setNewPasswordRequestDto.setUserId(userId);
        passwordRules.setNewPassword(setNewPasswordRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.NEW_PASSWORD_TITLE, SuccessMessages.NEW_PASSWORD_MESSAGE), HttpStatus.OK);
    }

    @GetMapping(value = "resetPassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity resetPassword(@RequestParam("userId") Long userId) throws NoSuchAlgorithmException {
        passwordRules.resetPassword(userId);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.RESET_PASSWORD_TITLE, SuccessMessages.RESET_PASSWORD_MESSAGE), HttpStatus.OK);
    }

    @Autowired
    public void setPasswordRules(PasswordRules passwordRules) {
        this.passwordRules = passwordRules;
    }
}

