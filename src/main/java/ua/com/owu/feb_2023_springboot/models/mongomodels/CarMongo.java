package ua.com.owu.feb_2023_springboot.models.mongomodels;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
public class CarMongo {

    //    @Indexed
    @Id
    private ObjectId id;
    private String model;
    private String producer;
    private int power;

    public CarMongo(String model, String producer, int power) {
        this.model = model;
        this.producer = producer;
        this.power = power;
    }
}
