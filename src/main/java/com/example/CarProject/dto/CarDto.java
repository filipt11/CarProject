package com.example.CarProject.dto;

import com.example.CarProject.entities.MyUser;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class CarDto {

    private Long id;

    @Size(min=2, max=30, message = "{brand.size}")
    private String brand;
    
    @Size(min=1, max=50, message = "{model.size}")
    private String model;

    @NotNull(message = "{prodYear.empty}")
    @Min(value=1980, message = "{prodYear.min}")
    @Max(value=2026, message = "{prodYear.max}")
    private int prodYear;

    @NotNull(message = "{engineSize.empty)")
    @DecimalMin(value="0.6",message = "{engineSize.min}")
    @DecimalMax(value="8.0",message = "{engineSize.max}")
    private double engineSize;

    @NotNull(message = "{hp.empty)")
    @Min(value=40, message = "{hp.min}")
    @Max(value=1600, message = "{hp.max}")
    private int hp;

    @Size(max=5000,message = "{description.size}")
    private String description;

    private String image;

    private Long user;

    private boolean highlighted;

    public void setEngineSize(Double engineSize) {
        if (engineSize != null) {
            this.engineSize = Math.round(engineSize * 10.0) / 10.0;
        }
    }

}
