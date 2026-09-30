package com.parkingsystem.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parkingsystem.backend.model.User;
import com.parkingsystem.backend.model.User.UserRole;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByCarLicensePlate(String licensePlate);
    
    /**
     * Find a user by username
     * @param username The username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email
     * @param email The email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a username exists
     * @param username The username to check
     * @return true if the username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if an email exists
     * @param email The email to check
     * @return true if the email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find all users with a specific role
     * @param role The role to search for
     * @return List of users with the specified role
     */
    java.util.List<User> findByRole(UserRole role);
}
