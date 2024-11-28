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

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> changeStatus(UserDto userChange) {
        Optional<User> exist = userRepository.findById(userChange.getId());

        if (!exist.isPresent()) {
            log.warn("The user doesn't exist");
            return new ResponseEntity<>(new ResponseObject("EL usuario no existe", Type.WARN), HttpStatus.NOT_FOUND);
        }
        User userStatusChange = exist.get();

        userStatusChange.setStatus(userChange.isStatus());

        userStatusChange = userRepository.saveAndFlush(userStatusChange);

        if (userStatusChange == null) {
            log.warn("Status doesn't change");
            return new ResponseEntity<>(new ResponseObject("No se puedo actualizar el status del usuario", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        log.info("Status changed");
        return new ResponseEntity<>(new ResponseObject("Se actualizo el status del usuario", Type.SUCCESS),
                HttpStatus.OK);
    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> updateUser(UserDto userDto) {
        Optional<User> exist = userRepository.findById(userDto.getId());
        if (!exist.isPresent()) {
            log.warn("User doesn't exist");
            return new ResponseEntity<>(new ResponseObject("NO existe el usuario", Type.WARN), HttpStatus.NOT_FOUND);
        }

        if (userDto.getEmail().length() > 50 || userDto.getEmail().length() <= 0) {
            log.warn("El email no es valido");
            return new ResponseEntity<>(
                    new ResponseObject("El email no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        if (userDto.getName().length() > 50 || userDto.getName().length() <= 0) {
            log.warn("El nombre no es valido");
            return new ResponseEntity<>(
                    new ResponseObject("El nombre no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        if (userDto.getPassword().length() > 100 || userDto.getPassword().length() <= 0) {
            log.warn("Password no valido");
            return new ResponseEntity<>(
                    new ResponseObject("La contrasena no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        if (userDto.getPhone().length() > 10 || userDto.getPhone().length() <= 0) {
            log.warn("Telefono no valido");
            return new ResponseEntity<>(
                    new ResponseObject("La telefono no puede ser tan largo a 50 caracteres", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }

        User updateUser = exist.get();
        if (userDto.getLastname() == null || userDto.getLastname().length() > 0) {
            log.info("User update with lastname");
            updateUser.setLastname(userDto.getLastname());
            updateUser.setName(userDto.getName());
            updateUser.setPhone(userDto.getPhone());
            updateUser.setEmail(userDto.getEmail());
            updateUser = userRepository.saveAndFlush(updateUser);
            return new ResponseEntity<>(new ResponseObject("Se actualizo el usuario", Type.SUCCESS),
                    HttpStatus.OK);
        }

        updateUser.setName(userDto.getName());
        updateUser.setPhone(userDto.getPhone());
        updateUser.setEmail(userDto.getEmail());
        updateUser = userRepository.saveAndFlush(updateUser);
        log.info("User update without lastname");

        return new ResponseEntity<>(new ResponseObject("Se actualizo el usuario", Type.SUCCESS), HttpStatus.OK);
    }
}
