package es.jpf.goliathbank_v2.Models;

import javafx.beans.property.*;

public class Cuenta {
    private final IntegerProperty duenio;
    private final StringProperty numCuenta;
    private final DoubleProperty balance;

    public Cuenta(int duenio, String numCuenta, double balance){
        this.duenio = new SimpleIntegerProperty(this,"Dueño", duenio);
        this.numCuenta = new SimpleStringProperty(this,"Numero de Cuenta", numCuenta);
        this.balance = new SimpleDoubleProperty(this,"Balance", balance);
    }

    public IntegerProperty duenioProperty(){return duenio;}
    public StringProperty numCuentaProperty(){return numCuenta;}
    public DoubleProperty balanceProperty(){return balance;}
}
