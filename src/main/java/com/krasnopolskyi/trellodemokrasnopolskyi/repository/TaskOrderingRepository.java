package com.krasnopolskyi.trellodemokrasnopolskyi.repository;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskOrderingRepository extends JpaRepository<TaskOrder, Long> {

    List<TaskOrder> findAllByColumnIdOrderByOrderIndex(Long columnId);
}
