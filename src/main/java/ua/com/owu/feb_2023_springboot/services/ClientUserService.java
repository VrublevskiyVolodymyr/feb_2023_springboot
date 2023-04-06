package ua.com.owu.feb_2023_springboot.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ua.com.owu.feb_2023_springboot.dao.ClientUserDAO;
import ua.com.owu.feb_2023_springboot.models.ClientUser;
import ua.com.owu.feb_2023_springboot.models.ClientUserDTO;


@Service
@AllArgsConstructor
public class ClientUserService implements UserDetailsService {
    private ClientUserDAO clientUserDAO;
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        ClientUser byEmail = clientUserDAO.findByEmail(username);
        System.out.println(byEmail);
        return clientUserDAO.findByEmail(username);
    }
    public void saveClient(@RequestBody ClientUserDTO clientUserDTO) {
        if (clientUserDTO == null) {
            throw new RuntimeException();
        }
        ClientUser clientUser = new ClientUser();
        clientUser.setEmail(clientUserDTO.getUsername());
        clientUser.setPassword(passwordEncoder.encode(clientUserDTO.getPassword()));
        clientUserDAO.save(clientUser);
    }
}
