package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.PillarPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Pillar;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.PillarMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.PillarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/columns")
public class PillarRestController {
    private final PillarService pillarService;
    private final PillarMapper pillarMapper;

    public PillarRestController(PillarService pillarService,
                                PillarMapper pillarMapper) {
        this.pillarService = pillarService;
        this.pillarMapper = pillarMapper;
    }

    @GetMapping("/{id}")
    public Pillar findById(@PathVariable("id") Long id) {
        return pillarService.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<Pillar> getAll() {
        return pillarService.findAll();
    }

    @PostMapping
    public Pillar create(@RequestBody PillarPostDto pillarPostDto) {
        Pillar pillar = pillarMapper.mapToEntity(pillarPostDto);
        return pillarService.create(pillar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return pillarService.delete(id) ? noContent().build() : notFound().build();
    }
}
