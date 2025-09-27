package com.eventbook.theatreservice.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "theatres")
@Data
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String address;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Screen> screens;
}