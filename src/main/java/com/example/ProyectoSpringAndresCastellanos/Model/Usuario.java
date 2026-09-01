package com.example.ProyectoSpringAndresCastellanos.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="usuarios")
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private  String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "nombre",nullable = false, length = 100)
    private String nombre;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Column(nullable = false)
    private Boolean activo;
    @Column(name = "fecha_creacion",nullable = false)
    private LocalDateTime fechaCreacion;

    // ---------- Métodos UserDetails ----------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
    return List.of(new SimpleGrantedAuthority("ROLE_"+ rol.name()));
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

}
