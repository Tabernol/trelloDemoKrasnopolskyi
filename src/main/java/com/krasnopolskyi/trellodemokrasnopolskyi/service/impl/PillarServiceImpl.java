package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Pillar;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.PillarRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.PillarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PillarServiceImpl implements PillarService {

    private final PillarRepository pillarRepository;

    public PillarServiceImpl(PillarRepository pillarRepository) {
        this.pillarRepository = pillarRepository;
    }

    @Override
    public Optional<Pillar> findById(Long id) {
        return pillarRepository.findById(id);
    }

    @Override
    @Transactional
    public Pillar create(Pillar entity) {
        return pillarRepository.save(entity);
    }

    @Override
    public List<Pillar> findAll() {
        return pillarRepository.findAll();
    }

    @Override
    public Optional<Pillar> update(Pillar pillar, Long id) {
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return pillarRepository.findById(id)
                .map(entity -> {
                    pillarRepository.delete(entity);
                    pillarRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
