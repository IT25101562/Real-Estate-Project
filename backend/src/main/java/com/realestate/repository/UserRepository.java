package com.realestate.repository;

import com.realestate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if user exists by email
     * @param email the email address
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
