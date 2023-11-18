package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardMapperTest {
    private BoardMapper boardMapper = new BoardMapper();

    // Mocked data
    private final BoardCreateRequest createRequest =
            BoardCreateRequest.builder().name("Test Board").owner("test@test.ua").build();
    private final BoardEditRequest
            editRequest = BoardEditRequest.builder().name("Updated Board Name").build();
    private final Board boardEntity =
            Board.builder().id(1L).name("Test Board").owner("test@test.ua").build();
    private final BoardReadResponse readResponse =
            BoardReadResponse.builder().id(1L).name("Test Board").owner("test@test.ua").build();

    @Test
    void testMapToEntityWithCreateRequest() {
        Board result = boardMapper.mapToEntity(createRequest);

        assertEquals(createRequest.getName(), result.getName());
        assertEquals(createRequest.getOwner(), result.getOwner());
    }

    @Test
    void testMapToEntityWithEditRequest() {
        Board result = boardMapper.mapToEntity(editRequest);

        assertEquals(editRequest.getName(), result.getName());
    }

    @Test
    void testMapToDto() {
        BoardReadResponse result = boardMapper.mapToDto(boardEntity);

        assertEquals(boardEntity.getId(), result.getId());
        assertEquals(boardEntity.getName(), result.getName());
        assertEquals(boardEntity.getOwner(), result.getOwner());
        assertEquals(boardEntity.getColumns(), result.getColumns());
    }

    @Test
    void testMapAll() {
        MockitoAnnotations.initMocks(this);

        // Mock data
        List<Board> boardList = Arrays.asList(boardEntity, new Board(2L, "Another Board", "Jane Doe", null));

        // Perform mapping
        List<BoardReadResponse> result = boardMapper.mapAll(boardList);

        // Verify the result
        assertEquals(boardList.size(), result.size());
        assertEquals(boardEntity.getId(), result.get(0).getId());
        assertEquals(boardEntity.getName(), result.get(0).getName());
        assertEquals(boardEntity.getOwner(), result.get(0).getOwner());
        assertEquals(boardEntity.getColumns(), result.get(0).getColumns());
    }
}
