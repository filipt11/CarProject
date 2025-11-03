package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class Car {
    @Id
    @GeneratedValue
    private long id;

    private String Brand;
    private String Model;
    private int ProdYear;
    private float EngineSize;
    private int Hp;

    public Car(String brand, String model, int prodYear, float engineSize, int hp) {
        Brand = brand;
        Model = model;
        ProdYear = prodYear;
        EngineSize = engineSize;
        Hp = hp;
    }

}
