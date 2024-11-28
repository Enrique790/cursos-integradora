package mx.edu.utez.integradora.course.controller;

import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.course.model.CourseDto;
import mx.edu.utez.integradora.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Crear o actualizar un curso usando CourseDto
    @PostMapping
    public ResponseEntity<Course> guardarCurso(@RequestBody CourseDto courseDto) {
        Course cursoGuardado = courseService.guardarCurso(courseDto);
        return ResponseEntity.ok(cursoGuardado);
    }

    // Obtener todos los cursos
    @GetMapping
    public ResponseEntity<List<Course>> obtenerCursos() {
        return ResponseEntity.ok(courseService.obtenerCursos());
    }

    // Obtener curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Course>> obtenerCursoPorId(@PathVariable int id) {
        return ResponseEntity.ok(courseService.obtenerCursoPorId(id));
    }

    // Deshabilitar un curso
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
        List<Course> cursosActivos = courseService.obtenerCursos().stream()
                .filter(Course::isStatus)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cursosActivos);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Course> actualizarCurso(@PathVariable int id, @RequestBody CourseDto courseDto) {
        Course cursoActualizado = courseService.actualizarCurso(id, courseDto);

        if (cursoActualizado != null) {
            return ResponseEntity.ok(cursoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

