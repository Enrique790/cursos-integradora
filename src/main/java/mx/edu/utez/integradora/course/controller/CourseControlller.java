package mx.edu.utez.integradora.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.utez.integradora.course.model.CourseDto;
import mx.edu.utez.integradora.course.service.CourseService;
import mx.edu.utez.integradora.utils.ResponseObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/course")
public class CourseControlller {

    private CourseService courseService;

    @Autowired
    public CourseControlller(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> all() {
        return courseService.allCourses();
    }

    @GetMapping("/specific/{id}")
    public ResponseEntity<ResponseObject> specificCourse(@PathVariable("id") Long id) {
        return courseService.specificCourse(id);
    }

    @GetMapping("/active")
    public ResponseEntity<ResponseObject> allStatusTrue() {
        return courseService.statusTrue();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> newCourse(@RequestBody CourseDto newCourseDto) {
        return courseService.createNew(newCourseDto);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> update(@RequestBody CourseDto courseDto) {
        return courseService.updateCourse(courseDto);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ResponseObject> changeStatus(@PathVariable("id") Long id) {
        return courseService.changeStatus(id);
    }

}
