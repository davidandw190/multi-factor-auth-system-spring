package com.davidandw190.mfa.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dummy Administration endpoints for implementing role based access
 */
@RestController
@RequestMapping("/admin")
public class AdministrationResource {

    @GetMapping
    public String dummyAdminHome() {
        return "GET :: In ADMIN HOME";
    }
}
