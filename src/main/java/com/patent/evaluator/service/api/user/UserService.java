package com.patent.evaluator.service.api.user;

import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.password.ResetPasswordDto;
import com.patent.evaluator.dto.user.UserInfoResponseDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    void save(Users user);

    Users getUser(Long userId);

    List<Users> getAllUsers();

    List getComboUsers();

    Page<UserInfoResponseDto> getUserInfoPage(PageRequest pageRequest);

    Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto);

    List<Users> findByRole(Roles role);

    //TODO:
//    Integer saveUserImage(UserImageDto userImageDto);

    String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException;

    List<Users> getAllUser(Integer locationId);

    String getUserInfo(Users user);

    Users getCurrentUser();
}
