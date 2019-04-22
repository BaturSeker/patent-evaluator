package com.patent.evaluator.dao;

import com.patent.evaluator.abstracts.BaseRepository;
import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.domain.RoleAuthority;
import com.patent.evaluator.domain.Roles;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAuthorityRepository extends BaseRepository<RoleAuthority, Long> {

    List<RoleAuthority> findAllByAuthority(Authority authority);

    List<RoleAuthority> findAllByRole(Roles role);

    void deleteAllByRole(Roles role);
}
