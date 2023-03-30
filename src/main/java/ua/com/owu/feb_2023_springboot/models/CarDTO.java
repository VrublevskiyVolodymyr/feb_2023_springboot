package ua.com.owu.feb_2023_springboot.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDTO {
    private int id;
    private String model;
    private String producer;
    private int power;

    public CarDTO(Car car) {
        this.id = car.getId();
        this.model = car.getModel();
        this.producer = car.getProducer();
        this.power = car.getPower();
    }
}
