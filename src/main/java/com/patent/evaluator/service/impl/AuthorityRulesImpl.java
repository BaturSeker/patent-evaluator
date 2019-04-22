package com.patent.evaluator.service.impl;


import com.patent.evaluator.constant.ValidationMessages;
import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.AuthorityRequest;
import com.patent.evaluator.dto.AuthorityResponse;
import com.patent.evaluator.dto.RoleAuthorityRequest;
import com.patent.evaluator.service.api.AuthorityRules;
import com.patent.evaluator.service.api.AuthorityService;
import com.patent.evaluator.util.ValidationHelper;
import com.patent.evaluator.util.exception.NullObjectException;
import com.patent.evaluator.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthorityRulesImpl implements AuthorityRules {

    private AuthorityService authorityService;

    @Override
    public void save(AuthorityRequest authorityRequest) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!Objects.nonNull(authorityRequest.getTitle())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_TITLE_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequest.getIcon())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_ICON_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequest.getUrl())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_URL_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequest.getAuthorizeCode())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_AUTHORIZE_CODE_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequest.getMenu())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_IS_MENU_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequest.getVisible())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_IS_VISIBLE_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        authorityService.save(authorityRequest);
    }

    @Override
    public void update(Long authorityId, AuthorityRequest authorityRequest) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!Objects.nonNull(authorityRequest)) {
            messages.append(ValidationMessages.AUTHORITY_NOT_NULL);
            messages.append(System.lineSeparator());
            throw new ValidationException(messages.toString());
        }
        if (shouldIconExist(authorityId)) {
            if (!ValidationHelper.isValid(authorityRequest.getIcon())) {
                isValid = false;
                messages.append(ValidationMessages.AUTHORITY_ICON_NOT_NULL);
                messages.append(System.lineSeparator());
            }
        }
        if (!ValidationHelper.isValid(authorityId)) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        authorityService.update(authorityId, authorityRequest);
    }

    @Override
    public Authority read(Long authorityId) {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.isValid(authorityId)) {
            isValid = false;
            message.append(ValidationMessages.AUTHORITY_ID_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
        return authorityService.getAuthority(authorityId);
    }

    @Override
    public List<Authority> readAll() throws NullObjectException {
        return authorityService.readAll();
    }

    @Override
    public void assignRoleAuthorities(RoleAuthorityRequest roleAuthorityRequest) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.isValid(roleAuthorityRequest.getRoleId())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.isValid(roleAuthorityRequest.getAuthorityIds())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_ID_LIST_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        authorityService.assignRoleAuthorities(roleAuthorityRequest);
    }

    @Override
    public List<Authority> getUserAuthorities(Users users) {
        return this.authorityService.getUserAuthorities(users);
    }


    @Override
    public List<Authority> getAnonymousUserAuthorities() {
        return this.authorityService.getAnonymousUserAuthorities();
    }

    @Override
    public Authority findByAuthorityCode(String authorityCode) {
        return null;
    }

    @Override
    public List<Authority> readAllAuthority() {
        return this.authorityService.readAllAuthority();
    }

    @Override
    public List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId) {
        return this.authorityService.findAuthoritiesByRoleId(roleId);
    }

    @Override
    public boolean shouldIconExist(Long authorityId) {
        Authority authority = read(authorityId);
        if (authority.getParentAuthority() != null) {
            Authority parent = authority.getParentAuthority();
            if (parent.getParentAuthority() == null) {
                return true;
            }
        }
        return false;
    }

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }
}

