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
import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.user.model.User;

// Course relationship missed 
@Entity
@Table(name = "user_course", indexes = {
        @Index(name = "user_course_id_user", columnList = "id_user"),
        @Index(name = "user_course_id_course", columnList = "id_course")
})
public class CourseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user", columnDefinition = "BIGINT")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_course", columnDefinition = "BIGINT")
    private Course course;

    @Column(name = "inscription_date", columnDefinition = "TIMESTAMP")
    private Timestamp inscriptionDate;

    public CourseUser(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public CourseUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Timestamp getInscriptionDate() {
        return inscriptionDate;
    }

    public void setInscriptionDate(Timestamp inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }

}
