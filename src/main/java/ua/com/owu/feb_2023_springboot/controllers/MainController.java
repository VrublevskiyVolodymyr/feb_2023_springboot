package ua.com.owu.feb_2023_springboot.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.owu.feb_2023_springboot.dao.CarDAO;
//import ua.com.owu.feb_2023_springboot.dao.mongo.CarMongoDAO;
//import ua.com.owu.feb_2023_springboot.models.mongomodels.CarMongo;
import ua.com.owu.feb_2023_springboot.dao.ClientUserDAO;
import ua.com.owu.feb_2023_springboot.models.ClientUser;
import ua.com.owu.feb_2023_springboot.models.ClientUserDTO;
import ua.com.owu.feb_2023_springboot.queryFilters.CarSpecifications;
import ua.com.owu.feb_2023_springboot.services.CarService;
import ua.com.owu.feb_2023_springboot.services.ClientUserService;
import ua.com.owu.feb_2023_springboot.views.Views;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@AllArgsConstructor
@RestController
//@RequestMapping(value = "/cars")
public class MainController {
    private CarDAO carDAO;
    private CarService carService;
    //    private CarMongoDAO carMongoDAO;
    private ClientUserService clientUserService;
    private ClientUserDAO clientUserDAO;
    private AuthenticationManager authenticationManager;


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/clients/save")
    public void saveClient(@RequestBody ClientUserDTO clientUserDTO) {
        clientUserService.saveClient(clientUserDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/clients/login")
    public ResponseEntity<String> login(@RequestBody ClientUserDTO clientUserDTO) {
        System.out.println(clientUserDTO);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(clientUserDTO.getUsername(), clientUserDTO.getPassword());
        System.out.println(usernamePasswordAuthenticationToken);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println(authenticate);
        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS256, "okten".getBytes(StandardCharsets.UTF_8))
                    .compact();
            System.out.println(jwtToken);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>("login :)", httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("bad credentials", HttpStatus.FORBIDDEN);
    }

    //OPEN
    @GetMapping("/clients/all")
    @JsonView(value = Views.Level3.class)
    public ResponseEntity<List<ClientUser>> getAllClientsWithoutSensetiveInformation() {
        return new ResponseEntity<>(clientUserDAO.findAll(), HttpStatus.OK);
    }

    //ADMIN
    @GetMapping("/admin/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<ClientUser>> getAllClients() {
        return new ResponseEntity<>(clientUserDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping("/cars")
    @JsonView(value = Views.Level3.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/withSpecifications/{model}/{producer}/{power}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getCar(@PathVariable String model, @PathVariable String producer, @PathVariable int power) {
        return carService.findAllWithSpecifications(
                CarSpecifications.byModel(model)
                        .and(CarSpecifications.byProducer(producer))
                        .and(CarSpecifications.byPower(power)));
    }

    @GetMapping("/cars/{id}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<ua.com.owu.feb_2023_springboot.models.Car> getCars(@PathVariable int id) {
        return carService.getCar(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cars")
    public void save(@RequestBody @Valid ua.com.owu.feb_2023_springboot.models.Car car) {
        carService.save(car);
//        carMongoDAO.save(new CarMongo(car.getModel(), car.getProducer(), car.getPower()));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/cars/{id}")
    public void deleteCar(@PathVariable int id) {
        carService.deleteCar(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/cars/model/{model}")
    public void deleteCarByModel(@PathVariable String model) {
        carService.deleteCarByModel(model);
    }

    @PatchMapping("/cars/{id}")
    public ResponseEntity<ua.com.owu.feb_2023_springboot.models.Car> updateCar(@PathVariable int id, @RequestBody ua.com.owu.feb_2023_springboot.models.Car car) {
        return carService.updateCar(id, car);
    }

    @GetMapping("/cars/power/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getCarsByPower(@PathVariable int value) {
        return carService.getCarsByPower(value);

//        return carDAO.findByPower(value);
    }

    @GetMapping("/cars/producer/{value}")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<ua.com.owu.feb_2023_springboot.models.Car>> getCarByProducer(@PathVariable String value) {
        return carService.getCarByProducer(value);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cars/saveWithPhoto")
    @JsonView(value = Views.Level1.class)
    public void saveWithPhoto(@RequestParam String model, @RequestParam String producer, @RequestParam int power, @RequestParam MultipartFile photo)
            throws IOException {
        carService.saveWithPhoto(model, producer, power, photo);
    }
}
