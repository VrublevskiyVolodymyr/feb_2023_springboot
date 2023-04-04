package ua.com.owu.feb_2023_springboot.controllers;
//Створити модель
//        Car
//        id
//        model
//        producer
//        power
//
//        реалізувати запити
//        get /cars
//        get /cars/{id}
//        post /cars
//        delete /cars/{id}
//        get cars/power/{value} (знайти всі по потужності) ()
//        get cars/producer/{value} (знайти всі по виробнику)
//
//
//        Зробити валідацію полів power (power > 0 && power < 1100) і відповідні оробники
//        Переробити всі методи контролера, щоб повертати response entity з відповідними статусами
//
//        Зробити 3 рівня відображення
//        Level1 - id model producer power (для endpoint /cars/{id})
//        Level2 - model producer power ( для endpoint /cars/power, /cars/producer)
//        Level2 - model producer (для endpoint /cars)

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.feb_2023_springboot.dao.CarDAO;
import ua.com.owu.feb_2023_springboot.models.Car;
import ua.com.owu.feb_2023_springboot.models.CarDTO;
import ua.com.owu.feb_2023_springboot.views.Views;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/cars")
public class MainController {
    private CarDAO carDAO;

    @GetMapping("")
    @JsonView(value = Views.Level3.class)
    public ResponseEntity<List<Car>> getCar() {
        Sort by = Sort.by(Sort.Order.desc("id"));
        return new ResponseEntity<>(carDAO.findAll(by), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<Car> getCars(@PathVariable int id) {
        Car car = carDAO.findById(id).get();
        return new ResponseEntity<>( car, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public void save(@RequestBody @Valid Car car) {
        carDAO.save(car);
    }

    @DeleteMapping("/{id}")
    public List<Car> deleteCar(@PathVariable int id) {
        carDAO.deleteById(id);
        return carDAO.findAll();
    }

    @DeleteMapping("/model/{model}")
    public List<Car> deleteCarByModel(@PathVariable String model) {
        carDAO.deleteCarByModel(model);
        return carDAO.findAll();
    }

    @PatchMapping("/{id}")
    public Car updateCar(@PathVariable int id, @RequestBody Car car) {
        Car c = carDAO.findById(id).get();
        c.setModel(car.getModel());
        c.setProducer(car.getProducer());
        c.setPower(car.getPower());
        carDAO.save(c);
        return c;

    }

    @GetMapping("/power/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Car>> getCarsByPower(@PathVariable int value) {
        return new ResponseEntity<>(carDAO.getCarsByPower(value), HttpStatus.OK);

//        return carDAO.findByPower(value);
    }

    @GetMapping("/producer/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<Car>> getCarByProducer(@PathVariable String value) {
        return new ResponseEntity<>(carDAO.findByProducer(value), HttpStatus.OK);
    }

}
