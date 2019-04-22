package com.patent.evaluator.service.impl.login;


import com.patent.evaluator.constant.Constants;
import com.patent.evaluator.constant.ExceptionMessages;
import com.patent.evaluator.dao.UsersRepository;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.LoginRequest;
import com.patent.evaluator.service.api.login.LoginService;
import com.patent.evaluator.util.CalendarHelper;
import com.patent.evaluator.util.HashUtils;
import com.patent.evaluator.util.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoginServiceImpl implements LoginService {
    private static Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    private UsersRepository usersRepository;

    private HttpServletRequest httpServletRequest;

    @Override
    public Users loggedIn(LoginRequest loginRequest) throws Exception {
        Users user = usersRepository.findUsersByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new ValidationException(ExceptionMessages.NO_SUCH_USER);
        }
        if (user.getActive() != null && !user.getActive()) {
            throw new ValidationException(ExceptionMessages.USER_NOT_ACTIVE);
        }

        if (!(user.getPassword().equalsIgnoreCase(HashUtils.sha256(loginRequest.getPassword())))) {
            user.setInvalidPswEntryCount(user.getInvalidPswEntryCount() + 1);
            if (user.getInvalidPswEntryCount() >= Constants.INV_PASSWORD_COUNT && user.getLoginLockDate() == null) {
                user.setLoginLockDate(CalendarHelper.getCurrentInstant());
            }
            isInvalidLoginCountReached(user);
            usersRepository.saveAndFlush(user);
            throw new ValidationException(ExceptionMessages.USER_NAME_PASSWORD_NOT_MATCH);
        }
        user.setInvalidPswEntryCount(0);
        user.setLoginLockDate(null);

        return user;
    }

    @Override
    public Users loggedInLDAP(LoginRequest loginRequest) throws Exception {
        Users user = usersRepository.findUsersByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new ValidationException(ExceptionMessages.NO_SUCH_USER);
        }

        if (user.getActive() != null && !user.getActive()) {
            throw new ValidationException(ExceptionMessages.USER_NOT_ACTIVE);
        }

        user.setPassword(HashUtils.sha256(loginRequest.getPassword()));
        user.setInvalidPswEntryCount(0);
        user.setLoginLockDate(null);

        return user;
    }

    /**
     * kullanıcı 5 yanlış giriş yaptığında bekletecek fonksiyon
     *
     * @param user
     * @throws Exception
     */
    private void isInvalidLoginCountReached(Users user) throws Exception {
        if (user.getLoginLockDate() != null) {
            if ((System.currentTimeMillis() - user.getLoginLockDate().getEpochSecond() <= Constants.TIMEOUT_DURATION_MS)) {
                long remaining = (user.getLoginLockDate().getEpochSecond() + Constants.TIMEOUT_DURATION_MS - System.currentTimeMillis()) / 1000;
                throw new ValidationException("Yeni giriş yapmak için kalan süre:\t" + remaining / 60 + " dakika " + remaining % 60 + " saniye");
            }
        }
    }

    private String getClientIp() {
        String remoteAddr = "";
        if (httpServletRequest != null) {
            remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = httpServletRequest.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}

