package com.davidandw190.mfa.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.davidandw190.mfa.enums.Permission.*;

/**
 * Enumeration representing user roles with associated permissions.
 */
public enum Role {
    USER(Collections.emptySet()),
    ADMIN(Set.of(
            ADMIN_READ,
            ADMIN_CREATE,
            ADMIN_UPDATE,
            ADMIN_DELETE,
            MANAGEMENT_READ,
            MANAGEMENT_CREATE,
            MANAGEMENT_UPDATE,
            MANAGEMENT_DELETE)
    ),

    MANAGER(Set.of(
            MANAGEMENT_READ,
            MANAGEMENT_CREATE,
            MANAGEMENT_UPDATE,
            MANAGEMENT_DELETE
    ));

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = new java.util.ArrayList<>(getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
