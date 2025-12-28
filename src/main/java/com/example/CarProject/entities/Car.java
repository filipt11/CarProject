package com.example.CarProject.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carGen")
    @SequenceGenerator(name = "carGen", sequenceName = "car_seq", allocationSize = 1)
    private long id;

    private String brand;
    private String model;
    private int prodYear;
    private float engineSize;
    private int hp;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String image;

    @Column(columnDefinition = "boolean default false")
    private boolean highlighted=false;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "car")
    private List<Reservation> reservations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private MyUser user;

public Car(String brand, String model, int prodYear, float engineSize, int hp, String description) {
    this.brand = brand;
    this.model = model;
    this.prodYear = prodYear;
    this.engineSize = engineSize;
    this.hp = hp;
    this.description = description;
}

}
