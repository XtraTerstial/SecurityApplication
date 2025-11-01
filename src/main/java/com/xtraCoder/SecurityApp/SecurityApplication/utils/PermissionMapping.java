package com.xtraCoder.SecurityApp.SecurityApplication.utils;

import com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Permission.*;
import static com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Role.*;

public class PermissionMapping {
    private static final Map<Role, Set<Permission>> map = Map.of(
            USER, Set.of(USER_VIEW, POST_VIEW),
            CREATOR, Set.of(USER_VIEW, POST_VIEW, USER_UPDATE, POST_UPDATE),
            ADMIN, Set.of(USER_DELETE, USER_UPDATE, POST_DELETE, USER_CREATE, POST_UPDATE)
        );

    //method to get granted authorities based on role
    public static  Set<SimpleGrantedAuthority> grantedAuthoritiesRole(Role role){
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}
