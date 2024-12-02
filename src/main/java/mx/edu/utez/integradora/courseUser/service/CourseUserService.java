package mx.edu.utez.integradora.courseUser.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.courseUser.model.CourseUser;
import mx.edu.utez.integradora.courseUser.model.CourseUserDto;
import mx.edu.utez.integradora.courseUser.model.CourseUserRepository;
import mx.edu.utez.integradora.user.model.User;
import mx.edu.utez.integradora.user.model.UserRepository;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;

@Service
@Transactional
public class CourseUserService {
    private CourseUserRepository courseUserRepository;
    private UserRepository userRepository;
    private Logger log = LoggerFactory.getLogger(CourseUserService.class);

    @Autowired
    public CourseUserService(CourseUserRepository courseUserRepository, UserRepository userRepository) {
        this.courseUserRepository = courseUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> myCourses(Long courseUserDto) {
        Optional<User> exist = userRepository.findById(courseUserDto);
        if (!exist.isPresent()) {
            return new ResponseEntity<>(new ResponseObject("No encontrado el usuario", Type.WARN),
                    HttpStatus.NOT_FOUND);
        }
        var validate = validateIdUser(courseUserDto);

        if (validate) {
            return new ResponseEntity<>(new ResponseObject("Debes enviar el id del usuario", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }
        log.info("Id user" + courseUserDto);

        return new ResponseEntity<>(new ResponseObject(courseUserRepository.courseByUser(courseUserDto), Type.SUCCESS,
                "Lista cursos usuario"), HttpStatus.OK);

    }

    @Transactional(rollbackFor = { SQLException.class })
    public ResponseEntity<ResponseObject> registerCourse(CourseUserDto courseUserDto) {
        var validate = validateCourseUser(courseUserDto.getId_user(), courseUserDto.getId_course());

        if (!validate) {
            return new ResponseEntity<>(new ResponseObject("No se puedo enviar ingresar datos vacios", Type.WARN),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        Course course = new Course();

        user.setId(courseUserDto.getId_user());
        course.setId(courseUserDto.getId_course());
        CourseUser courseUser = new CourseUser(user, course);
        courseUserRepository.save(courseUser);

        log.info("Se ingreso la inscripcion del usuario" + courseUserDto.getId_user());
        return new ResponseEntity<>(new ResponseObject("Se ingreso el registro exitoso", Type.SUCCESS),
                HttpStatus.CREATED);

    }

    public boolean validateCourseUser(Long user_id, Long course_id) {
        if (user_id == null || user_id == 0) {
            return false;
        }

        if (course_id == null || course_id == 0) {
            return false;
        }
        return true;
    }

    public boolean validateIdUser(Long user_id) {
        return (user_id == null || user_id == 0) ? true : false;
    }
}
