package com.patent.evaluator.service.api;

import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.LoginRequest;

public interface LoginService {

    Users loggedIn(LoginRequest loginRequest) throws Exception;

    Users loggedInLDAP(LoginRequest loginRequest) throws Exception;

}
