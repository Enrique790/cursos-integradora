package mx.edu.utez.integradora.user.service;

import mx.edu.utez.integradora.role.model.Role;
import mx.edu.utez.integradora.role.model.RoleRepository;
import mx.edu.utez.integradora.user.model.User;
import mx.edu.utez.integradora.user.model.UserRepository;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;
import org.apache.coyote.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;

import java.util.List;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAll(){
       return  new ResponseEntity<>(new ResponseObject(userRepository.findAll(), Type.SUCCESS, "Listado de usuarios"), HttpStatus.OK);
    }
}
