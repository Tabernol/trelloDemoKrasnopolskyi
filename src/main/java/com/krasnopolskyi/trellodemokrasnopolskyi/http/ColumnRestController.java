package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/columns")
public class ColumnRestController {
    private final ColumnService columnService;
    private final ColumnMapper columnMapper;

    public ColumnRestController(ColumnService columnService,
                                ColumnMapper columnMapper) {
        this.columnService = columnService;
        this.columnMapper = columnMapper;
    }

    @GetMapping("/{id}")
    public Column findById(@PathVariable("id") Long id) {
        return columnService.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<Column> getAll() {
        return columnService.findAll();
    }

    @PostMapping
    public Column create(@RequestBody ColumnPostDto columnPostDto) {
        Column column = columnMapper.mapToEntity(columnPostDto);
        return columnService.create(column);
    }
}
