package com.patent.evaluator.service.impl;


import com.patent.evaluator.constant.ExceptionMessages;
import com.patent.evaluator.dao.AuthorityRepository;
import com.patent.evaluator.dao.RoleAuthorityRepository;
import com.patent.evaluator.dao.RoleRepository;
import com.patent.evaluator.domain.*;
import com.patent.evaluator.dto.AuthorityPageDto;
import com.patent.evaluator.dto.AuthorityRequest;
import com.patent.evaluator.dto.AuthorityResponse;
import com.patent.evaluator.dto.RoleAuthorityRequest;
import com.patent.evaluator.pageablesearch.model.BaseSortRequest;
import com.patent.evaluator.pageablesearch.model.PageRequestDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import com.patent.evaluator.pageablesearch.specification.SearchSpecificationBuilder;
import com.patent.evaluator.service.api.AuthorityService;
import com.patent.evaluator.util.CalendarHelper;
import com.patent.evaluator.util.exception.NullObjectException;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private AuthorityRepository authorityRepository;
    private RoleAuthorityRepository roleAuthorityRepository;
    private RoleRepository roleRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Override
    public void save(AuthorityRequest authorityRequest) {
        Authority authority = new Authority();
        authority.setParentAuthority(authorityRepository.getOne(authorityRequest.getParentId()));
        authority.setCreatedDate(CalendarHelper.getCurrentInstant());
        authority.setMenu(authorityRequest.getMenu());
        authority.setAuthorityCode(authorityRequest.getAuthorizeCode());
        authority.setTitle(authorityRequest.getTitle());
        authority.setUrl(authorityRequest.getUrl());
        authority.setVisible(authorityRequest.getVisible());
        authority.setIcon(authorityRequest.getIcon());
        authorityRepository.save(authority);
    }

    @Override
    public void update(Long authorityId, AuthorityRequest authorityRequest) {
        Authority authority = authorityRepository.getOne(authorityId);

        if (Objects.equals(authority, null)) {
            throw new NullObjectException(ExceptionMessages.AUTHORITY_NULL);
        }
        //this.cancelRoleAuthorityRelation(authorityId);
        authority.setTitle(authorityRequest.getTitle());
        authority.setIcon(authorityRequest.getIcon());
        authorityRepository.save(authority);
    }

    private void cancelRoleAuthorityRelation(Long authorityId) {
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findAllByAuthority(authorityRepository.getOne(authorityId));
        for (RoleAuthority roleAuthority : roleAuthorities) {
            roleAuthorityRepository.delete(roleAuthority);
        }
    }

    @Override
    public Authority getAuthority(Long authorityId) {
        Authority role = authorityRepository.getOne(authorityId);

        if (role == null) {
            throw new NullObjectException(ExceptionMessages.ROLE_NULL);
        }
        return role;
    }

    @Override
    public List<Authority> readAll() {
        return authorityRepository.findByParentAuthorityOrderById(null);
    }

    @Override
    public void assignRoleAuthorities(RoleAuthorityRequest roleAuthorityRequest) {
        Roles role = roleRepository.getOne(roleAuthorityRequest.getRoleId());
        roleAuthorityRepository.deleteAllByRole(role);
        for (Long authorityId : roleAuthorityRequest.getAuthorityIds()) {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRole(role);
            roleAuthority.setAuthority(authorityRepository.getOne(authorityId));
            roleAuthorityRepository.save(roleAuthority);
        }
    }

    @Override
    public List<Authority> getUserAuthorities(Users users) {
        Collection<UserRole> userRolesByUsersId = users.getUserRoles();
        List<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRolesByUsersId) {
            roleIds.add(userRole.getRole().getId());
        }
        Set<Long> authorityIds = new HashSet<>();
        for (Long roleId : roleIds) {
            List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findAllByRole(roleRepository.getOne(roleId));
            for (RoleAuthority roleAuth : roleAuthorities) {
                authorityIds.add(roleAuth.getAuthority().getId());
            }
        }

        List<Authority> allAuthority = this.authorityRepository.findByParentAuthorityOrderById(null);
        return getAuthorities(allAuthority, authorityIds);
    }

    @Override
    public List<Authority> getAnonymousUserAuthorities() {
        Roles role = roleRepository.findByName("AnonymousUser");
        Set<Long> authorityIds = new HashSet<>();
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findAllByRole(roleRepository.getOne(role.getId()));
        for (RoleAuthority roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthority().getId());
        }

        List<Authority> allAuthority = this.authorityRepository.findByParentAuthorityOrderById(null);
        return getAuthorities(allAuthority, authorityIds);
    }

    @Override
    public List<Authority> readAllAuthority() {
        return IteratorUtils.toList(this.authorityRepository.findAll().iterator());
    }

    @Override
    public Page<AuthorityPageDto> getAll(PageableSearchFilterDto filterDto) {

        SearchSpecificationBuilder<Authority> specificationBuilder =
                SearchSpecificationBuilder.filterAllowedKeysInstance(Authority.class,
                        "title");

        Specification<Authority> builtSpecification = specificationBuilder.build(filterDto.getCriteriaList());

        PageRequestDto pageRequest = filterDto.getPageRequest();
        addSortRequestForCreatedOnDesc(pageRequest);
        PageRequest pageable = pageRequest.getSpringPageRequest();

        Page<Authority> authorityPageResult = authorityRepository.findAll(builtSpecification, pageable);

        List<AuthorityPageDto> authorityPageDtos = new ArrayList<>();

        long totalCount = authorityPageResult.getTotalElements();
        buildResultList(authorityPageResult, authorityPageDtos);

        return new PageImpl<>(authorityPageDtos, pageable, totalCount);
    }

    private void addSortRequestForCreatedOnDesc(PageRequestDto pageRequest) {
        BaseSortRequest sortRequest = new BaseSortRequest();
        sortRequest.setDir("desc");
        sortRequest.setField("updateOn");
        pageRequest.addSort(sortRequest);
    }

    private void buildResultList(Page<Authority> authorityPageResult, List<AuthorityPageDto> authorityPageDtos) {
        for (Authority authority : authorityPageResult) {
            AuthorityPageDto authorityPageDto = new AuthorityPageDto();

            authorityPageDto.setAuthorityId(authority.getId());
            authorityPageDto.setUrl(authority.getUrl());
            authorityPageDto.setParentId(Objects.nonNull(authority.getParentAuthority()) ? authority.getParentAuthority().getId() : null);
            authorityPageDto.setMenu(authority.getMenu());
            authorityPageDto.setTitle(authority.getTitle());

            authorityPageDtos.add(authorityPageDto);
        }
    }

    @Override
    public List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId) {
        List<RoleAuthority> roleAuthorities = this.roleAuthorityRepository.findAllByRole(roleRepository.getOne(roleId));
        Set<Long> authorityIds = new HashSet<>();

        for (RoleAuthority roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthority().getId());
        }

        List<Authority> allAuthority = this.authorityRepository.findByParentAuthorityOrderById(null);
        List<Authority> authorities = getAuthorities(allAuthority, authorityIds);
//        return AuthorityMapper.INSTANCE.entityListToDtoList(authorities);
        return new ArrayList<>();
    }

    private List<Authority> getAuthorities(List<Authority> authorityListByDb, Set<Long> authorityIds) {
        List<Authority> authorityList = new ArrayList<>();
        for (Authority authority : authorityListByDb) {
            if (authorityIds.contains(authority.getId())) {
                authorityList.add(authority);
            } else {
                if (authority.getAuthorities().size() > 0) {
                    List<Authority> newAuthorities = getAuthorities(new ArrayList<>(authority.getAuthorities()), authorityIds);
                    if (newAuthorities.size() > 0) {
                        authority.setAuthorities(newAuthorities);
                        authorityList.add(authority);
                    }
                }
            }
        }
        return authorityList;
    }

    @Autowired
    public void setRoleAuthorityRepository(RoleAuthorityRepository roleAuthorityRepository) {
        this.roleAuthorityRepository = roleAuthorityRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}

