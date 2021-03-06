package com.patent.evaluator.controller;

import com.patent.evaluator.constant.SuccessMessages;
import com.patent.evaluator.domain.UserRole;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.SuccessResponseDto;
import com.patent.evaluator.dto.password.ResetPasswordDto;
import com.patent.evaluator.dto.user.UserInfoResponseDto;
import com.patent.evaluator.dto.user.UserResponseDto;
import com.patent.evaluator.pageablesearch.model.PageRequestDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import com.patent.evaluator.service.api.authority.AuthorityListRules;
import com.patent.evaluator.service.api.password.PasswordRules;
import com.patent.evaluator.service.api.user.UserRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("rest/user/")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserRules userRules;
    private PasswordRules passwordRules;
    private AuthorityListRules authorityListRules;

    @GetMapping("{userId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getUser(@PathVariable Long userId) {
        try {
            Users user = userRules.getUser(userId);
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setUsername(user.getUsername());
            userResponseDto.setName(user.getFirstname());
            userResponseDto.setSurname(user.getSurname());
            userResponseDto.setUserId(user.getId());
            userResponseDto.setTemporaryPassword(user.getPasswordTemporary());
            userResponseDto.setPhone(user.getTelephone());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setActive(user.getActive());

            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            // TODO baseController handleException metodu silindi
            throw e;
        }
    }

    @GetMapping("index")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAllUsers() {
        List<Users> userList = userRules.read();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("getAuthorizeList{userId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAuthorizeList(@Valid @RequestBody Long userId) {
        List<UserRole> userRoleList = authorityListRules.getAuthorizeList(userId);
        return new ResponseEntity<>(userRoleList, HttpStatus.OK);
    }

    @GetMapping("getComboUsers")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getComboUsers() {
        List userList = userRules.getComboUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(userRules.getUserInfoPage(PageRequest.of(0, 10000)).getContent());
    }

    @GetMapping(value = "getUser/{viewId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAll(@Valid @PathVariable Integer viewId) {
        return ResponseEntity.ok(userRules.getUserInfoPage(PageRequest.of(0, 10000)).getContent().get(viewId - 1));
    }

    @PostMapping("getUserInfoPage")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity<Page<UserInfoResponseDto>> getUserInfoPage(@RequestBody PageRequestDto pageRequest) {
        return ResponseEntity.ok(userRules.getUserInfoPage(PageRequest.of(pageRequest.getPage(), pageRequest.getSize())));
    }

    @PostMapping("getFilteredUsers")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity<Page<UserInfoResponseDto>> getFilteredUsers(@RequestBody PageableSearchFilterDto pageRequest) {
        Page<UserInfoResponseDto> response = userRules.getUsersFiltered(pageRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("resetPassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        String message = null;
        try {
            message = userRules.resetPassword(resetPasswordDto);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("error in userController", e);
        }
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.USER_TEMP_PASSWORD_TITLE, message), HttpStatus.OK);
    }

    @GetMapping("getAllUser/{locationId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAllUser(@PathVariable Integer locationId) {
        List<Users> userList = userRules.getAllUser(locationId);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @Autowired
    public void setUserRules(UserRules userRules) {
        this.userRules = userRules;
    }

    @Autowired
    public void setPasswordRules(PasswordRules passwordRules) {
        this.passwordRules = passwordRules;
    }

    @Autowired
    public void setAuthorityListRules(AuthorityListRules authorityListRules) {
        this.authorityListRules = authorityListRules;
    }
}
