package com.krasnopolskyi.trellodemokrasnopolskyi.repository;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
    @Query("SELECT c FROM Column c WHERE c.board.id = :boardId")
    List<Column> findAllByBoard(Long boardId);
}
