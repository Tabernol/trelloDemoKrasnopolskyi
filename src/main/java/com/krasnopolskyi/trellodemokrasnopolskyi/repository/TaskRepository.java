package com.krasnopolskyi.trellodemokrasnopolskyi.repository;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET " +
            "t.name = COALESCE(:name, t.name)," +
            "t.description = COALESCE(:description, t.description) " +
          //  "t.column_id = COALESCE(:columnId, t.column_id) " +
            "WHERE t.id = :id")
    int customUpdate(@Param("name") String name,
                      @Param("description") String description,
                    //  @Param("column_id") Long columnId,
                      @Param("id") Long id);
}
