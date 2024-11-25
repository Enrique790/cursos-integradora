package mx.edu.utez.integradora.course.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String duration;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String temario;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "id_categoria", nullable = false)
    private int idCategoria;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private boolean status;

    public Course() {
    }

    public Course(String name, String duration, String temario, String descripcion, int idCategoria, boolean status) {
        this.name = name;
        this.duration = duration;
        this.temario = temario;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getTemario() {
        return temario;
    }

    public void setTemario(String temario) {
        this.temario = temario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
