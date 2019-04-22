package com.patent.evaluator.service.api;

import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.AuthorityRequest;
import com.patent.evaluator.dto.AuthorityResponse;
import com.patent.evaluator.dto.RoleAuthorityRequest;

import java.util.List;

public interface AuthorityRules {
    void save(AuthorityRequest authorityRequest);

    void update(Long authorityId, AuthorityRequest authorityRequest);

    Authority read(Long authorityId);

    List<Authority> readAll();

    void assignRoleAuthorities(RoleAuthorityRequest roleAuthorityRequest);

    List<Authority> getUserAuthorities(Users users) throws Exception;

    List<Authority> getAnonymousUserAuthorities();

    Authority findByAuthorityCode(String authorityCode);

    List<Authority> readAllAuthority();

    List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId);

    boolean shouldIconExist(Long authorityId);
}
