package com.example.CarProject.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersGen")
    @SequenceGenerator(name = "usersGen", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Car> cars;

    @Column(columnDefinition = "boolean default false")
    private boolean isBanned=false;

}
