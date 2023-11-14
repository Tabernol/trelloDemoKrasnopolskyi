package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks_order", schema = "krasnopolskyi")
public class TaskOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "column_id")
    @jakarta.persistence.Column(name = "column_id")
    private Long columnId;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "task_id")
    @jakarta.persistence.Column(name = "task_id")
    private Long taskId;

    private int orderIndex;
}

