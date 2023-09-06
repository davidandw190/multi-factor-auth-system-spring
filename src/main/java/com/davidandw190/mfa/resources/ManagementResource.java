package com.davidandw190.mfa.resources;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *  Just some dummy Administration endpoints for implementing role based access
 */
@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@RequestMapping("/management")
public class ManagementResource {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    public String dummyManagementGet() {
        return "GET :: In MANAGEMENT HOME";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'management:create')")
    public String dummyManagementCreate() {
        return "POST :: In MANAGEMENT HOME";
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public String dummyManagementUpdate() {
        return "PUT :: In MANAGEMENT HOME";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:delete', 'management:delete')")
    public String dummyManagementDelete() {
        return "DELETE :: In MANAGEMENT HOME";
    }
}
