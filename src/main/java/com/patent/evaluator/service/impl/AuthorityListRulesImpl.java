package com.patent.evaluator.service.impl;

import com.patent.evaluator.domain.*;
import com.patent.evaluator.service.api.AuthorityListRules;
import com.patent.evaluator.service.api.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityListRulesImpl implements AuthorityListRules {

    private AuthorizationService authorizationService;

    private static Logger LOGGER = LoggerFactory.getLogger(AuthorityListRulesImpl.class);

    @Override
    public List<UserRole> getAuthorizeList(Long userId) {

        if (userId != null) {
            List<UserRole> userRoleElem = authorizationService.getAuthorizeList(userId);
            if (userRoleElem != null) {
                return userRoleElem;
            }
        }
        return null;
    }

    @Override
    public void authorize(Users user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        List<UserRole> userRoleList = getAuthorizeList(user.getId());

        for (UserRole userRole : userRoleList) {
            Roles role = userRole.getRole();
            List<RoleAuthority> roleAuthorities = role.getRoleAuthorities();
            if (roleAuthorities != null) {
                for (RoleAuthority ra : roleAuthorities) {
                    Authority authority = ra.getAuthority();
                    authorities.add(new SimpleGrantedAuthority(authority.getAuthorityCode()));
                }
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, "N/A",
                authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
}
