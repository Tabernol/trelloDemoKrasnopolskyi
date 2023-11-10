package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/columns")
public class ColumnRestController {
    private final ColumnService columnService;

    public ColumnRestController(ColumnService columnService) {
        this.columnService = columnService;
    }
}
