package ua.com.owu.feb_2023_springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.owu.feb_2023_springboot.models.ClientUser;

public interface ClientUserDAO extends JpaRepository<ClientUser, Integer> {
    ClientUser findByEmail(String email);
}
