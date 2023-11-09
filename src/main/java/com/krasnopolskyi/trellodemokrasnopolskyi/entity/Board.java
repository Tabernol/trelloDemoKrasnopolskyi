package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "boards", schema = "krasnopolskyi")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String owner;

    @OneToMany(mappedBy = "board")
    private List<Column> columns;
}
