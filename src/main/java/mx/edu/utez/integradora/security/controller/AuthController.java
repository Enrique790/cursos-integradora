package mx.edu.utez.integradora.security.controller;

import mx.edu.utez.integradora.security.UserDetailsServiceImpl;
import mx.edu.utez.integradora.security.JwtUtil;
import mx.edu.utez.integradora.security.dto.AuthRequest;
import mx.edu.utez.integradora.security.service.AuthService;
import mx.edu.utez.integradora.user.model.UserDto;
import mx.edu.utez.integradora.user.service.UserService;
import mx.edu.utez.integradora.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    private final AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
            JwtUtil jwtUtil, UserService userService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody UserDto newUser) {
        return authService.register(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> loging(@RequestBody AuthRequest userLogin) throws Exception {
        return authService.login(userLogin);
    }
}
