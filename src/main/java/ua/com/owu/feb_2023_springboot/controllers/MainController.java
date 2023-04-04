package ua.com.owu.feb_2023_springboot.controllers;

//Беремо проєкт з автомобілями, котрий робили до цього моменту
//        1. Виносимо логіку у сервісний прошарок.
//        2. Додаємо відправку листа на пошту з повідомленням реєстрації нової автівки

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.owu.feb_2023_springboot.dao.CarDAO;
import ua.com.owu.feb_2023_springboot.dao.mongo.CarMongoDAO;
import ua.com.owu.feb_2023_springboot.models.mongomodels.CarMongo;
import ua.com.owu.feb_2023_springboot.queryFilters.CarSpecifications;
import ua.com.owu.feb_2023_springboot.servises.CarService;
import ua.com.owu.feb_2023_springboot.views.Views;

import java.io.File;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/cars")
public class MainController {
    private CarDAO carDAO;
    private CarService carService;
    private CarMongoDAO carMongoDAO;

    @GetMapping("")
    @JsonView(value = Views.Level3.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getAllCars() {
        return carService.getAllCars();

    }

    @GetMapping("/withSpecifications/{model}/{producer}/{power}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getCar(@PathVariable String model, @PathVariable String producer, @PathVariable int power) {
        return carService.findAllWithSpecifications(
                CarSpecifications.byModel(model)
                        .and(CarSpecifications.byProducer(producer))
                        .and(CarSpecifications.byPower(power)));
    }

    @GetMapping("/{id}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<ua.com.owu.feb_2023_springboot.models.Car> getCars(@PathVariable int id) {
        return carService.getCar(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public void save(@RequestBody @Valid ua.com.owu.feb_2023_springboot.models.Car car) {
        carService.save(car);
        carMongoDAO.save(new CarMongo(car.getModel(), car.getProducer(), car.getPower()));
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable int id) {
        carService.deleteCar(id);
    }

    @DeleteMapping("/model/{model}")
    public void deleteCarByModel(@PathVariable String model) {
        carService.deleteCarByModel(model);
    }

    @PatchMapping("/{id}")
    public ua.com.owu.feb_2023_springboot.models.Car updateCar(@PathVariable int id, @RequestBody ua.com.owu.feb_2023_springboot.models.Car car) {
        return carService.updateCar(id, car);
    }

    @GetMapping("/power/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getCarsByPower(@PathVariable int value) {
        return carService.getCarsByPower(value);

//        return carDAO.findByPower(value);
    }

    @GetMapping("/producer/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getCarByProducer(@PathVariable String value) {
        return carService.getCarByProducer(value);
    }

    @PostMapping("/saveWithPhoto")
    @JsonView(value = Views.Level1.class)
    public void saveWithPhoto(
            @RequestParam String model,
            @RequestParam String producer,
            @RequestParam int power,
            @RequestParam MultipartFile photo
    ) throws IOException {

        ua.com.owu.feb_2023_springboot.models.Car car = new ua.com.owu.feb_2023_springboot.models.Car(model, producer, power);
        String originalFilename = photo.getOriginalFilename();
        car.setPhoto("/photo/" + originalFilename);
        String path = System.getProperty("user.home") + File.separator + "images" + File.separator + originalFilename;
        File file = new File(path);
        photo.transferTo(file);
        carService.save(car);
    }
}

