package es.jpf.goliathbank_v2.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
