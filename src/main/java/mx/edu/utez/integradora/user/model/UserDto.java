package mx.edu.utez.integradora.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

 public class UserDto {
     private long id;
     @NotNull(groups = {Modify.class, Create.class}, message = "El usuario necesita un nombre")
     @NotBlank(groups = {Modify.class,Create.class}, message = "Es necesario el nombre")
     private String name;

     private String lastname;

     @Email(groups = {Modify.class,Create.class}, message = "Tiene que se un correo valido")
     @NotBlank(groups = {Modify.class,Create.class}, message = "Tiene que tener un correo")
     @NotNull(groups = {Modify.class,Create.class}, message = "Tiene que tener un correo")
     private String email;

     @NotNull(groups = {Modify.class, Create.class}, message = "Tiene que tener contrasena")
     @NotBlank(groups = {Modify.class,Create.class}, message = "Tienes que tener contrasena")
     private String password;

     @NotNull(groups = {Modify.class, Create.class}, message = "Tienes que tener un numero de telefono")
     @NotBlank(groups = {Modify.class, Create.class}, message = "Tienes que tener numero telefonico")
     private String phone;

     private boolean status;

     public UserDto(long id, String name, String lastname, String email, String password, String phone, boolean status) {
         this.id = id;
         this.name = name;
         this.lastname = lastname;
         this.email = email;
         this.password = password;
         this.phone = phone;
         this.status = status;
     }

     public UserDto(String name, String lastname, String email, String password, String phone, boolean status) {
         this.name = name;
         this.lastname = lastname;
         this.email = email;
         this.password = password;
         this.phone = phone;
         this.status = status;
     }

     public UserDto() {
     }

     public long getId() {
         return id;
     }

     public void setId(long id) {
         this.id = id;
     }

     public @NotNull(groups = {Modify.class, Create.class}, message = "El usuario necesita un nombre") @NotBlank(groups = {Modify.class, Create.class}, message = "Es necesario el nombre") String getName() {
         return name;
     }

     public void setName(@NotNull(groups = {Modify.class, Create.class}, message = "El usuario necesita un nombre") @NotBlank(groups = {Modify.class, Create.class}, message = "Es necesario el nombre") String name) {
         this.name = name;
     }

     public @NotBlank(groups = {Modify.class, Create.class}, message = "NO PUEDE ESTAR EN BLANCO SI QUIERES TENER APELLIDO") String getLastname() {
         return lastname;
     }

     public void setLastname(String lastname) {
         this.lastname = lastname;
     }

     public @Email(groups = {Modify.class, Create.class}, message = "Tiene que se un correo valido") @NotBlank(groups = {Modify.class, Create.class}, message = "Tiene que tener un correo") @NotNull(groups = {Modify.class, Create.class}, message = "Tiene que tener un correo") String getEmail() {
         return email;
     }

     public void setEmail(@Email(groups = {Modify.class, Create.class}, message = "Tiene que se un correo valido") @NotBlank(groups = {Modify.class, Create.class}, message = "Tiene que tener un correo") @NotNull(groups = {Modify.class, Create.class}, message = "Tiene que tener un correo") String email) {
         this.email = email;
     }

     public @NotNull(groups = {Modify.class, Create.class}, message = "Tiene que tener contrasena") @NotBlank(groups = {Modify.class, Create.class}, message = "Tienes que tener contrasena") String getPassword() {
         return password;
     }

     public void setPassword(@NotNull(groups = {Modify.class, Create.class}, message = "Tiene que tener contrasena") @NotBlank(groups = {Modify.class, Create.class}, message = "Tienes que tener contrasena") String password) {
         this.password = password;
     }

     public @NotNull(groups = {Modify.class, Create.class}, message = "Tienes que tener un numero de telefono") @NotBlank(groups = {Modify.class, Create.class}, message = "Tienes que tener numero telefonico") String getPhone() {
         return phone;
     }

     public void setPhone(@NotNull(groups = {Modify.class, Create.class}, message = "Tienes que tener un numero de telefono") @NotBlank(groups = {Modify.class, Create.class}, message = "Tienes que tener numero telefonico") String phone) {
         this.phone = phone;
     }

     public boolean isStatus() {
         return status;
     }

     public void setStatus(boolean status) {
         this.status = status;
     }

     interface Modify{}
     interface Create{}

}
