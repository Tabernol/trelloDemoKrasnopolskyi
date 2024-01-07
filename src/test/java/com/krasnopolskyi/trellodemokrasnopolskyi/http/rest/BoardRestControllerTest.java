package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.rest.BoardRestController;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.BoardMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(BoardRestController.class)
class BoardRestControllerTest {
    @MockBean
    private BoardService boardService;

    @InjectMocks
    private BoardRestController boardRestController;
    @Autowired
    private MockMvc mockMvc;

    private Board board1;
    private Board board2;
    private BoardReadResponse boardReadResponse1;
    private BoardReadResponse boardReadResponse2;

    @BeforeEach
    public void setUp() {
        board1 = Board.builder().id(1L).name("Board 1").owner("test@test.ua").build();
        board2 = Board.builder().id(2L).name("Board 2").owner("test@test.ua").build();
        boardReadResponse1 = BoardReadResponse.builder().id(1L).name("Board 1").owner("test@test.ua").build();
        boardReadResponse2 = BoardReadResponse.builder().id(2L).name("Board 2").owner("test@test.ua").build();
    }

    @Test
    void testGetBoardById() throws Exception {
        when(boardService.findById(1L)).thenReturn(boardReadResponse1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

        verify(boardService, times(1)).findById(1L);
    }

    @Test
    void testGetAllBoards() throws Exception {
        List<Board> boards = Arrays.asList(board1, board2);
        List<BoardReadResponse> boardReadResponses = Arrays.asList(boardReadResponse1, boardReadResponse2);
        when(boardService.findAll()).thenReturn(boardReadResponses);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());

        verify(boardService, times(1)).findAll();
    }

    @Test
    void testCreateBoard() throws Exception {
        BoardCreateRequest boardCreateRequest = BoardCreateRequest.builder().name("New board").owner("new@new.ua").build();
        BoardReadResponse boardReadResponse = BoardReadResponse.builder().id(3L).name("New board").owner("new@new.ua").build();
        when(boardService.create(boardCreateRequest)).thenReturn(boardReadResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(boardCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());

        verify(boardService, times(1)).create(boardCreateRequest);
    }

    @Test
    void testUpdateBoard() throws Exception {
        Long boardId = 1L;
        BoardEditRequest boardEditRequest = BoardEditRequest.builder().name("edited name").build();
        BoardReadResponse boardReadResponseUpdated =
                BoardReadResponse.builder().id(1L).name("edited name").owner("test@test.ua").build();

        when(boardService.update(boardEditRequest, boardId)).thenReturn(boardReadResponseUpdated);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/{id}", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(boardEditRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());

        verify(boardService, times(1)).update(boardEditRequest, boardId);
    }

    @Test
    void testDeleteBoard() throws Exception {
        Long boardId = 1L;
        when(boardService.delete(boardId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/boards/{id}", boardId))
                .andExpect(status().isNoContent());

        verify(boardService, times(1)).delete(boardId);
    }

}

