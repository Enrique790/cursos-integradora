package mx.edu.utez.integradora.courseUser.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.edu.utez.integradora.course.model.Course;

public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

    // @Query(value = "SELECT c.name, c.duration, c.description, c.syllabus FROM
    // CourseUser uc RIGHT JOIN Course c ON uc.course.id = c.id WHERE uc.user.id =
    // :id_user")
    // List<Course> coursesByUser(@Param("id_user") Long id_user);

    @Query(value = "SELECT c FROM CourseUser  cu INNER JOIN Course c ON cu.course.id = c.id WHERE cu.user.id = :id_user")
    List<Course> courseByUser(@Param("id_user") Long id_user);
}
