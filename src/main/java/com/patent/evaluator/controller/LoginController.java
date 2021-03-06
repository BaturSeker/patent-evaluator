package com.patent.evaluator.controller;


import com.patent.evaluator.config.ActiveDirectoryHelper;
import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.authority.AuthorityResponse;
import com.patent.evaluator.dto.login.LoginRequestDto;
import com.patent.evaluator.dto.login.LoginResponseDto;
import com.patent.evaluator.dto.login.LoginUserResponseDto;
import com.patent.evaluator.security.JwtUtil;
import com.patent.evaluator.service.api.authority.AuthorityListRules;
import com.patent.evaluator.service.api.authority.AuthorityRules;
import com.patent.evaluator.service.api.login.LoginRules;
import com.patent.evaluator.util.ClearSession;
import com.patent.evaluator.util.string.StringAppenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {

        if (activeDirectoryHelper.getLdapConfig().getEnabled()) {
            if (activeDirectoryHelper.authenticate(loginRequestDto.getUsername(), loginRequestDto.getPassword())) {
                Users user = loginRules.loginLDAP(loginRequestDto);
                authorityListRules.authorize(user);
                List<Authority> authorities = this.authorityRules.getUserAuthorities(user);
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setLoginUserResponseDto(getUserResponse(user));
                loginResponseDto.setAuthorityResponse(getAuthorityResponse(authorities));

                HttpHeaders headers = new HttpHeaders();
                String token = jwtUtil.generateTokenWithId(user);
                headers.set("Authorization", StringAppenderUtil.append("Bearer ", token));
                loginResponseDto.setToken(token);
                return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
            } else {
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                return new ResponseEntity<>(loginResponseDto, HttpStatus.UNAUTHORIZED);
            }

        } else {
            Users user = loginRules.login(loginRequestDto);
            authorityListRules.authorize(user);
            List<Authority> authorities = this.authorityRules.getUserAuthorities(user);
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setLoginUserResponseDto(getUserResponse(user));
            loginResponseDto.setAuthorityResponse(getAuthorityResponse(authorities));

            HttpHeaders headers = new HttpHeaders();
            String token = jwtUtil.generateTokenWithId(user);
            headers.set("Authorization", StringAppenderUtil.append("Bearer ", token));
            loginResponseDto.setToken(token);
            return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
        }

    }


    @GetMapping("getAnonymousUserAuthorities")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getCustomerUserAuthorities() {
        List<Authority> authorities = this.authorityRules.getAnonymousUserAuthorities();
        List<AuthorityResponse> authorityResponse = getAuthorityResponse(authorities);
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
    }

    @PostMapping(value = "logout")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity logout() {

        SecurityContextHolder.getContext().setAuthentication(null);
        HttpStatus responseCode;

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        responseCode = new ClearSession().clear(attr);

        return new ResponseEntity<>(true, responseCode);
    }

    private LoginUserResponseDto getUserResponse(Users user) {
        LoginUserResponseDto loginUserResponseDto = new LoginUserResponseDto();
        loginUserResponseDto.setUserId(user.getId());
        loginUserResponseDto.setSurname(user.getSurname());
        loginUserResponseDto.setName(user.getFirstname());
        loginUserResponseDto.setUsername(user.getUsername());
        loginUserResponseDto.setPasswordTemporary(user.getPasswordTemporary());

        return loginUserResponseDto;
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

