package com.patent.evaluator.service.api.login;

import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.LoginRequestDto;

public interface LoginService {

    Users loggedIn(LoginRequestDto loginRequestDto) throws Exception;

    Users loggedInLDAP(LoginRequestDto loginRequestDto) throws Exception;

}
