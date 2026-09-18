package ru.hawoline.cloudfilestorage.data;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String password;
    private String role;

    public UserEntity(long id, String name, String password, String role) {
        this(name, password, role);
        this.id = id;
    }
    public UserEntity(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public UserEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
