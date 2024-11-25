package mx.edu.utez.integradora.security.dto;

public class AuthRequest {
    private String  email;
    private String password;

    public AuthRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email= mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
