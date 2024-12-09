package mx.edu.utez.integradora.user.controller;

import mx.edu.utez.integradora.user.model.UserDto;
import mx.edu.utez.integradora.user.service.UserService;
import mx.edu.utez.integradora.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = { "http://localhost:5173" }, methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH }, allowCredentials = "true")
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

    @PatchMapping("/change")
    public ResponseEntity<ResponseObject> changeStatus(@RequestBody UserDto userChangeStatus) {
        return userService.changeStatus(userChangeStatus);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateProfile(@RequestBody UserDto userUpdateProfile) {
        return userService.updateUser(userUpdateProfile);
    }
}
