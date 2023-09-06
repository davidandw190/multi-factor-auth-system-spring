package com.davidandw190.mfa.resources;

import org.springframework.web.bind.annotation.*;

/**
 *  Just some dummy Management endpoints for implementing role based access
 */
@RestController
@RequestMapping("/management")
public class AdministrationResource {

    @GetMapping
    public String dummyAdministrationGet() {
        return "GET :: In ADMIN HOME";
    }

    @PostMapping
    public String dummyAdministrationCreate() {
        return "POST :: In ADMIN HOME";
    }

    @PutMapping
    public String dummyAdministrationUpdate() {
        return "PUT :: In ADMIN HOME";
    }

    @DeleteMapping
    public String dummyAdministrationDelete() {
        return "DELETE :: In ADMIN HOME";
    }
}
