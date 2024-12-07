package mx.edu.utez.integradora.category.model;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryDto {
    @NotNull(groups = {Modify.class}, message = "Es necesario un id")
    private long id;

    @NotBlank(groups = {Register.class, Modify.class}, message = "Es necesario un nombre")
    private String name;

    @NotBlank(groups = {Register.class, Modify.class}, message = "Es necesaria una descripccion")
    private String description;

    public CategoryDto(String nombre, String descripcion) {
    }

    @NotNull(groups = {Modify.class}, message = "Es necesario un id")
    public long getId() {
        return id;
    }

    public void setId(@NotNull(groups = {Modify.class}, message = "Es necesario un id") long id) {
        this.id = id;
    }

    public @NotBlank(groups = {Register.class, Modify.class}, message = "Es necesario un nombre") String getName() {
        return name;
    }

    public void setName(@NotBlank(groups = {Register.class, Modify.class}, message = "Es necesario un nombre") String name) {
        this.name = name;
    }

    public @NotBlank(groups = {Register.class, Modify.class}, message = "Es necesaria una descripccion") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(groups = {Register.class, Modify.class}, message = "Es necesaria una descripccion") String description) {
        this.description = description;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ChangeStatus {
    }

    public ResponseEntity<Object> save(CategoryDto categoryDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    public Optional<Category> searchByNameAndId(String categoryName, long categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByNameAndId'");
    }
}
