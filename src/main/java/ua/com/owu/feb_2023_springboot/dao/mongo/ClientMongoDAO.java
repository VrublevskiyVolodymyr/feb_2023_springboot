package ua.com.owu.feb_2023_springboot.dao.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.com.owu.feb_2023_springboot.models.mongomodels.Client;

public interface ClientMongoDAO extends MongoRepository<Client, ObjectId> {

}
