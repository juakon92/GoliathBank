package es.jpf.goliathbank_v2.Models;

import jakarta.persistence.*;


@Entity
@Table (name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="usuario")
    private String usuario;
    @Column(name="pass")
    private String pass;

    public Admin() {
        // Constructor vacío requerido por Hibernate
    }

    public Admin(String usuario, String pass) {
        this.usuario = usuario;
        this.pass = pass;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
