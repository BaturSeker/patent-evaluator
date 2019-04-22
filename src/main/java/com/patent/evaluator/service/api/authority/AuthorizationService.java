package com.patent.evaluator.service.api.authority;

import com.patent.evaluator.domain.UserRole;

import java.util.List;

public interface AuthorizationService {

    List<UserRole> getAuthorizeList(Long userId);
}
