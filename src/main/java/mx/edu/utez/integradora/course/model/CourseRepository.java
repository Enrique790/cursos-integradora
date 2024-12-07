package mx.edu.utez.integradora.course.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT * FROM course WHERE status = true", nativeQuery = true)
    List<Course> coursesWithStatusTrue();

    @Query(value = "SELECT * FROM course ORDER BY rand() LIMIT 4", nativeQuery = true)
    List<Course> courseLimit();
}
