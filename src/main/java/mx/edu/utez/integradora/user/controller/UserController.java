package mx.edu.utez.integradora.user.controller;

import mx.edu.utez.integradora.user.model.UserDto;
import mx.edu.utez.integradora.user.service.UserService;
import mx.edu.utez.integradora.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/user")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        return userService.getAll();
    }
}
