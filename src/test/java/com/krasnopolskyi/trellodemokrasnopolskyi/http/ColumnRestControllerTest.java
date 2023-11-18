package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(ColumnRestController.class)
class ColumnRestControllerTest {
    @MockBean
    private ColumnService columnService;
    @MockBean
    private ColumnMapper columnMapper;
    @InjectMocks
    private ColumnRestController columnRestController;

    @Autowired
    private MockMvc mockMvc;

    private Column testColumn;

    public void setUp() {
        testColumn = Column.builder().id(1L).name("Test Column").board(Board.builder().id(1L).build()).build();
    }


    @Test
    void testGetColumnById() throws Exception {
        Long columnId = 1L;
        ColumnReadResponse columnReadResponse = ColumnReadResponse.builder().id(columnId).name("Test Column").build();

        when(columnService.findById(columnId)).thenReturn(new Column());
        when(columnMapper.mapToDto(any(Column.class))).thenReturn(columnReadResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/columns/{id}", columnId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(columnId))
                .andExpect(jsonPath("$.name").value("Test Column"));

        verify(columnService, times(1)).findById(columnId);
        verify(columnMapper, times(1)).mapToDto(any(Column.class));
    }

    @Test
    void testGetAllColumns() throws Exception {
        List<ColumnReadResponse> columnList = Arrays.asList(
                ColumnReadResponse.builder().id(1L).name("Column 1").build(),
                ColumnReadResponse.builder().id(2L).name("Column 2").build()
        );

        when(columnService.findAll()).thenReturn(Collections.emptyList());
        when(columnMapper.mapAll(anyList())).thenReturn(columnList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/columns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Column 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Column 2"));

        verify(columnService, times(1)).findAll();
        verify(columnMapper, times(1)).mapAll(anyList());
    }

    @Test
    void testCreateColumn() throws Exception {
        ColumnCreateRequest columnCreateRequest = ColumnCreateRequest.builder().name("New Column").boardId(1L).build();
        Column column = Column.builder().id(1L).name("New Column").build();

        when(columnMapper.mapToEntity(columnCreateRequest)).thenReturn(column);
        when(columnService.create(column)).thenReturn(column);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/columns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(columnCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Column"));

        verify(columnMapper, times(1)).mapToEntity(columnCreateRequest);
        verify(columnService, times(1)).create(column);
    }

    @Test
    void testUpdateColumn() throws Exception {
        Long columnId = 1L;
        ColumnEditRequest columnEditRequest = ColumnEditRequest.builder().name("Updated Column").build();
        Column column = Column.builder().id(columnId).name("Updated Column").build();
        ColumnReadResponse columnReadResponse = ColumnReadResponse.builder().id(columnId).name("Updated Column").build();

        when(columnMapper.mapToEntity(columnEditRequest)).thenReturn(column);
        when(columnService.update(column, columnId)).thenReturn(column);
        when(columnMapper.mapToDto(column)).thenReturn(columnReadResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/columns/{id}", columnId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(columnEditRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(columnId))
                .andExpect(jsonPath("$.name").value("Updated Column"));

        verify(columnMapper, times(1)).mapToEntity(columnEditRequest);
        verify(columnService, times(1)).update(column, columnId);
        verify(columnMapper, times(1)).mapToDto(column);
    }

    @Test
    void testDeleteColumn() throws Exception {
        Long columnId = 1L;

        when(columnService.delete(columnId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/columns/{id}", columnId))
                .andExpect(status().isNoContent());

        verify(columnService, times(1)).delete(columnId);
    }

    @Test
    void testDeleteColumn_Success() throws Exception {
        Long columnId = 1L;

        when(columnService.delete(columnId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/columns/{id}", columnId))
                .andExpect(status().isNoContent())
                .andExpect(result -> assertNull(result.getResolvedException()));

        verify(columnService, times(1)).delete(columnId);
    }

    @Test
    void testDeleteColumn_NotFound() throws Exception {
        Long columnId = 1L;
        when(columnService.delete(columnId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/columns/{id}", columnId))
                .andExpect(status().isNotFound());

        verify(columnService, times(1)).delete(columnId);
    }


}
