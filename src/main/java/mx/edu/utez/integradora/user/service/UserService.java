package mx.edu.utez.integradora.user.service;

import mx.edu.utez.integradora.user.model.User;
import mx.edu.utez.integradora.user.model.UserDto;
import mx.edu.utez.integradora.user.model.UserRepository;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(new ResponseObject(userRepository.findAll(), Type.SUCCESS, "Listado de usuarios"),
                HttpStatus.OK);
    }

}
