package com.suman.sharecare.campaign.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String state;

    @Column
    private String landmark;

    public Location(String city, String district, String state, String landmark) {
        this.city = city;
        this.state = state;
        this.district = district;
        this.landmark = landmark;
    }
}
