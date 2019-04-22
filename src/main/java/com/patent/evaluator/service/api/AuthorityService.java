package com.patent.evaluator.service.api;

import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.AuthorityPageDto;
import com.patent.evaluator.dto.AuthorityRequest;
import com.patent.evaluator.dto.AuthorityResponse;
import com.patent.evaluator.dto.RoleAuthorityRequest;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorityService {

    void save(AuthorityRequest authorityRequest);

    void update(Long authorityId, AuthorityRequest authorityRequest);

    Authority getAuthority(Long authorityId);

    List<Authority> readAll();

    void assignRoleAuthorities(RoleAuthorityRequest roleAuthorityRequest);

    List<Authority> getUserAuthorities(Users users);

    List<Authority> getAnonymousUserAuthorities();

    List<Authority> readAllAuthority();

    Page<AuthorityPageDto> getAll(PageableSearchFilterDto filterDto);

    List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId);
}
