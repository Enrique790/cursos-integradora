package mx.edu.utez.integradora.course.service;

import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.course.model.CourseDto;
import mx.edu.utez.integradora.course.model.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course guardarCurso(CourseDto courseDto) {
        Course course = new Course();
        course.setNombre(courseDto.getNombre());
        course.setDuracion(courseDto.getDuracion());
        course.setTemario(courseDto.getTemario());
        course.setDescripcion(courseDto.getDescripcion());
        course.setIdCategoria(courseDto.getIdCategoria());
        course.setStatus(courseDto.isStatus());

        return courseRepository.save(course);
    }

    public Course actualizarCurso(int id, CourseDto courseDto) {
        Optional<Course> cursoOpt = courseRepository.findById(id);

        if (cursoOpt.isPresent()) {
            Course course = cursoOpt.get();

            course.setNombre(courseDto.getNombre());
            course.setDuracion(courseDto.getDuracion());
            course.setTemario(courseDto.getTemario());
            course.setDescripcion(courseDto.getDescripcion());
            course.setIdCategoria(courseDto.getIdCategoria());
            course.setStatus(courseDto.isStatus());

            return courseRepository.save(course);
        }
        return null;
    }



    public List<Course> obtenerCursos() {
        return courseRepository.findAll();
    }

    public Optional<Course> obtenerCursoPorId(int id) {
        return courseRepository.findById(id);
    }

    public boolean deshabilitarCurso(int id) {
        Optional<Course> cursoOpt = courseRepository.findById(id);
        if (cursoOpt.isPresent()) {
            Course course = cursoOpt.get();
            course.setStatus(false);
            courseRepository.save(course);
            return true;
        }
        return false;
    }

    public List<Course> obtenerCursosActivos() {
        return courseRepository.findByStatusTrue();
    }
}
