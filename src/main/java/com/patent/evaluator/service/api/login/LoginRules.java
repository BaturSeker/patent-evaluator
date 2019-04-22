package com.patent.evaluator.service.api.login;

import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.LoginRequestDto;

public interface LoginRules {

    Users login(LoginRequestDto loginRequestDto) throws Exception;

    Users loginLDAP(LoginRequestDto loginRequestDto) throws Exception;

}
