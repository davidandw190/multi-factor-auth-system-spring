package com.davidandw190.mfa.entities;

import com.davidandw190.mfa.enums.TokenType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Represents an authentication token entity.
 */
@Data
@Builder
@Entity
@Table(name="token")
public class Token {
    @Id @GeneratedValue
    private Integer id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType type;
    private boolean expired;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token() {}

    public Token(Integer id, String token, TokenType type, boolean expired, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.type = type;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }

}
