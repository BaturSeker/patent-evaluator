package com.patent.evaluator.service.api.authority;

import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.authority.AuthorityPageDto;
import com.patent.evaluator.dto.authority.AuthorityRequestDto;
import com.patent.evaluator.dto.authority.AuthorityResponse;
import com.patent.evaluator.dto.authority.RoleAuthorityRequestDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorityService {

    void save(AuthorityRequestDto authorityRequestDto);

    void update(Long authorityId, AuthorityRequestDto authorityRequestDto);

    Authority getAuthority(Long authorityId);

    List<Authority> readAll();

    void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto);

    List<Authority> getUserAuthorities(Users users);

    List<Authority> getAnonymousUserAuthorities();

    List<Authority> readAllAuthority();

    Page<AuthorityPageDto> getAll(PageableSearchFilterDto filterDto);

    List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId);
}
