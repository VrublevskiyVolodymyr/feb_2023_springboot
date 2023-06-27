package ua.com.owu.feb_2023_springboot.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ua.com.owu.feb_2023_springboot.dao.ClientUserDAO;
import ua.com.owu.feb_2023_springboot.models.ClientUser;
import ua.com.owu.feb_2023_springboot.models.ClientUserDTO;

import java.nio.charset.StandardCharsets;


@Service
public class ClientUserService implements UserDetailsService {
    private final ClientUserDAO clientUserDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
@Autowired
    public ClientUserService(ClientUserDAO clientUserDAO, @Lazy AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.clientUserDAO = clientUserDAO;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

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

    public ResponseEntity<String> login(@RequestBody ClientUserDTO clientUserDTO) {
//        System.out.println(clientUserDTO);
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

}
