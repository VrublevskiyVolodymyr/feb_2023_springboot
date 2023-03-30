package ua.com.owu.feb_2023_springboot.dao;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.owu.feb_2023_springboot.models.Car;

import java.util.List;

public interface CarDAO extends JpaRepository<Car, Integer>, JpaSpecificationExecutor<Car> {

    @Query("select c from Car c where c.power=:value")
    List<Car> getCarsByPower(@Param("value") int value);

//    List<Car> findByPower(int value);

    List<Car> findByProducer(String value);
    List<Car> findByPower(int power);
    @Modifying
    @Transactional
    void deleteCarByModel(String model);
}
