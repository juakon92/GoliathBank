package es.jpf.goliathbank_v2.Models;

import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "usuario")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASS")
    private String password;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "APELLIDOS")
    private String apellidos;

    @Column(name = "NUM_TLFN")
    private String movil;

    public Cliente() {
        // Constructor vacío requerido por Hibernate
    }

    public Cliente(String email, String password, String name, String apellidos, String movil) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.apellidos = apellidos;
        this.movil = movil;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
