package com.patent.evaluator.dto;

import java.util.List;

public class LoginResponse {
    private LoginUserResponse loginUserResponse;
    private List<AuthorityResponse> authorityResponse;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AuthorityResponse> getAuthorityResponse() {
        return authorityResponse;
    }

    public void setAuthorityResponse(List<AuthorityResponse> authorityResponse) {
        this.authorityResponse = authorityResponse;
    }

    public LoginUserResponse getLoginUserResponse() {
        return loginUserResponse;
    }

    public void setLoginUserResponse(LoginUserResponse loginUserResponse) {
        this.loginUserResponse = loginUserResponse;
    }
}
