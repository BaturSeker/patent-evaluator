package com.patent.evaluator.domain;

import com.patent.evaluator.abstracts.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Roles extends BaseEntity<Long> {

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "IsDeleted")
    private Boolean isDeleted = Boolean.FALSE;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<RoleAuthority> roleAuthorities = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<RoleAuthority> getRoleAuthorities() {
        return roleAuthorities;
    }

    public void setRoleAuthorities(List<RoleAuthority> roleAuthorities) {
        this.roleAuthorities = roleAuthorities;
    }
}
