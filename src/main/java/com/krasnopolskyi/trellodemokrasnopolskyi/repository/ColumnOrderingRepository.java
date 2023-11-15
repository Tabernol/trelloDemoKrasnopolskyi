package com.krasnopolskyi.trellodemokrasnopolskyi.repository;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnOrderingRepository extends JpaRepository<ColumnOrder, Long> {

    List<ColumnOrder> findAllByBoardIdOrderByOrderIndex(Long boardId);
}
