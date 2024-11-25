package mx.edu.utez.integradora.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.course.service.CourseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CourseController {

    private CourseService cursoService;

    @Autowired
    public CourseController(CourseService cursoService) {
        this.cursoService = cursoService;
    }

    // Crear o actualizar un curso
    @PostMapping
    public ResponseEntity<Course> guardarCurso(@RequestBody Course curso) {
        return ResponseEntity.ok(cursoService.guardarCurso(curso));
    }

    // Obtener todos los cursos
    @GetMapping
    public ResponseEntity<List<Course>> obtenerCursos() {
        return ResponseEntity.ok(cursoService.obtenerCursos());
    }

    // Obtener curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Course>> obtenerCursoPorId(@PathVariable int id) {
        return ResponseEntity.ok(cursoService.obtenerCursoPorId(id));
    }

    // Deshabilitar un curso
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deshabilitarCurso(@PathVariable int id) {
        boolean exito = cursoService.deshabilitarCurso(id);
        if (exito) {
            return ResponseEntity.ok("El curso a sido desabilitado");
        } else {
            return ResponseEntity.badRequest().body("No se encontro ningun curso con el id: " + id);
        }
    }
}
