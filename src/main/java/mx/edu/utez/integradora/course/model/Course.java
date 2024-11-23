package mx.edu.utez.integradora.course.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String duracion;

    @Column(nullable = false)
    private String temario;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "id_categoria", nullable = false)
    private int idCategoria;

    @Column(nullable = false)
    private boolean status;

    // Se colocan los Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getTemario() { return temario; }
    public void setTemario(String temario) { this.temario = temario; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
