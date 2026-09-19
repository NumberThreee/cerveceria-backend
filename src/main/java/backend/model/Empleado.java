package backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String pin; // <--- ASEGURATE DE TENER ESTE CAMPO

    public Empleado() {}

    public Empleado(String nombre, String pin) {
        this.nombre = nombre;
        this.pin = pin;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}