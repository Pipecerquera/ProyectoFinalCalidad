package com.AuthNode.auth.Entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "persona")
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "photo", nullable = true, columnDefinition = "LONGTEXT")
    private String photo;

    @Column(name = "fullname", nullable = false, length = 150)
    private String fullname;

    @Column(name = "password", nullable = false, length = 300)
    private String password;

    @Column(name = "email", nullable = false, length = 90, unique = true)
    private String email;

    @Column(name = "role", nullable = false, length = 11)
    private String role;

    @Column(name = "locate", nullable = true, length = 20)
    private String locate;

    @Column(name = "last_login", nullable = true)
    private LocalDateTime last_login;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updated_at;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "notifications", nullable = false)
    private boolean notifications;


    @Column(name = "verification_token", nullable = true, length = 100)
    private String verificationToken;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    // ── RELACIONES ──────────────────────────────────────────────

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VehiculoEntity> vehiculos;

    // ── GETTERS Y SETTERS ───────────────────────────────────────

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getLocate() { return locate; }
    public void setLocate(String locate) { this.locate = locate; }

    public LocalDateTime getLast_login() { return last_login; }
    public void setLast_login(LocalDateTime last_login) { this.last_login = last_login; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }

    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public boolean getNotifications() { return notifications; }
    public void setNotifications(boolean notifications) { this.notifications = notifications; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public List<VehiculoEntity> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<VehiculoEntity> vehiculos) { this.vehiculos = vehiculos; }
}