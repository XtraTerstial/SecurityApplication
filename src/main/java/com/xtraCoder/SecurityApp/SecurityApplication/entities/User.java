package com.xtraCoder.SecurityApp.SecurityApplication.entities;

import com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Role;
import com.xtraCoder.SecurityApp.SecurityApplication.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String name;

    @ElementCollection(fetch = FetchType.EAGER) //Eager fetch because roles are needed during authentication
    @Enumerated(EnumType.STRING)
    private Set<Role> roles; //Set because a user can have multiple roles

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Add authorities from roles
        roles.forEach(
                role -> {
                    Set<SimpleGrantedAuthority> permissions = PermissionMapping.grantedAuthoritiesRole(role);
                    authorities.addAll(permissions);
                    authorities.add(
                            new SimpleGrantedAuthority("ROLE_" + role.name())
                    );
                }
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
