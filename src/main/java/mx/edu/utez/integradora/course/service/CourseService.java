package mx.edu.utez.integradora.course.service;

import com.example.demo.model.Curso;
import com.example.demo.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CursoRepository cursoRepository;

    // Para crear o actualizar un curso
    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    // Para obtener todos los datos del curso
    public List<Curso> obtenerCursos() {
        return cursoRepository.findAll();
    }

    // Para poder obtener un curso por medio de su id
    public Optional<Curso> obtenerCursoPorId(int id) {
        return cursoRepository.findById(id);
    }

    // Para hacer una eliminacion logica de un curso (Desabilitarlo)
    public boolean deshabilitarCurso(int id) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setStatus(false);
            cursoRepository.save(curso);
            return true;
        }
        return false;
    }
}
