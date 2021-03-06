package com.patent.evaluator.service.impl.login;


import com.patent.evaluator.constant.ValidationMessages;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.login.LoginRequestDto;
import com.patent.evaluator.service.api.login.LoginRules;
import com.patent.evaluator.service.api.login.LoginService;
import com.patent.evaluator.service.api.password.PasswordRules;
import com.patent.evaluator.util.ValidationHelper;
import com.patent.evaluator.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginRulesImpl implements LoginRules {

    private LoginService loginService;
    private PasswordRules passwordRules;

    @Override
    public Users login(LoginRequestDto loginRequestDto) throws Exception {

        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(loginRequestDto.getPassword())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_PASSWORD_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(loginRequestDto.getUsername())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_USER_NAME_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
        return loginService.loggedIn(loginRequestDto);
    }


    @Override
    public Users loginLDAP(LoginRequestDto loginRequestDto) throws Exception {

        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(loginRequestDto.getPassword())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_PASSWORD_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(loginRequestDto.getUsername())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_USER_NAME_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
        return loginService.loggedInLDAP(loginRequestDto);
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setPasswordRules(PasswordRules passwordRules) {
        this.passwordRules = passwordRules;
    }
}

