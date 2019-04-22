package com.patent.evaluator.service.impl;

import com.patent.evaluator.constant.ExceptionMessages;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.SetNewPasswordRequest;
import com.patent.evaluator.dto.UserRequest;
import com.patent.evaluator.service.api.PasswordRules;
import com.patent.evaluator.service.api.UserRules;
import com.patent.evaluator.service.api.UserService;
import com.patent.evaluator.util.CalendarHelper;
import com.patent.evaluator.util.HashUtils;
import com.patent.evaluator.util.PasswordGenerator;
import com.patent.evaluator.util.ValidationHelper;
import com.patent.evaluator.util.exception.PasswordNotGenerateException;
import com.patent.evaluator.util.exception.ValidationException;
import org.passay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class PasswordRulesImpl implements PasswordRules {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordRulesImpl.class);

    private UserService userService;
    private UserRules userRules;
    private ResourceLoader resourceLoader;

    @Override
    public void passwordValidation(UserRequest userRequest) throws ValidationException {
        boolean isValidPass = true;
        StringBuilder messages = new StringBuilder();

        basePasswordRules(userRequest.getPassword());

        checkSlangWord(userRequest.getPassword());

        if (userRequest.getPassword().contains(userRequest.getName()) || userRequest.getPassword().contains(userRequest.getSurname())
                || userRequest.getPassword().contains(userRequest.getUserName())) {
            messages.append(ExceptionMessages.CONSTRAINT_PERSONAL_INFO);
            messages.append(System.lineSeparator());
            isValidPass = false;
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    public boolean isPasswordValid(String password) {
        basePasswordRules(password);
        return true;
    }

    @Override
    public boolean isUsernamePasswordValid(String username, String password) {
        StringBuilder messages = new StringBuilder();
        boolean isValidPass = true;
        basePasswordRules(password);
        if (password.contains(username)) {
            isValidPass = false;
            messages.append(ExceptionMessages.CONSTRAINT_PERSONAL_INFO);
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }
        return true;
    }

    @Override
    public void setIsTemproraryPassword(Users user) throws NoSuchAlgorithmException {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generate(10);
        int generateCount = 0;
        while ((!this.isUsernamePasswordValid(user.getUsername(), password)) && generateCount < 5) {
            generateCount++;
            password = passwordGenerator.generate(10);
        }
        if (generateCount > 5 && (!this.isUsernamePasswordValid(user.getUsername(), password))) {
            throw new PasswordNotGenerateException(ExceptionMessages.PASSWORD_NOT_GENERATE);
        }
        String hashedPassword = HashUtils.sha256(password);
        user.setPassword(hashedPassword);
        user.setPasswordCreatedDate(CalendarHelper.getCurrentInstant());
        user.setInvalidPswEntryCount(0);
        user.setPasswordTemporary(true);
        userService.save(user);
    }

    @Override
    public void setIsTemproraryPassword(List<Users> usersList) throws NoSuchAlgorithmException {
        for (Users users : usersList) {
            this.setIsTemproraryPassword(users);
        }
    }

    @Override
    public void setNewPassword(SetNewPasswordRequest setNewPasswordRequest) throws NoSuchAlgorithmException {
        StringBuilder messages = new StringBuilder();
        boolean isValidPass = true;
        String hashedOldPass, hashedNewPass;
        Users user = userService.getUser(setNewPasswordRequest.getUserId());
        hashedOldPass = HashUtils.sha256(setNewPasswordRequest.getOldPassword());
        hashedNewPass = HashUtils.sha256(setNewPasswordRequest.getNewPassword());
        if (!setNewPasswordRequest.getNewPassword().equals(setNewPasswordRequest.getNewPasswordRe())) {
            isValidPass = false;
            messages.append(ExceptionMessages.PASSWORDS_NOT_MATCH);
            messages.append(System.lineSeparator());
        }
        if (!user.getPassword().equalsIgnoreCase(hashedOldPass)) {
            isValidPass = false;
            messages.append(ExceptionMessages.OLD_PASSWORDS_NOT_MATCH);
            messages.append(System.lineSeparator());
        }
        if (user.getPassword().equalsIgnoreCase(hashedNewPass)) {
            isValidPass = false;
            messages.append(ExceptionMessages.OLD_AND_NEW_PASSWORDS_CAN_NOT_BE_SAME);
            messages.append(System.lineSeparator());
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }

        UserRequest userRequest = new UserRequest();
        userRequest.setName(user.getFirstname());
        userRequest.setSurname(user.getSurname());
        userRequest.setUserName(user.getUsername());
        userRequest.setPassword(setNewPasswordRequest.getNewPassword());
        passwordValidation(userRequest);

        String hashedPassword = HashUtils.sha256(setNewPasswordRequest.getNewPassword());
        user.setPassword(hashedPassword);
        user.setPasswordCreatedDate(CalendarHelper.getCurrentInstant());
        user.setPasswordTemporary(false);
        userService.save(user);
    }

    @Override
    public void resetPassword(Long userId) throws NoSuchAlgorithmException {
        StringBuilder messages = new StringBuilder();
        boolean isValidPass = true;
        if (!Objects.nonNull(this.userRules.getUser(userId))) {
            isValidPass = false;
            messages.append(ExceptionMessages.RESET_PASSWORD_USER_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }
        Users user = this.userRules.getUser(userId);
        setIsTemproraryPassword(user);
    }

    @Override
    public void basePasswordRules(String password) {
        boolean isValidPass = true;
        StringBuilder messages = new StringBuilder();
        if (!ValidationHelper.isValid(password)) {
            messages.append(ExceptionMessages.CONSTRAINT_EMPTY);
            messages.append(System.lineSeparator());
            throw new ValidationException(messages.toString());
        } else {
            if (password.length() < 8 || password.length() > 14) {
                messages.append(ExceptionMessages.CONSTRAINT_SIZE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (!password.matches(".*[a-z].*")) {
                messages.append(ExceptionMessages.CONSTRAINT_LOWERCASE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (!password.matches(".*[A-Z].*")) {
                messages.append(ExceptionMessages.CONSTRAINT_UPPERCASE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (!password.matches(".*\\d.*")) {
                messages.append(ExceptionMessages.CONSTRAINT_NUMBER);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (password.contains(" ")) {
                messages.append(ExceptionMessages.CONSTRAINT_WHITESPACE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
        }
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new NumericalSequenceRule(4, false),
                new AlphabeticalSequenceRule(4, false),
                new QwertySequenceRule(4, false)));
        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            isValidPass = false;
            messages.append(ExceptionMessages.CONSTRAINT_SEQUENCE);
            messages.append(System.lineSeparator());
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    public void checkSlangWord(String password) {
        boolean isValidPass = true;
        StringBuilder messages = new StringBuilder();
        Resource resource = resourceLoader.getResource("classpath:argo.txt");
        try (Scanner scanner = new Scanner(resource.getInputStream())) {
            ArrayList<String> list = new ArrayList<>();
            while (scanner.hasNext()) {
                list.add(scanner.next());
            }

            for (String word : list) {
                if (password.toLowerCase().contains(word.toLowerCase())) {
                    isValidPass = false;
                }
            }

        } catch (IOException e) {
            LOGGER.error("error occured", e);
        }

        if (!isValidPass) {
            messages.append(ExceptionMessages.CONSTRAINT_DICTIONARY_WORD);
            throw new ValidationException(messages.toString());
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserRules(UserRules userRules) {
        this.userRules = userRules;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
