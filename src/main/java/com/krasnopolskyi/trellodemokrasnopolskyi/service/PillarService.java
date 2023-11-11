package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Pillar;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;

import java.util.Optional;

public interface PillarService extends BaseService<Pillar> {

    Optional<Pillar> update(Pillar pillar, Long id);
}
