package task_manager_microservices_lab.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import task_manager_microservices_lab.user_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by username
    Optional<User> findByUsername(String username);
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
}
