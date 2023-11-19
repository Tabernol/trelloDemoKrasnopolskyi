package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_order_dto.ColumnOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_order_dto.TaskOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.rest.ColumnOrderRestController;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(ColumnOrderRestController.class)
class ColumnOrderRestControllerTest {
    @MockBean
    private ColumnOrderingService columnOrderingService;
    @MockBean
    private ColumnMapper columnMapper;

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private ColumnOrderRestController columnOrderRestController;

    @Test
    void testGetOrderedColumn() throws Exception {
        // Mock data
        Long boardId = 1L;
        List<Column> mockColumns = new ArrayList<>();
        mockColumns.add(Column.builder().id(1L).name("Column 1").build());
        mockColumns.add(Column.builder().id(2L).name("Column 2").build());

        // Mocking service method
        when(columnOrderingService.findAllColumnsByBoardInUserOrder(anyLong())).thenReturn(mockColumns);

        // Mocking mapper method
        List<ColumnReadResponse> mockResponses = new ArrayList<>();
        mockResponses.add(ColumnReadResponse.builder().id(1L).name("Column 1").build());
        mockResponses.add(ColumnReadResponse.builder().id(2L).name("Column 2").build());
        when(columnMapper.mapAll(anyList())).thenReturn(mockResponses);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards/{boardId}/columns", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Column 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Column 2")));
    }


    @Test
    void testReOrderSuccess() throws Exception {
        // Mock data
        Long columnId = 1L;
        ColumnOrderEditRequest request = new ColumnOrderEditRequest();
        request.setColumnId(columnId);
        request.setOrderIndex(2);
        ColumnOrder columnOrder = ColumnOrder.builder().columnId(columnId).orderIndex(2).build();
        // Mocking service method
        when(columnOrderingService.reorder(columnOrder, 1L)).thenReturn(12);

        // Convert request to JSON
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // Perform the PUT request
        mockMvc.perform(put("/api/v1/boards/columns/1/reorder", columnId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Column ordered successfully"));
    }

    @Test
    void testReOrderFailure() throws Exception {
        // Mock data
        Long columnId = 1L;
        ColumnOrderEditRequest request = new ColumnOrderEditRequest();
        request.setColumnId(columnId);
        request.setOrderIndex(2);
        ColumnOrder columnOrder = ColumnOrder.builder().columnId(columnId).orderIndex(2).boardId(1L).build();

        // Mocking service method
        when(columnOrderingService.reorder(columnOrder, 1L)).thenReturn(0);

        // Convert request to JSON
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // Perform the PUT request
        mockMvc.perform(put("/api/v1/boards/columns/{columnId}/reorder", columnId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Column ordered failed"));
    }

    @Test
    void testReOrderMismatchedColumns() throws Exception {
        // Mock data
        Long columnId = 1L;
        ColumnOrderEditRequest request = new ColumnOrderEditRequest();
        request.setColumnId(2L); // Mismatched ID

        // Convert request to JSON
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // Setup mockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(columnOrderRestController).build();

        // Perform the PUT request
        mockMvc.perform(put("/api/v1/boards/columns/{columnId}/reorder", columnId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
