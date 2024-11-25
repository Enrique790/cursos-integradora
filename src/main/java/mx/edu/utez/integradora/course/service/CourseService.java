package mx.edu.utez.integradora.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.course.model.CourseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private CourseRepository cursoRepository;

    @Autowired
    public CourseService(CourseRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    // Para crear o actualizar un curso
    public Course guardarCurso(Course curso) {
        return cursoRepository.save(curso);
    }

    // Para obtener todos los datos del curso
    public List<Course> obtenerCursos() {
        return cursoRepository.findAll();
    }

    // Para poder obtener un curso por medio de su id
    public Optional<Course> obtenerCursoPorId(int id) {
        return cursoRepository.findById(id);
    }

    // Para hacer una eliminacion logica de un curso (Desabilitarlo)
    public boolean deshabilitarCurso(int id) {
        Optional<Course> cursoOpt = cursoRepository.findById(id);
        if (cursoOpt.isPresent()) {
            Course curso = cursoOpt.get();
            curso.setStatus(false);
            cursoRepository.save(curso);
            return true;
        }
        return false;
    }
}
