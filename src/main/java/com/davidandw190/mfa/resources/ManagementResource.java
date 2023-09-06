package com.davidandw190.mfa.resources;

import org.springframework.web.bind.annotation.*;

/**
 *  Just some dummy Administration endpoints for implementing role based access
 */
@RestController
@RequestMapping("/management")
public class ManagementResource {

    @GetMapping
    public String dummyManagementGet() {
        return "GET :: In MANAGEMENT HOME";
    }

    @PostMapping
    public String dummyManagementCreate() {
        return "POST :: In MANAGEMENT HOME";
    }

    @PutMapping
    public String dummyManagementUpdate() {
        return "PUT :: In MANAGEMENT HOME";
    }

    @DeleteMapping
    public String dummyManagementDelete() {
        return "DELETE :: In MANAGEMENT HOME";
    }
}
