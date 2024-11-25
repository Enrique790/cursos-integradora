package mx.edu.utez.integradora.course.controller;

import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.course.model.CourseDto;
import mx.edu.utez.integradora.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> guardarCurso(@RequestBody CourseDto courseDto) {
        Course cursoGuardado = courseService.guardarCurso(courseDto);
        return ResponseEntity.ok(cursoGuardado);
    }

    @GetMapping
    public ResponseEntity<List<Course>> obtenerCursos() {
        return ResponseEntity.ok(courseService.obtenerCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Course>> obtenerCursoPorId(@PathVariable int id) {
        return ResponseEntity.ok(courseService.obtenerCursoPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deshabilitarCurso(@PathVariable int id) {
        boolean exito = courseService.deshabilitarCurso(id);
        if (exito) {
            return ResponseEntity.ok("El curso ha sido deshabilitado");
        } else {
            return ResponseEntity.badRequest().body("No se encontró ningún curso con el id: " + id);
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Course>> obtenerCursosActivos() {
        return ResponseEntity.ok(courseService.obtenerCursosActivos());
    }
}
