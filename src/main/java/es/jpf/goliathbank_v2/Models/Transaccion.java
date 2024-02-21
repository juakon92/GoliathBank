package es.jpf.goliathbank_v2.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Transaccion {
    private final IntegerProperty origen;
    private final IntegerProperty destino;
    private final DoubleProperty cantidad;
    private final ObjectProperty<LocalDate> fecha;
    private final StringProperty mensaje;
    private StringProperty nombreRemitente;
    private StringProperty nombreDestinatario;

    public Transaccion(int origen, int destino, double cantidad, LocalDate fecha, String mensaje){
        this.origen = new SimpleIntegerProperty(this,"Origen", origen);
        this.destino = new SimpleIntegerProperty(this,"Destino", destino);
        this.cantidad = new SimpleDoubleProperty(this,"Cantidad", cantidad);
        this.fecha = new SimpleObjectProperty<>(this, "Fecha",fecha);
        this.mensaje = new SimpleStringProperty(this, "Mensaje", mensaje);
        this.nombreRemitente = new SimpleStringProperty(this,"NombreRemitente","");
        this.nombreDestinatario = new SimpleStringProperty(this,"NombreDestinatario","");
    }

    public IntegerProperty origenProperty(){return this.origen;}

    public IntegerProperty destinoProperty(){return this.destino;}

    public DoubleProperty cantidadProperty(){return this.cantidad;}

    public ObjectProperty<LocalDate> fechaProperty(){return this.fecha;}

    public StringProperty mensajeProperty(){return this.mensaje;}

    public StringProperty nombreRemitenteProperty() {return nombreRemitente;}
    public StringProperty nombreDestinatarioProperty() {return nombreDestinatario;}

    public String getNombreRemitente() {return nombreRemitente.get();}

    public void setNombreRemitente(String nombreRemitente) {this.nombreRemitente.set(nombreRemitente);}

    public String getNombreDestinatario() {return nombreDestinatario.get();}

    public void setNombreDestinatario(String nombreDestinatario) {this.nombreDestinatario.set(nombreDestinatario);}
}
