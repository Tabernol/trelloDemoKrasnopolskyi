package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColumnMapperTest {
    // Mock data
    private final ColumnCreateRequest createRequest =
            ColumnCreateRequest.builder().name("Test Column").boardId(1L).build();
    private final ColumnEditRequest editRequest =
            ColumnEditRequest.builder().name("Updated Column Name").build();
    private final Column columnEntity =
            Column.builder().id(1L).name("Test Column").board(Board.builder().id(1L).build()).build();
    private final ColumnReadResponse readResponse =
            ColumnReadResponse.builder().id(1L).name("Test Column").boardId(1L).tasks(null).build();

    private final ColumnMapper columnMapper = new ColumnMapper();

    @Test
    void testMapToEntityWithCreateRequest() {
        Column result = columnMapper.mapToEntity(createRequest);

        assertEquals(createRequest.getName(), result.getName());
        assertEquals(createRequest.getBoardId(), result.getBoard().getId());
    }

    @Test
    void testMapToEntityWithEditRequest() {
        Column result = columnMapper.mapToEntity(editRequest);

        assertEquals(editRequest.getName(), result.getName());
    }

    @Test
    void testMapToDto() {
        ColumnReadResponse result = columnMapper.mapToDto(columnEntity);

        assertEquals(columnEntity.getId(), result.getId());
        assertEquals(columnEntity.getName(), result.getName());
        assertEquals(columnEntity.getBoard().getId(), result.getBoardId());
        assertEquals(columnEntity.getTasks(), result.getTasks());
    }

    @Test
    void testMapAll() {
        // Mock data
        List<Column> columnList = Arrays.asList(
                columnEntity,
                Column.builder().id(2L).name("Another Column").board(Board.builder().id(1L).build()).build()
        );

        // Perform mapping
        List<ColumnReadResponse> result = columnMapper.mapAll(columnList);

        // Verify the result
        assertEquals(columnList.size(), result.size());
        assertEquals(columnEntity.getId(), result.get(0).getId());
        assertEquals(columnEntity.getName(), result.get(0).getName());
        assertEquals(columnEntity.getBoard().getId(), result.get(0).getBoardId());
        assertEquals(columnEntity.getTasks(), result.get(0).getTasks());
    }
}
