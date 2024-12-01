package mx.edu.utez.integradora.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import mx.edu.utez.integradora.category.model.Category;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(40)")
    private String name;

    @Column(name = "duration", nullable = false, columnDefinition = "VARCHAR(40)")
    private String duration;

    @Column(name = "syllabus", columnDefinition = "TEXT")
    private String syllabus;

    @Column(name = "description", columnDefinition = "VARCHAR(40)")
    private String description;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1")
    private boolean status = true;

    @ManyToOne
    @JoinColumn(name = "id_category", columnDefinition = "BIGINT")
    private Category category;

    public Course(String name, String duration, String syllabus, String description) {
        this.name = name;
        this.duration = duration;
        this.syllabus = syllabus;
        this.description = description;
    }

    public Course() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
