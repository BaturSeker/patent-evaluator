package com.patent.evaluator.service.impl.user;

import com.patent.evaluator.constant.ValidationMessages;
import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.ResetPasswordDto;
import com.patent.evaluator.dto.UserInfoResponse;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import com.patent.evaluator.service.api.user.UserRules;
import com.patent.evaluator.service.api.user.UserService;
import com.patent.evaluator.util.ValidationHelper;
import com.patent.evaluator.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
public class UserRulesImpl implements UserRules {

    private UserService userService;

    @Override
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
    public Page<UserInfoResponse> getUsersFiltered(PageableSearchFilterDto filterDto) {
        return userService.getUsersFiltered(filterDto);
    }

    @Override
    public List<Users> read() {
        return userService.getAllUsers();
    }

    @Override
    public List getComboUsers() {
        return userService.getComboUsers();
    }

    @Override
    public Page<UserInfoResponse> getUserInfoPage(PageRequest pageRequest) {
        return userService.getUserInfoPage(pageRequest);
    }

    @Override
    public List<Users> findByRole(Roles role) {
        return this.userService.findByRole(role);
    }

    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException {
        return this.userService.resetPassword(resetPasswordDto);
    }

    @Override
    public List<Users> getAllUser(Integer locationId) {
        return this.userService.getAllUser(locationId);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
