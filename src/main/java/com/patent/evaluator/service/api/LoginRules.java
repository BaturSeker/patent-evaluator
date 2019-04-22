package com.patent.evaluator.service.api;

import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.LoginRequest;

public interface LoginRules {

    Users login(LoginRequest loginRequest) throws Exception;

    Users loginLDAP(LoginRequest loginRequest) throws Exception;

}
