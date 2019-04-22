package com.patent.evaluator.controller;


import com.patent.evaluator.config.ActiveDirectoryHelper;
import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.AuthorityResponse;
import com.patent.evaluator.dto.LoginRequest;
import com.patent.evaluator.dto.LoginResponse;
import com.patent.evaluator.dto.LoginUserResponse;
import com.patent.evaluator.security.JwtUtil;
import com.patent.evaluator.service.api.AuthorityListRules;
import com.patent.evaluator.service.api.AuthorityRules;
import com.patent.evaluator.service.api.LoginRules;
import com.patent.evaluator.util.ClearSession;
import com.patent.evaluator.util.string.StringAppenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rest/")
public class LoginController {
    private LoginRules loginRules;
    private AuthorityListRules authorityListRules;
    private AuthorityRules authorityRules;
    private JwtUtil jwtUtil;
    private ActiveDirectoryHelper activeDirectoryHelper;


    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) throws Exception {

        if (activeDirectoryHelper.getLdapConfig().getEnabled()) {
            if (activeDirectoryHelper.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
                Users user = loginRules.loginLDAP(loginRequest);
                authorityListRules.authorize(user);
                List<Authority> authorities = this.authorityRules.getUserAuthorities(user);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setLoginUserResponse(getUserResponse(user));
                loginResponse.setAuthorityResponse(getAuthorityResponse(authorities));

                HttpHeaders headers = new HttpHeaders();
                String token = jwtUtil.generateTokenWithId(user);
                headers.set("Authorization", StringAppenderUtil.append("Bearer ", token));
                loginResponse.setToken(token);
                return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK);
            } else {
                LoginResponse loginResponse = new LoginResponse();
                return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
            }

        } else {
            Users user = loginRules.login(loginRequest);
            authorityListRules.authorize(user);
            List<Authority> authorities = this.authorityRules.getUserAuthorities(user);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setLoginUserResponse(getUserResponse(user));
            loginResponse.setAuthorityResponse(getAuthorityResponse(authorities));

            HttpHeaders headers = new HttpHeaders();
            String token = jwtUtil.generateTokenWithId(user);
            headers.set("Authorization", StringAppenderUtil.append("Bearer ", token));
            loginResponse.setToken(token);
            return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK);
        }

    }


    @GetMapping("getAnonymousUserAuthorities")
    public ResponseEntity getCustomerUserAuthorities() {
        List<Authority> authorities = this.authorityRules.getAnonymousUserAuthorities();
        List<AuthorityResponse> authorityResponse = getAuthorityResponse(authorities);
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
    }

    @PostMapping(value = "logout")
    public ResponseEntity logout() {

        SecurityContextHolder.getContext().setAuthentication(null);
        HttpStatus responseCode;

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        responseCode = new ClearSession().clear(attr);

        return new ResponseEntity<>(true, responseCode);
    }

    private LoginUserResponse getUserResponse(Users user) {
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setUserId(user.getId());
        loginUserResponse.setSurname(user.getSurname());
        loginUserResponse.setName(user.getFirstname());
        loginUserResponse.setUsername(user.getUsername());
        loginUserResponse.setPasswordTemporary(user.getPasswordTemporary());

        return loginUserResponse;
    }

    private List<AuthorityResponse> getAuthorityResponse(List<Authority> authorities) {
        List<AuthorityResponse> authorityResponses = new ArrayList<>();
        for (Authority authority : authorities) {
            AuthorityResponse authorityResponse = new AuthorityResponse();
            if (!authority.getAuthorities().isEmpty()) {
                List<AuthorityResponse> newAuthorityResponses = getAuthorityResponse(new ArrayList<>(authority.getAuthorities()));
                authorityResponse.setAuthorities(newAuthorityResponses);
            }
            authorityResponse.setUrl(authority.getUrl());
            authorityResponse.setAuthorityId(authority.getId());
            authorityResponse.setTitle(authority.getTitle());
            authorityResponses.add(authorityResponse);
            authorityResponse.setMenu(authority.getMenu());
            authorityResponse.setIcon(authority.getIcon());
            authorityResponse.setAuthorityCode(authority.getAuthorityCode());
        }
        return authorityResponses;
    }

    @Autowired
    public void setLoginRules(LoginRules loginRules) {
        this.loginRules = loginRules;
    }

    @Autowired
    public void setAuthorityListRules(AuthorityListRules authorityListRules) {
        this.authorityListRules = authorityListRules;
    }

    @Autowired
    public void setAuthorityRules(AuthorityRules authorityRules) {
        this.authorityRules = authorityRules;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setActiveDirectoryHelper(ActiveDirectoryHelper activeDirectoryHelper) {
        this.activeDirectoryHelper = activeDirectoryHelper;
    }
}

