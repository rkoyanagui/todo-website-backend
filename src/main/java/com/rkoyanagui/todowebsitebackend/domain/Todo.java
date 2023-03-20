package com.rkoyanagui.todowebsitebackend.domain;

import javax.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Entity
public class Todo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String description;

    private boolean done;
}
