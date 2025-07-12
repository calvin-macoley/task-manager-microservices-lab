package task_manager_microservices_lab.user_service.dto;

public class LoginResponse {
    
    private String token;
    private String type;
    private String username;
    private String email;
    private Long expiresIn;
    
    // Constructors
    public LoginResponse() {}
    
    public LoginResponse(String token, String type, String username, String email, Long expiresIn) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.email = email;
        this.expiresIn = expiresIn;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}