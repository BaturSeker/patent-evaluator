package com.patent.evaluator.service.impl.authority;

import com.patent.evaluator.dao.UserRoleRepository;
import com.patent.evaluator.dao.UsersRepository;
import com.patent.evaluator.domain.UserRole;
import com.patent.evaluator.service.api.authority.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorizationServiceImpl implements AuthorizationService {

    private UserRoleRepository userRoleRepository;

    private UsersRepository usersRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserRole> getAuthorizeList(Long userId) {
        List<UserRole> authorizeList = userRoleRepository.findByUser(usersRepository.getOne(userId));
        return authorizeList;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
