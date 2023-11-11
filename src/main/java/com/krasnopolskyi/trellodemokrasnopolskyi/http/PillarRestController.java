package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.pillar_dto.PillarPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Pillar;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.PillarMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.PillarService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/pillars")
public class PillarRestController {
    private final PillarService pillarService;
    private final PillarMapper pillarMapper;

    public PillarRestController(PillarService pillarService,
                                PillarMapper pillarMapper) {
        this.pillarService = pillarService;
        this.pillarMapper = pillarMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pillar> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pillarService.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping
    public ResponseEntity<List<Pillar>> getAll() {
        return ResponseEntity.ok(pillarService.findAll());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pillar> create(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestBody PillarPostDto pillarPostDto) {
        Pillar pillar = pillarMapper.mapToEntity(pillarPostDto);
        return ResponseEntity.ok(pillarService.create(pillar));
    }


//    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Pillar> update(
//            @PathVariable("id") Long id,
//            @Validated()
//            @RequestBody PillarPostDto pillarPostDto) {
//        if (!pillarValidator.isPillarExist(taskPostDto)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }

//        Pillar pillar = pillarMapper.mapToEntity(pillarPostDto);


//        TaskReadDto updatedTaskDto = taskMapper.mapToDto(taskService.update(task, id).orElseThrow(()
//                -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
//        return ResponseEntity.ok(updatedTaskDto);
 //   }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return pillarService.delete(id) ? noContent().build() : notFound().build();
    }
}
