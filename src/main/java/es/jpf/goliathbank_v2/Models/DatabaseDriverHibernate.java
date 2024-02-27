package es.jpf.goliathbank_v2.Models;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDriverHibernate{

    public ResultSet getClientDatos(String email, String password) {
        return null;
    }

    public ResultSet getAdminDatos(String email, String password) {
        return null;
    }

    public ResultSet getTransaccion(int idUsuario, int limite){
        return null;
    }

    public int obtenerIdUsuarioPorMovil(String s) {
        return 0;
    }

    public String obtenerNombreUsuario(int idEmisor) {
        return null;
    }

    public ResultSet getCuenta(int i) {
        return null;
    }

    public boolean insertarUsuario(String email, String password, String name, String apellidos, String movil) {
        return false;
    }

    public boolean insertarCuenta(int idUsuario, String numCuenta, BigDecimal saldo) {
        return false;
    }
}
