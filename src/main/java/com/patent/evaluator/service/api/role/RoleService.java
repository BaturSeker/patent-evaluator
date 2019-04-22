package com.patent.evaluator.service.api.role;

import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.dto.role.RoleRequest;
import com.patent.evaluator.dto.user.UserRoleRequest;

import java.util.List;

public interface RoleService {
    void save(RoleRequest roleRequest);

    void update(Long roleId, RoleRequest roleRequest);

    void delete(Long roleId);

    Roles getRole(Long roleId);

    List<Roles> getAllRoles();

    void assignUserRoles(UserRoleRequest userRoleRequest);

    List getComboRoles();
}
