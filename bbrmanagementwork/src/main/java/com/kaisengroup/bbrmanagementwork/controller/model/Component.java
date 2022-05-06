package com.kaisengroup.bbrmanagementwork.controller.model;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table (name = "\"component\"")
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "data")
    private String data;

    @Column(name = "fkwork")
    private int fkwork;

    public Component(String name, String data, int fkwork) {
        this.name = name;
        this.data = data;
        this.fkwork = fkwork;
    }
}
