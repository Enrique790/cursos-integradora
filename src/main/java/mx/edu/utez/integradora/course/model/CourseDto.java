package mx.edu.utez.integradora.course.model;

import java.util.Locale.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CourseDto {

    @NotNull(groups = { Modify.class, ChangeStatus.class,
            Delete.class }, message = "Es necesario el id para realizar la accion")
    private long id;

    @NotNull(groups = { Modify.class, Create.class }, message = " Se necesita el nombr ")
    @NotBlank(groups = { Modify.class, Create.class }, message = "Tiene que tener valor el nombre")
    private String name;

    @NotBlank(groups = { Create.class, Modify.class }, message = "Se necesita su duracion")
    @NotNull(groups = { Create.class, Modify.class }, message = "Se necestia el su duracion")
    private String duration;

    @NotBlank(groups = { Create.class, Modify.class }, message = "Es necesario el temario")
    private String syllabus;

    @NotBlank(groups = { Create.class, Modify.class }, message = "Se necesita una pequena descripcion")
    private String description;

    @NotNull(groups = { ChangeStatus.class }, message = "Para cambiar el estatus, se necesita el status")
    private boolean status;

    private Long category;

    public CourseDto(
            @NotNull(groups = { Modify.class, Create.class }, message = " Se necesita el nombr ") @NotBlank(groups = {
                    Modify.class, Create.class }, message = "Tiene que tener valor el nombre") String name,
            @NotBlank(groups = { Create.class, Modify.class }, message = "Se necesita su duracion") @NotNull(groups = {
                    Create.class, Modify.class }, message = "Se necestia el su duracion") String duration,
            @NotBlank(groups = { Create.class, Modify.class }, message = "Es necesario el temario") String syllabus,
            @NotBlank(groups = { Create.class,
                    Modify.class }, message = "Se necesita una pequena descripcion") String description) {
        this.name = name;
        this.duration = duration;
        this.syllabus = syllabus;
        this.description = description;
    }

    public CourseDto() {
    }

    public interface Modify {

    }

    public interface Create {

    }

    public interface ChangeStatus {

    }

    public interface Delete {

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

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

}
