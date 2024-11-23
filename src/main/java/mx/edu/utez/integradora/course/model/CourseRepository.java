package mx.edu.utez.integradora.course.model;

import com.example.demo.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Curso, Integer> {

}
