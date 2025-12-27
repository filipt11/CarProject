package com.example.CarProject.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservationGen")
    @SequenceGenerator(name = "reservationGen", sequenceName = "reservation_seq", allocationSize = 1)
    private long id;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="myUser_id")
    @OnDelete(action= OnDeleteAction.SET_NULL)
    private MyUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Car car;

    public Reservation(LocalDate startDate, LocalDate endDate, MyUser user, Car car) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.user=user;
        this.car = car;
    }
}
