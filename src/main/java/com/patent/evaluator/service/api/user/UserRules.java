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

public interface UserRules {

    void saveAll(List<Users> userList);

    void save(Users user);

    List<Users> read();

    Users getUser(Long userId);

    Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto);

    List getComboUsers();

    Page<UserInfoResponseDto> getUserInfoPage(PageRequest pageRequest);

//    Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto);

    List<Users> findByRole(Roles role);

    String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException;

    List<Users> getAllUser(Integer locationId);

}
