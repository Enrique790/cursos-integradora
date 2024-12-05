package mx.edu.utez.integradora.courseUser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.edu.utez.integradora.courseUser.model.CourseUserDto;
import mx.edu.utez.integradora.courseUser.service.CourseUserService;
import mx.edu.utez.integradora.utils.ResponseObject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/api/intersection")
@CrossOrigin(origins = { "http://localhost:*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH })
public class CourseUserController {
    private CourseUserService courseUserService;

    @Autowired
    public CourseUserController(CourseUserService courseUserService) {
        this.courseUserService = courseUserService;
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<ResponseObject> courseByUser(@PathVariable("id") Long id) {
        return courseUserService.myCourses(id);
    }

    @PostMapping("/suscribe")
    public ResponseEntity<ResponseObject> suscribeCouResponseEntity(@RequestBody CourseUserDto courseUserDto) {
        return courseUserService.registerCourse(courseUserDto);
    }

}
