package ua.com.owu.feb_2023_springboot.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.com.owu.feb_2023_springboot.views.Views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@ToString
public class ClientUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = Views.Level1.class)
    private int id;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @JsonView(value = Views.Level1.class)
    private List<Role> roles = List.of(Role.USER);
    @Column(unique = true)
    @JsonView(value={Views.Level3.class,Views.Level1.class })
    private String email;
    @JsonView(value={Views.Level3.class,Views.Level1.class })
    private String password;


    @Override
//    @JsonView(value = Views.Level1.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return authorities;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public String getUsername() {
        return this.email;
    }
    @Override
    @JsonView(value = Views.Level1.class)
    public String getPassword() {
        return this.password;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonView(value = Views.Level1.class)
    public boolean isEnabled() {
        return true;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ClientUserDTO {
        private String username;
        private String password;
    }
}
