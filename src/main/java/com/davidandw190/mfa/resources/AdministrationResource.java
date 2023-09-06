package com.davidandw190.mfa.resources;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *  Just some dummy Management endpoints for implementing role based access
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdministrationResource {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String dummyAdministrationGet() {
        return "GET :: In ADMIN HOME";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public String dummyAdministrationCreate() {
        return "POST :: In ADMIN HOME";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String dummyAdministrationUpdate() {
        return "PUT :: In ADMIN HOME";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String dummyAdministrationDelete() {
        return "DELETE :: In ADMIN HOME";
    }
}
