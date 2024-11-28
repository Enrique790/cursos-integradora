package mx.edu.utez.integradora.course.model;

public class CourseDto {
    private String nombre;
    private String duracion;
    private String temario;
    private String descripcion;
    private int idCategoria;
    private boolean status;

    // Constructor
    public CourseDto(String nombre, String duracion, String temario, String descripcion, int idCategoria, boolean status) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.temario = temario;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.status = status;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
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
