package com.patent.evaluator.dao;

import com.patent.evaluator.abstracts.BaseRepository;
import com.patent.evaluator.domain.Authority;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuthorityRepository extends BaseRepository<Authority, Long> {

    List<Authority> findByParentAuthorityOrderById(Authority parentAuthority);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from Authority where (ParentId is not null)")
    void deleteFirst();

    Authority findByTitle(String title);
}
