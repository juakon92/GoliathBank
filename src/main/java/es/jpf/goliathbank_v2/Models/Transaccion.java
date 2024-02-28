package es.jpf.goliathbank_v2.Models;

import jakarta.persistence.*;
import javafx.beans.property.*;

import java.time.LocalDate;

@Entity
@Table(name = "transaccion")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "ID_EMISOR")
    private int origen;

    @Column(name = "ID_RECEPTOR")
    private int destino;

    @Column(name = "CANTIDAD")
    private double cantidad;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "NOTA")
    private String mensaje;

    @Transient
    private StringProperty nombreRemitente;

    @Transient
    private StringProperty nombreDestinatario;

    public Transaccion(){}

    public Transaccion(int origen, int destino, double cantidad, LocalDate fecha, String mensaje){
        this.origen = origen;
        this.destino = destino;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.nombreRemitente = new SimpleStringProperty(this, "NombreRemitente", "");
        this.nombreDestinatario = new SimpleStringProperty(this, "NombreDestinatario", "");
    }

    public int getId() {
        return id;
    }

    public int getOrigen() {
        return origen;
    }

    public int getDestino() {
        return destino;
    }

    public double getCantidad() {
        return cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getMensaje() {
        return mensaje;
    }
    @Transient
    public StringProperty nombreRemitenteProperty() {
        return nombreRemitente;
    }

    @Transient
    public StringProperty nombreDestinatarioProperty() {
        return nombreDestinatario;
    }

    @Transient
    public String getNombreRemitente() {
        return nombreRemitente.get();
    }

    public void setNombreRemitente(String nombreRemitente) {
        this.nombreRemitente.set(nombreRemitente);
    }

    @Transient
    public String getNombreDestinatario() {
        return nombreDestinatario.get();
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario.set(nombreDestinatario);
    }
}
