package mx.edu.utez.integradora.user.model;

import jakarta.persistence.*;
import mx.edu.utez.integradora.courseUser.model.CourseUser;
import mx.edu.utez.integradora.role.model.Role;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user", indexes = {
        @Index(name = "user_username", columnList = "name"),
        @Index(name = "user_email", columnList = "email"),
        @Index(name = "user_phone", columnList = "phone")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
    private String name;

    @Column(name = "lastname", nullable = true, columnDefinition = "VARCHAR(40)")
    private String lastname;

    @Column(name = "email", nullable = false, unique = true, length = 50, columnDefinition = "VARCHAR(50)")
    private String email;

    @Column(name = "password", nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(name = "phone", unique = true, length = 10, columnDefinition = "VARCHAR(10)")
    private String phone;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1")
    private boolean status;

    @Column(name = "code", columnDefinition = "VARCHAR(10)")
    private String code;

    @ManyToOne
    @JoinColumn(name = "role_id", columnDefinition = "BIGINT DEFAULT 2")
    private Role role = new Role(2);

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<CourseUser> courseUser;

    public User(String name, String lastname, String email, String password, String phone, boolean status) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.status = status;
    }

    public User(long id) {
        this.id = id;
    }

    public User(String name, String lastname, String email, String password, String phone) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public User(String name, String email, String password, String phone, boolean status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.status = status;
    }

    public User(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public User(String name, String email, String password, String phone, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public User() {
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<CourseUser> getCourseUser() {
        return courseUser;
    }

    public void setCourseUser(List<CourseUser> courseUser) {
        this.courseUser = courseUser;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
