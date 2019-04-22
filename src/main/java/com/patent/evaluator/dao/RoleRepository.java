package com.patent.evaluator.dao;

import com.patent.evaluator.abstracts.BaseRepository;
import com.patent.evaluator.domain.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends BaseRepository<Roles, Long> {

    @Query(nativeQuery = true, value = "select RoleId as VALUE, Name as TEXT from Role Where IsDeleted = 0")
    List<Object[]> findRolesAsComboValues();

    Roles findByName(String name);

    List<Roles> findAllByNameContains(String name);

    List<Roles> findAllByIsDeleted(Boolean isDeleted);
}
