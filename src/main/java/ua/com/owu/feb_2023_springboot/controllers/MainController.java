package ua.com.owu.feb_2023_springboot.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.feb_2023_springboot.dao.CarDAO;
import ua.com.owu.feb_2023_springboot.models.Car;

import java.util.List;

@AllArgsConstructor
@RestController

public class MainController {
    private CarDAO carDAO;

    @GetMapping("/cars")
    public List<Car> getCar() {
        List<Car> all = carDAO.findAll();
        return all;
    }

    @GetMapping("/cars/{id}")
    public Car getCars(@PathVariable int id) {
        Car car = carDAO.findById(id).get();
        return car;
    }

    @PostMapping("/cars")
    public void save(@RequestBody Car car) {
        carDAO.save(car);
    }

    @DeleteMapping("/cars/{id}")
    public List<Car> deleteCar(@PathVariable int id) {
        carDAO.deleteById(id);
        return carDAO.findAll();
    }
    @DeleteMapping("/cars/model/{model}")
    public List<Car> deleteCarByModel(@PathVariable String model) {
        carDAO.deleteCarByModel(model);
        return carDAO.findAll();
    }

    @PatchMapping("/cars/{id}")
    public Car updateCar(@PathVariable int id, @RequestBody Car car) {
        Car c = carDAO.findById(id).get();
        c.setModel(car.getModel());
        c.setProducer(car.getProducer());
        c.setPower(car.getPower());
        carDAO.save(c);
        return c;

    }

    @GetMapping("/cars/power/{value}")
    public List<Car> getCarsByPower(@PathVariable int value) {
        List<Car> getCarsByPower = carDAO.getCarsByPower(value);
        return getCarsByPower;

//        return carDAO.findByPower(value);
    }

    @GetMapping("cars/producer/{value}")
    public List<Car> getCarByProducer(@PathVariable String value) {
        return carDAO.findByProducer(value);
    }

}
