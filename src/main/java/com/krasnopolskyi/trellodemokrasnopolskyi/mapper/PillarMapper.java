package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.PillarPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Pillar;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PillarMapper implements BaseMapper<Pillar, PillarPostDto> {

    private final BoardService boardService;

    public PillarMapper(BoardService boardService) {
        this.boardService = boardService;
    }



    public Pillar mapToEntity(PillarPostDto pillarPostDto) {
        return Pillar.builder()
                .name(pillarPostDto.getName())
                .board(boardService.findById(pillarPostDto.getBoardId()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .build();
    }

    @Override
    public PillarPostDto mapToDto(Pillar pillar) {
        return null;
    }
}
