package mx.edu.utez.integradora.course.service;

import java.sql.SQLException;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.course.model.CourseDto;
import mx.edu.utez.integradora.category.model.Category;
import mx.edu.utez.integradora.course.model.CourseRepository;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;

@Service
@Transactional
public class CourseService {

    private CourseRepository courseRepository;

    private Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> allCourses() {
        log.info("Todos los cursos ");
        return new ResponseEntity<>(new ResponseObject(courseRepository.findAll(), Type.SUCCESS, "Todos los cursos"),
                HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> statusTrue() {
        return new ResponseEntity<>(
                new ResponseObject(courseRepository.coursesWithStatusTrue(), Type.SUCCESS, "Todos los cursos activos"),
                HttpStatus.OK);
    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> createNew(CourseDto newCourse) {
        var validate = validateCourse(newCourse.getName(), newCourse.getDuration(), newCourse.getSyllabus(),
                newCourse.getDescription());

        if (!validate.equals("VALIDADO")) {
            return new ResponseEntity<>(new ResponseObject(validate, Type.WARN), HttpStatus.BAD_REQUEST);
        }
        try {
            Course course = new Course(newCourse.getName(), newCourse.getDuration(),
                    newCourse.getSyllabus(), newCourse.getDescription());
            Category category = new Category(newCourse.getCategory());
            course.setCategory(category);
            course = courseRepository.save(course);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseObject(course, Type.SUCCESS, "Se creó el curso"));

        } catch (Exception e) {
            log.error("Error al crear el curso: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("Error al crear el curso", Type.WARN));
        }
    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> updateCourse(CourseDto updateCourse) {
        var validate = validateCourse(updateCourse.getName(), updateCourse.getDuration(), updateCourse.getSyllabus(),
                updateCourse.getDescription());
        if (!validate.equals("VALIDADO")) {
            return new ResponseEntity<>(new ResponseObject(validate, Type.WARN), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Course> existingCourseOpt = courseRepository.findById(updateCourse.getId());
            if (existingCourseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("El curso no existe", Type.WARN));
            }
            Course existingCourse = existingCourseOpt.get();
            existingCourse.setName(updateCourse.getName());
            existingCourse.setDuration(updateCourse.getDuration());
            existingCourse.setSyllabus(updateCourse.getSyllabus());
            existingCourse.setDescription(updateCourse.getDescription());

            Course updatedCourse = courseRepository.save(existingCourse);

            return ResponseEntity.ok(new ResponseObject(updatedCourse, Type.SUCCESS, "Se actualizó correctamente"));

        } catch (Exception e) {
            log.error("Error al actualizar el curso: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("Error al acutalizar  el curso", Type.WARN));
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> specificCourse(Long id) {
        Optional<Course> exist = courseRepository.findById(id);
        if (!exist.isPresent()) {
            log.warn("NO existe el curso");
            return new ResponseEntity<>(new ResponseObject("No existe este curso", Type.WARN), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseObject(exist.get(), Type.SUCCESS, "EXISTE"), HttpStatus.OK);
    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> changeStatus(Long id) {
        Optional<Course> exist = courseRepository.findById(id);

        if (!exist.isPresent()) {
            log.warn("NO existe el curso");
            return new ResponseEntity<>(new ResponseObject("No existe este curso", Type.WARN), HttpStatus.NOT_FOUND);
        }

        Course courseChange = exist.get();

        if (courseChange.isStatus()) {
            log.info("Se cambio el curso a inactivo");
            courseChange.setStatus(false);
            courseRepository.save(courseChange);
            return new ResponseEntity<>(new ResponseObject(courseChange.isStatus(), Type.SUCCESS,
                    "Se cambio el estado del curso a inactivo"), HttpStatus.OK);
        }

        courseChange.setStatus(true);
        courseRepository.save(courseChange);
        log.info("Se activo el curso");
        return new ResponseEntity<>(new ResponseObject(courseChange.isStatus(), Type.SUCCESS,
                "Se cambio el estado del curso a activo"), HttpStatus.OK);
    }

    private String validateCourse(String name, String duration, String syllabus,
            String description) {
        if (name == null || name.length() == 0 || name.length() > 80) {
            log.warn("Debe tener nombre el curso");
            return "Se necesita un nombre o no puede se mayor de 40";
        }
        if (duration == null || duration.length() == 0 || duration.length() > 40) {
            log.warn("Se necesita un duracion o no puede se mayor de 40 caracteres");
            ;
            return "Se necesita un duracion o no puede se mayor de 40 caracteres";
        }

        if (syllabus == null || syllabus.length() == 0 || syllabus.length() > 100) {
            log.warn("Se necesita un temario o no puede se mayor de 100 caracteres");
            ;
            return "Se necesita un temario o no puede se mayor de 100 caracteres";
        }

        if (description == null || description.length() == 0 || description.length() > 100) {
            log.warn("Se necesita un descripcion o no puede se mayor de 40 caracteres");
            ;
            return "Se necesita un descripcion o no puede se mayor de 40 caracteres";
        }
        return "VALIDADO";
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> getLimitCourse() {
        return new ResponseEntity<>(new ResponseObject(courseRepository.courseLimit(), Type.SUCCESS, "Cursos"),
                HttpStatus.OK);
    }

}
