package com.example.blog2.dao;

import com.example.blog2.po.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EssayRepository extends JpaRepository<Essay,Long> {
    @Query("SELECT e FROM Essay e LEFT JOIN FETCH e.essayFileUrls")
    List<Essay> findAllWithFileUrls();

    @Modifying
    @Query("UPDATE Essay e SET e.likes = e.likes + :delta WHERE e.id = :id")
    int updateLikes(@Param("id") Long id, @Param("delta") int delta);

    @Modifying
    @Query("UPDATE Essay e SET e.recommend = :recommend WHERE e.id = :id")
    int updateRecommend(@Param("id") Long id, @Param("recommend") boolean recommend);

}
