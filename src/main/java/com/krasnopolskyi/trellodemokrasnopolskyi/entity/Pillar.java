package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@ToString(exclude =  "tasks")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pillars", schema = "krasnopolskyi")
public class Pillar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    @OneToMany(mappedBy = "pillar")
    private List<Task> tasks;


}
