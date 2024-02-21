package es.jpf.goliathbank_v2.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Cliente {
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty name;
    private final StringProperty apellidos;
    private final StringProperty movil;
    private int id;

    public Cliente(String email, String password, String name, String apellidos, String movil) {
        this.email = new SimpleStringProperty(this, "Email", email);
        this.password = new SimpleStringProperty(this, "Password", password);
        this.name = new SimpleStringProperty(this, "Nombre", name);
        this.apellidos = new SimpleStringProperty(this, "Apellidos", apellidos);
        this.movil = new SimpleStringProperty(this, "Movil", movil);
    }

    public StringProperty emailProperty() {
        return this.email;
    }

    public StringProperty passwordProperty() {
        return this.password;
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public StringProperty apellidosProperty(){return this.apellidos;}

    public StringProperty movilProperty() {
        return this.movil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
