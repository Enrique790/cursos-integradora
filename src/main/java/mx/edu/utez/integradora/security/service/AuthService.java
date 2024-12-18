package mx.edu.utez.integradora.security.service;

import mx.edu.utez.integradora.role.model.Role;
import mx.edu.utez.integradora.security.JwtUtil;
import mx.edu.utez.integradora.security.UserDetailsServiceImpl;
import mx.edu.utez.integradora.security.controller.AuthController;
import mx.edu.utez.integradora.security.dto.AuthRequest;
import mx.edu.utez.integradora.user.model.User;
import mx.edu.utez.integradora.user.model.UserDto;
import mx.edu.utez.integradora.user.model.UserRepository;
import mx.edu.utez.integradora.user.model.UserDto.ChangePassword;
import mx.edu.utez.integradora.utils.EmailSender;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.validation.annotation.Validated;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AuthService {

    Logger log = LoggerFactory.getLogger(AuthService.class);

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    private UserDetailsServiceImpl userDetailsService;

    private UserRepository userRepository;

    private EmailSender emailSender;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsService, UserRepository userRepository, EmailSender sender) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.emailSender = sender;
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
        Optional<User> optionalDetail = userRepository.findByMail(auth.getEmail());
        if (optionalDetail.get().getRole().getId() == 1) {
            log.warn("Un admin trato de iniciar sesion en el login equivodado");
            return new ResponseEntity<>(new ResponseObject("Por favor inicia sesion en donde se debe", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        HttpHeaders headers = new HttpHeaders();

        ResponseObject responseObject = new ResponseObject(optionalDetail.get().getId(), Type.SUCCESS, "AUTENTICADO");

        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                .httpOnly(true)
                .secure(false)
                .maxAge(3600)
                .sameSite("LAX")
                .path("/")
                .build();

        // ResponseCookie details = ResponseCookie.from("details", idValues)
        // .httpOnly(false)
        // .secure(false)
        // .maxAge(36000)
        // .sameSite("Lax")
        // .path("/api/**")
        // .build();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        // headers.add(HttpHeaders.SET_COOKIE, details.toString());
        // headers.setBearerAuth(jwt);
        return ResponseEntity.ok().headers(headers).body(responseObject);
    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> loginAdmin(AuthRequest auth) throws Exception {
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
        Optional<User> optionalDetail = userRepository.findByMail(auth.getEmail());
        if (optionalDetail.get().getRole().getId() == 2) {
            log.warn("Este usuario trato de iniciar sesion sin ser admin" + optionalDetail.get().getEmail());
            return new ResponseEntity<>(new ResponseObject("No eres el admin", Type.FATAL), HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        HttpHeaders headers = new HttpHeaders();

        ResponseObject responseObject = new ResponseObject(optionalDetail.get().getId(), Type.SUCCESS, "AUTENTICADO");

        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                .httpOnly(true)
                .secure(false)
                .maxAge(3600)
                .sameSite("LAX")
                .path("/")
                .build();

        // ResponseCookie details = ResponseCookie.from("details", idValues)
        // .httpOnly(false)
        // .secure(false)
        // .maxAge(36000)
        // .sameSite("Lax")
        // .path("/api/**")
        // .build();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        // headers.add(HttpHeaders.SET_COOKIE, details.toString());
        // headers.setBearerAuth(jwt);
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

        if (user.getLastname() != null || user.getLastname().length() > 0) {
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

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> sendEmailPassword(AuthRequest userTO) {

        if (userTO.getEmail().length() < 0 || userTO.getEmail() == null) {
            log.warn("Debe tener correo para cambiar contrasena");
            return new ResponseEntity<>(new ResponseObject("Debe tener correo para recuperar contrasena", Type.ERROR),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<User> exist = userRepository.findByMail(userTO.getEmail());

        if (!exist.isPresent()) {
            log.warn("User doesn't exist");
            return new ResponseEntity<>(new ResponseObject("El usuario no existe", Type.WARN), HttpStatus.NOT_FOUND);
        }

        Random random = new Random();
        StringBuilder numberString = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10);
            numberString.append(digit);
        }

        User userToSendEmail = exist.get();

        userToSendEmail.setCode(numberString.toString());

        userToSendEmail = userRepository.saveAndFlush(userToSendEmail);

        if (userToSendEmail == null) {
            log.warn("Code not create");
            return new ResponseEntity<>(new ResponseObject("Código no registrado", Type.ERROR), HttpStatus.BAD_REQUEST);
        }
        String htmlContent = "<!DOCTYPE html>\n"
                + "<html lang=\"es\">\n"
                + "<head>\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "  <title>Recuperación de Contraseña</title>\n"
                + "  <script src=\"https://cdn.tailwindcss.com\"></script>\n"
                + "  <style>\n"
                + "    :root {\n"
                + "      --bg-main: #05090B;\n"
                + "      --primary: #00adb5;\n"
                + "      --accent: #ac7dd2;\n"
                + "      --text: #eeeeee;\n"
                + "    }\n"
                + "    body {\n"
                + "      background-color: var(--bg-main);\n"
                + "      color: var(--text);\n"
                + "    }\n"
                + "    .bg-primary {\n"
                + "      background-color: var(--primary);\n"
                + "    }\n"
                + "    .bg-accent {\n"
                + "      background-color: var(--accent);\n"
                + "    }\n"
                + "    .text-primary {\n"
                + "      color: var(--primary);\n"
                + "    }\n"
                + "    .text-accent {\n"
                + "      color: var(--accent);\n"
                + "    }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body class=\"p-6\">\n"
                + "  <div class=\"max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg\">\n"
                + "    <h1 class=\"text-2xl font-bold text-center text-primary mb-4\">Recuperación de Contraseña</h1>\n"
                + "\n"
                + "    <p class=\"text-lg mb-6\">Hola, <span id=\"userName\" class=\"font-semibold text-accent\">%USER_NAME%</span>,</p>\n"
                + "\n"
                + "    <p class=\"text-lg mb-4\">Hemos recibido una solicitud para restablecer tu contraseña. Si no fuiste tú, ignora este mensaje.</p>\n"
                + "\n"
                + "    <p class=\"text-lg mb-4\">Tu código de recuperación es:</p>\n"
                + "\n"
                + "    <div class=\"bg-accent text-white p-4 rounded-lg text-center font-semibold text-xl mb-6\">\n"
                + "      <span id=\"recoveryCode\">%RECOVERY_CODE%</span>\n"
                + "    </div>\n"
                + "\n"
                + "    <a href=\"http://localhost:5173/src/auth/contrasena.html?mail=%USER_MAIL%\" class=\"text-lg\">Click</a>\n"
                + "\n"
                + "    <p class=\"text-lg mt-6\">Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>\n"
                + "\n"
                + "    <footer class=\"mt-8 text-center text-gray-600\">\n"
                + "      <p>Gracias por usar nuestro servicio.</p>\n"
                + "    </footer>\n"
                + "  </div>\n"
                + "</body>\n"
                + "</html>";
        String personalizedHtml = htmlContent
                .replace("%USER_NAME%", userToSendEmail.getName())
                .replace("%RECOVERY_CODE%", userToSendEmail.getCode())
                .replace("%USER_MAIL%", userToSendEmail.getEmail());

        emailSender.sendEmail(userToSendEmail.getEmail(), "Recuperacion de contrasena", personalizedHtml);

        return new ResponseEntity<>(new ResponseObject("Correo enviado", Type.SUCCESS), HttpStatus.OK);

    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> verifyCode(String verifyCode) {
        Optional<User> exist = userRepository.findByCode(verifyCode);

        if (!exist.isPresent()) {
            return new ResponseEntity<>(new ResponseObject("NO existe el usuario con ese codigo", Type.WARN),
                    HttpStatus.NOT_FOUND);
        }

        User user = new User();

        user.setEmail(exist.get().getEmail());
        user.setName(exist.get().getName());

        return new ResponseEntity<>(new ResponseObject(user, Type.SUCCESS, "Usuario encontrado"), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ResponseObject> changePassword(@Validated(ChangePassword.class) UserDto password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (password.getEmail() == null || password.getEmail().length() <= 0 || password.getEmail().length() > 50) {
            log.warn("Correo no valido");
            return new ResponseEntity<>(
                    new ResponseObject("El correo no es valido no es valida", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }

        if (password.getPassword() == null || password.getPassword().length() <= 0
                || password.getPassword().length() > 100) {
            log.warn("Contrasena no valido");
            return new ResponseEntity<>(
                    new ResponseObject("La constrasena no es valido no es valida", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> exist = userRepository.findByMail(password.getEmail());

        if (!exist.isPresent()) {
            log.warn("No existe el correo");
            return new ResponseEntity<>(
                    new ResponseObject("Correo inexistente", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }

        User newPassword = exist.get();

        newPassword.setPassword(encoder.encode(password.getPassword()));

        userRepository.save(newPassword);

        return new ResponseEntity<>(new ResponseObject("Existo al cambiar la contra", Type.SUCCESS), HttpStatus.OK);
    }
}
