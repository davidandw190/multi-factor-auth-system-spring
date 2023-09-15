package com.davidandw190.mfa.repositories;

import com.davidandw190.mfa.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
        SELECT t FROM Token t
        INNER JOIN User u ON t.user.id = u.id
        WHERE u.id = :userID AND (t.expired = false OR t.revoked = false)
    """)
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);

}
