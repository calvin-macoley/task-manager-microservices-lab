package task_manager_microservices_lab.user_service.controller;

import task_manager_microservices_lab.user_service.dto.LoginRequest;
import task_manager_microservices_lab.user_service.dto.LoginResponse;
import task_manager_microservices_lab.user_service.dto.RegisterRequest;
import task_manager_microservices_lab.user_service.model.User;
import task_manager_microservices_lab.user_service.service.AuthService;
import task_manager_microservices_lab.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails);

            // Get user information
            User user = authService.getUserByUsername(loginRequest.getUsername());

            // Create response
            LoginResponse response = new LoginResponse(
                token,
                "Bearer",
                userDetails.getUsername(),
                user.getEmail(),
                jwtUtil.getExpirationTime()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Check if username already exists
            if (authService.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Username is already taken"));
            }

            // Check if email already exists
            if (authService.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Email is already taken"));
            }

            // Create new user
            User user = authService.createUser(registerRequest);

            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                
                if (username != null && jwtUtil.isTokenValid(token)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    String newToken = jwtUtil.generateToken(userDetails);
                    
                    LoginResponse response = new LoginResponse(
                        newToken,
                        "Bearer",
                        username,
                        null,
                        jwtUtil.getExpirationTime()
                    );
                    
                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Token refresh failed"));
        }
    }

    // Inner class for API response
    public static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        // Getters
        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}