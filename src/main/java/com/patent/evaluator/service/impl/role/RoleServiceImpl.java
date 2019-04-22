package com.patent.evaluator.service.impl.role;


import com.patent.evaluator.constant.ExceptionMessages;
import com.patent.evaluator.dao.RoleRepository;
import com.patent.evaluator.dao.UserRoleRepository;
import com.patent.evaluator.dao.UsersRepository;
import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.domain.UserRole;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.role.RoleRequest;
import com.patent.evaluator.dto.user.UserRoleRequest;
import com.patent.evaluator.service.api.role.RoleService;
import com.patent.evaluator.service.api.user.UserRules;
import com.patent.evaluator.util.ComboResponseBuilder;
import com.patent.evaluator.util.exception.NullObjectException;
import com.patent.evaluator.util.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private UsersRepository usersRepository;
    private UserRules userRules;

    @Override
    public void save(RoleRequest roleRequest) {
        Roles role = new Roles();
        role.setDeleted(false);
        role.setDescription(roleRequest.getDescription());
        role.setName(roleRequest.getName());
        roleRepository.saveAndFlush(role);
    }

    @Override
    public void update(Long roleId, RoleRequest roleRequest) throws NullObjectException {
        Roles role = null;
        try {
            role = roleRepository.getOne(roleId);
        } catch (Exception e) {
            LOGGER.error("Role update failed!", e);
        }
        if (role == null) {
            throw new NullObjectException(ExceptionMessages.ROLE_NULL);
        }
        role.setDescription(roleRequest.getDescription());
        role.setName(roleRequest.getName());
        roleRepository.saveAndFlush(role);
    }

    @Override
    public void delete(Long roleId) throws NullObjectException {
        Roles role = roleRepository.getOne(roleId);
        List<Users> users = userRules.findByRole(role);
        if (!users.isEmpty()) {
            throw new ValidationException("Bu role atanmış kullanıcılar olduğundan Bu Rol Silinemez.");
        }
        role.setDeleted(true);
        roleRepository.saveAndFlush(role);
    }

    public Roles getRole(Long roleId) throws NullObjectException {
        return roleRepository.getOne(roleId);
    }

    @Override
    public List<Roles> getAllRoles() throws NullObjectException {
        return roleRepository.findAllByIsDeleted(false);
    }

    @Override
    public void assignUserRoles(UserRoleRequest userRoleRequest) {
        for (Long roleId : userRoleRequest.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.getOne(roleId));
            userRole.setUser(usersRepository.getOne(userRoleRequest.getUserId()));
            userRoleRepository.saveAndFlush(userRole);
        }
    }

    @Override
    public List getComboRoles() {
        List<Object[]> resultList = roleRepository.findRolesAsComboValues();
        return ComboResponseBuilder.buildComboResponseList(resultList);
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setUserRules(UserRules userRules) {
        this.userRules = userRules;
    }
}
