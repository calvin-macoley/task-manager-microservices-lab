package task_manager_microservices_lab.user_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import task_manager_microservices_lab.user_service.model.User;
import task_manager_microservices_lab.user_service.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController implements HealthIndicator {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Health health() {
		return null;
	}

	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("User service is up!");
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userRepository.findAll();
		
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/test")
    public ResponseEntity<User> createTestUser(@RequestBody User user) {
        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
