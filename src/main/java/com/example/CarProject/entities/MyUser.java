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

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "user")
    private List<Reservation> reservations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Car> cars;

    public MyUser(String nickname, String email, String password, String role, List<Reservation> reservations) {
        this.username = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
