package com.patent.evaluator.controller;


import com.patent.evaluator.constant.SuccessMessages;
import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.dto.SuccessResponseDto;
import com.patent.evaluator.dto.role.RoleDto;
import com.patent.evaluator.dto.role.RoleRequest;
import com.patent.evaluator.dto.user.UserRoleRequest;
import com.patent.evaluator.service.api.authority.AuthorityRules;
import com.patent.evaluator.service.api.role.RoleRules;
import com.patent.evaluator.service.impl.role.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/roles/")
public class RoleController {
    private RoleRules roleRules;
    private AuthorityRules authorityRules;

    @GetMapping(value = "{roleId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getRole(@PathVariable Long roleId) {
        Roles role = roleRules.read(roleId);
        RoleDto roleDto = RoleMapper.INSTANCE.entityToDto(role);
        roleDto.setRoleAuthorities(authorityRules.findAuthoritiesByRoleId(roleId));
        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }

    @GetMapping(value = "getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAll() {
        List<RoleDto> roleDtos = RoleMapper.INSTANCE.entityListToDtoList(roleRules.readAll());
        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }

    @DeleteMapping(value = "{roleId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity deleteRole(@PathVariable Long roleId) {
        roleRules.delete(roleId);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_DELETE_TITLE, SuccessMessages.ROLE_DELETE_MESSAGE), HttpStatus.OK);
    }

    @PutMapping(value = "{roleId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity updateRole(@PathVariable Long roleId, @Valid @RequestBody RoleRequest roleRequest) {
        roleRules.update(roleId, roleRequest);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_UPDATE_TITLE, SuccessMessages.ROLE_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping(value = "save")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity saveRole(@Valid @RequestBody RoleRequest roleRequest) {
        roleRules.save(roleRequest);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_CREATE_TITLE, SuccessMessages.ROLE_CREATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping(value = "assignUserRoles")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity assignUserRoles(@Valid @RequestBody UserRoleRequest userRoleRequest) {
        roleRules.assignUserRoles(userRoleRequest);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_ASSIGN_TITLE, SuccessMessages.ROLE_ASSIGN_MESSAGE), HttpStatus.OK);
    }

    @GetMapping("getComboRoles")
    public ResponseEntity getComboRoles() {
        List roleList = roleRules.getComboRoles();
        return new ResponseEntity<>(roleList, HttpStatus.OK);
    }

    @Autowired
    public void setRoleRules(RoleRules roleRules) {
        this.roleRules = roleRules;
    }

    @Autowired
    public void setAuthorityRules(AuthorityRules authorityRules) {
        this.authorityRules = authorityRules;
    }
}
