package mx.edu.utez.integradora.courseUser.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import mx.edu.utez.integradora.user.model.User;

// Course relationship missed 
@Entity
@Table(name = "user_course", indexes = {
        @Index(name = "user_course_id_user", columnList = "id_user"),
// @Index(name = "user_course_id_course", columnList = "falta")
})
public class CourseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user", columnDefinition = "BIGINT")
    private User user;

    private long idCourse;

    @Column(name = "inscription_date", columnDefinition = "TIMESTAMP")
    private Timestamp inscriptionDate;
}
