package ua.com.owu.feb_2023_springboot.queryFilters;

import org.springframework.data.jpa.domain.Specification;
import ua.com.owu.feb_2023_springboot.models.Car;

public class CarSpecifications {
    public static Specification<Car> byModel(String model) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("model"), model);
    }

    public static Specification<Car> byPower(int power) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.gt(root.get("power"), power);
    }

    public static Specification<Car> byId(int id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }
    public static Specification<Car> byProducer(String producer) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("producer"), producer);
    }
}
