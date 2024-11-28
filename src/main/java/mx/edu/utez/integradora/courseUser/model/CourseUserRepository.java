package mx.edu.utez.integradora.courseUser.model;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.utez.integradora.course.model.Course;

public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

}
