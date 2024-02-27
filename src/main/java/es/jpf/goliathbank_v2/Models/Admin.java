package es.jpf.goliathbank_v2.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


@Entity
@Table (name = "Admin")
public class Admin {

    private final StringProperty usuario;
    private final StringProperty pass;

    public Admin(String usuario, String pass) {
        this.usuario = new SimpleStringProperty(this, "Usuario", usuario);
        this.pass = new SimpleStringProperty(this, "Password", pass);
    }

    public StringProperty usuarioProperty() {
        return this.usuario;
    }

    public StringProperty passProperty() {
        return this.pass;
    }
}
