package mx.edu.utez.integradora.security.service;

import mx.edu.utez.integradora.security.JwtUtil;
import mx.edu.utez.integradora.security.UserDetailsServiceImpl;
import mx.edu.utez.integradora.security.controller.AuthController;
import mx.edu.utez.integradora.security.dto.AuthRequest;
import mx.edu.utez.integradora.user.model.User;
import mx.edu.utez.integradora.user.model.UserDto;
import mx.edu.utez.integradora.user.model.UserRepository;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    Logger log = LoggerFactory.getLogger(AuthService.class);

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    private UserDetailsServiceImpl userDetailsService;

    private UserRepository userRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> login(AuthRequest auth) throws Exception {
        if (auth.getEmail().length() <= 0) {
            log.warn("El correo de inicio no cumple el contrato");
            return new ResponseEntity<>(new ResponseObject("Tiene que tener correo para pasar", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        if (auth.getPassword().length() <= 0) {
            log.warn("No se cumple con la contrasena requerida");
            return new ResponseEntity<>(new ResponseObject("Tiene que tener password para pasar", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));

        } catch (BadCredentialsException e) {
            throw new Exception("Usuario o contraseña incorrectos", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        ResponseObject responseObject = new ResponseObject("AUTENTICADO", Type.SUCCESS);
        HttpHeaders headers = new HttpHeaders();

        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                .httpOnly(true)
                .secure(false)
                .maxAge(3600)
                .sameSite("Lax")
                .build();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        headers.setBearerAuth(jwt);
        return ResponseEntity.ok().headers(headers).body(responseObject);
    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> register(UserDto user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user.getEmail().length() > 50 || user.getEmail().length() <= 0) {
            log.warn("El email no es valido");
            return new ResponseEntity<>(
                    new ResponseObject("El email no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        if (user.getName().length() > 50 || user.getName().length() <= 0) {
            log.warn("El nombre no es valido");
            return new ResponseEntity<>(
                    new ResponseObject("El nombre no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword().length() > 100 || user.getPassword().length() <= 0) {
            log.warn("Password no valido");
            return new ResponseEntity<>(
                    new ResponseObject("La contrasena no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        if (user.getPhone().length() > 10 || user.getPhone().length() <= 0) {
            log.warn("Telefono no valido");
            return new ResponseEntity<>(
                    new ResponseObject("La telefono no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> userByMailAndPhone = userRepository.findByMail(user.getEmail());

        if (userByMailAndPhone.isPresent()) {
            log.warn("Ya existe el usuario con este correo");
            return new ResponseEntity<>(new ResponseObject("EL correo ya existe", Type.WARN), HttpStatus.BAD_REQUEST);
        }

        userByMailAndPhone = userRepository.findByPhone(user.getPhone());

        if (userByMailAndPhone.isPresent()) {
            log.warn("Ya existe este un usuario con este telefono");
            return new ResponseEntity<>(new ResponseObject("EL telefono ya existe", Type.WARN), HttpStatus.BAD_REQUEST);
        }

        if (user.getLastname() == null || user.getLastname().length() > 0) {
            log.info("SE creo el usuario con apellido");
            User newUser = new User(user.getName(), user.getLastname(), user.getEmail(),
                    encoder.encode(user.getPassword()), user.getPhone(), true);
            userRepository.saveAndFlush(newUser);
            return new ResponseEntity<>(new ResponseObject(newUser, Type.SUCCESS, "Se creo exitosamente el usuario"),
                    HttpStatus.CREATED);
        }

        User newUser = new User(user.getName(), user.getEmail(), encoder.encode(user.getPassword()), user.getPhone(),
                true);
        userRepository.saveAndFlush(newUser);
        log.info("Se creo el usuario  sin apellido");
        return new ResponseEntity<>(
                new ResponseObject(newUser, Type.SUCCESS, "Se creo exitosamente el usuario, sin apellido"),
                HttpStatus.CREATED);

    }
}
