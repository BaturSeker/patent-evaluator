package com.patent.evaluator.service.impl.user;

import com.patent.evaluator.constant.AuthorityCodes;
import com.patent.evaluator.constant.ValidationMessages;
import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.password.ResetPasswordDto;
import com.patent.evaluator.dto.user.UserInfoResponseDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import com.patent.evaluator.service.api.user.UserRules;
import com.patent.evaluator.service.api.user.UserService;
import com.patent.evaluator.util.ValidationHelper;
import com.patent.evaluator.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserRulesImpl implements UserRules {

    private UserService userService;

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public void saveAll(List<Users> userList) {
        boolean isValid = true;
        StringBuilder messages = new StringBuilder();
        if (!ValidationHelper.isValid(userList)) {
            isValid = false;
            messages.append(ValidationMessages.LIST_CAN_NOT_BE_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        for (Users user : userList) {
            this.save(user);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public void save(Users user) {
        boolean isValid = true;
        StringBuilder messages = new StringBuilder();
        if (Objects.equals(user, null)) {
            isValid = false;
            messages.append(ValidationMessages.USER_CAN_NOT_BE_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
        userService.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public Users getUser(Long userId) {
        boolean isValid = true;
        StringBuilder message = new StringBuilder();
        if (!ValidationHelper.isValid(userId)) {
            isValid = false;
            message.append(ValidationMessages.USER_ID_CAN_NOT_BE_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
        return userService.getUser(userId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto) {
        return userService.getUsersFiltered(filterDto);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public List<Users> read() {
        return userService.getAllUsers();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public List getComboUsers() {
        return userService.getComboUsers();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public Page<UserInfoResponseDto> getUserInfoPage(PageRequest pageRequest) {
        return userService.getUserInfoPage(pageRequest);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public List<Users> findByRole(Roles role) {
        return this.userService.findByRole(role);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException {
        return this.userService.resetPassword(resetPasswordDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Users> getAllUser(Integer locationId) {
        return this.userService.getAllUser(locationId);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
