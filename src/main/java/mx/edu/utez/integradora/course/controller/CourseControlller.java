package mx.edu.utez.integradora.course.controller;

import com.example.demo.model.Curso;
import com.example.demo.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CourseController {

    @Autowired
    private CursoService cursoService;

    // Crear o actualizar un curso
    @PostMapping
    public ResponseEntity<Curso> guardarCurso(@RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.guardarCurso(curso));
    }

    // Obtener todos los cursos
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerCursos() {
        return ResponseEntity.ok(cursoService.obtenerCursos());
    }

    // Obtener curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Curso>> obtenerCursoPorId(@PathVariable int id) {
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
