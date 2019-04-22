package com.patent.evaluator.service.api;

import com.patent.evaluator.domain.UserRole;
import com.patent.evaluator.domain.Users;

import java.util.List;

public interface AuthorityListRules {
    List<UserRole> getAuthorizeList(Long userId);

    void authorize(Users user);

}
