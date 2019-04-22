package com.patent.evaluator.dto;

import java.util.List;

public class RoleAuthorityRequest {
    private Long roleId;
    private List<Long> authorityIds;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getAuthorityIds() {
        return authorityIds;
    }

    public void setAuthorityIds(List<Long> authorityIds) {
        this.authorityIds = authorityIds;
    }
}
