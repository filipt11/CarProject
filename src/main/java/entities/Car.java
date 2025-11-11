package entities;

import jakarta.persistence.*;
import lombok.*;

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

public Car(String brand, String model, int prodYear, float engineSize, int hp, String description) {
    this.brand = brand;
    this.model = model;
    this.prodYear = prodYear;
    this.engineSize = engineSize;
    this.hp = hp;
    this.description = description;
}

}
