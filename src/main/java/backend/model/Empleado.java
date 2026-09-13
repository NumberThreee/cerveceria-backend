package backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String pinAcceso;
    private String rol;

    public Empleado() {}

    public Empleado(String nombre, String pinAcceso, String rol) {
        this.nombre = nombre;
        this.pinAcceso = pinAcceso;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPinAcceso() { return pinAcceso; }
    public void setPinAcceso(String pinAcceso) { this.pinAcceso = pinAcceso; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}