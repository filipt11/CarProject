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
    private String Description;

    public Car(String brand, String model, int prodYear, float engineSize, int hp, String description) {
        Brand = brand;
        Model = model;
        ProdYear = prodYear;
        EngineSize = engineSize;
        Hp = hp;
        Description=description;
    }

}
