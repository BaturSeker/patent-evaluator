package com.patent.evaluator.dao;

import com.patent.evaluator.abstracts.BaseRepository;
import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.domain.UserRole;
import com.patent.evaluator.domain.Users;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

    List<UserRole> findByUser(Users user);

    List<UserRole> findAllByRole(Roles role);
}
