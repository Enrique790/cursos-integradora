package mx.edu.utez.integradora.security.controller;

import mx.edu.utez.integradora.security.UserDetailsServiceImpl;
import mx.edu.utez.integradora.security.JwtUtil;
import mx.edu.utez.integradora.security.dto.AuthRequest;
import mx.edu.utez.integradora.security.service.AuthService;
import mx.edu.utez.integradora.user.model.UserDto;
import mx.edu.utez.integradora.user.service.UserService;
import mx.edu.utez.integradora.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5173" }, methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.PATCH }, allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "Set-Cookie")
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
    public ResponseEntity<ResponseObject> login(@RequestBody AuthRequest userLogin) throws Exception {
        return authService.login(userLogin);
    }

    @PostMapping("/admin")
    public ResponseEntity<ResponseObject> loginAdmin(@RequestBody AuthRequest admin) throws Exception {
        return authService.loginAdmin(admin);
    }

    @PostMapping("/recovery")
    public ResponseEntity<ResponseObject> passwordRecovery(@RequestBody AuthRequest authRequest) {
        return authService.sendEmailPassword(authRequest);
    }

    @GetMapping("/code/{verify}")
    public ResponseEntity<ResponseObject> getUserByCode(@PathVariable("verify") String code) {
        return authService.verifyCode(code);
    }

    @PostMapping("/password")
    public ResponseEntity<ResponseObject> updatePassword(@RequestBody UserDto userDto) {

        return authService.changePassword(userDto);
    }

}
