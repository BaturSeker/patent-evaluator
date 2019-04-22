package com.patent.evaluator.service.api.authority;

import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.AuthorityRequestDto;
import com.patent.evaluator.dto.AuthorityResponse;
import com.patent.evaluator.dto.RoleAuthorityRequestDto;

import java.util.List;

public interface AuthorityRules {
    void save(AuthorityRequestDto authorityRequestDto);

    void update(Long authorityId, AuthorityRequestDto authorityRequestDto);

    Authority read(Long authorityId);

    List<Authority> readAll();

    void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto);

    List<Authority> getUserAuthorities(Users users) throws Exception;

    List<Authority> getAnonymousUserAuthorities();

    Authority findByAuthorityCode(String authorityCode);

    List<Authority> readAllAuthority();

    List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId);

    boolean shouldIconExist(Long authorityId);
}
