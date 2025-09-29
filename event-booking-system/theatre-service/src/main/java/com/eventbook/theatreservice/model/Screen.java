package com.eventbook.theatreservice.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "screens")
@Data
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @Lob
    private String seatLayout; // JSON representation of the seat map
}