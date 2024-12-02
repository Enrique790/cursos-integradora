package mx.edu.utez.integradora.courseUser.model;

import jakarta.validation.constraints.NotNull;

public class CourseUserDto {

    private long id;

    @NotNull(groups = { Modify.class, Create.class, Delete.class }, message = "Es necesario el id del usuario")
    private Long id_user;

    @NotNull(groups = { Modify.class, Create.class, Delete.class }, message = "Es necesario el id del curso")
    private Long id_course;

    public CourseUserDto(
            @NotNull(groups = { Modify.class, Create.class,
                    Delete.class }, message = "Es necesario el id del usuario") long id_user,
            @NotNull(groups = { Modify.class, Create.class, Delete.class }) long id_course) {
        this.id_user = id_user;
        this.id_course = id_course;
    }

    public CourseUserDto() {
    }

    public interface Modify {
    }

    public interface Create {
    }

    public interface Delete {
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public Long getId_course() {
        return id_course;
    }

    public void setId_course(long id_course) {
        this.id_course = id_course;
    }

}
