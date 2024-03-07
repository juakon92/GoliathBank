package es.jpf.goliathbank_v2.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "ID_USUARIO")
    private int duenio;

    @Column(name = "NUM_CUENTA")
    private String numCuenta;

    @Column(name = "SALDO")
    private BigDecimal balance;

    public Cuenta() {}

    public Cuenta(int duenio, String numCuenta, BigDecimal balance) {
        this.duenio = duenio;
        this.numCuenta = numCuenta;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuenio() {
        return duenio;
    }

    public void setDuenio(int duenio) {
        this.duenio = duenio;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
