package es.jpf.goliathbank_v2.Models;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {
    private Connection con;
    String databaseName = "goliathBank";
    String databaseUser = "root";
    String databasePass = "deca4951_";
    String url = "jdbc:mysql://localhost/" + databaseName;

    public DatabaseDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            crearDatabase();
            con = DriverManager.getConnection(url,databaseUser,databasePass);
            initDatabase();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /*
    * Sección Cliente
    * */
    public ResultSet getClientDatos(String email, String password){
        try {
            String query = "SELECT * FROM usuario WHERE EMAIL=? AND PASS=?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * Sección Admin
    * */
    public ResultSet getAdminDatos(String email, String password){
        try {
            String query = "SELECT * FROM admin WHERE usuario = ? AND pass = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            return preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    /*
    * Métodos de Utilidad
    * */

    public boolean insertarUsuario(String email, String password, String firstName, String lastName, String numTlfn) {
        try {
            String query = "INSERT INTO usuario (EMAIL, PASS, NOMBRE, APELLIDOS, NUM_TLFN) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, lastName);
                preparedStatement.setString(5, numTlfn);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean insertarTransaccion(int idEmisor, int idReceptor, BigDecimal cantidad, Date fecha) {
        try {
            String query = "INSERT INTO transaccion (ID_EMISOR, ID_RECEPTOR, CANTIDAD, FECHA) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, idEmisor);
                preparedStatement.setInt(2, idReceptor);
                preparedStatement.setBigDecimal(3, cantidad);
                preparedStatement.setDate(4, fecha);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertarCuenta(int idUsuario, String numCuenta, BigDecimal saldo) {
        try {
            String query = "INSERT INTO cuenta (ID_USUARIO, NUM_CUENTA, SALDO) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, idUsuario);
                preparedStatement.setString(2, numCuenta);
                preparedStatement.setBigDecimal(3, saldo);

                int filasAfectadas = preparedStatement.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean actualizarSaldoCuenta(int idUsuario, BigDecimal newBalance) {
        try {
            if (con.getAutoCommit() == true) {
                con.setAutoCommit(false);
            }

            String query = "UPDATE cuenta SET SALDO = ? WHERE ID_USUARIO = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setBigDecimal(1, newBalance);
                preparedStatement.setInt(2, idUsuario);

                int filasAfectadas = preparedStatement.executeUpdate();

                System.out.println("Filas afectadas por la actualización: " + filasAfectadas);

                con.commit();

                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
    public boolean actualizarSaldoCuentaPorID(int idCuenta, BigDecimal newBalance) {
        try {
            if (con.getAutoCommit() == true) {
                con.setAutoCommit(false);
            }

            String query = "UPDATE cuenta SET SALDO = ? WHERE ID = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setBigDecimal(1, newBalance);
                preparedStatement.setInt(2, idCuenta);

                int filasAfectadas = preparedStatement.executeUpdate();

                System.out.println("Filas afectadas por la actualización: " + filasAfectadas);

                con.commit();

                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
    public boolean realizarTransaccion(int idEmisor, int idReceptor, BigDecimal cantidad) {
        try {
            con.setAutoCommit(false);
            BigDecimal emisorBalance = getSaldoCuentaPorUsuario(idEmisor);
            if (emisorBalance.compareTo(cantidad) < 0) {
                con.rollback();
                return false;
            }

            if (insertarTransaccion(idEmisor, idReceptor, cantidad, new Date(System.currentTimeMillis()))) {
                BigDecimal nuevoSaldoEmisor = emisorBalance.subtract(cantidad);
                BigDecimal receptorBalance = getSaldoCuentaPorUsuario(idReceptor);
                BigDecimal nuevoSaldoReceptor = receptorBalance.add(cantidad);

                boolean actualizacionEmisor = actualizarSaldoCuenta(idEmisor, nuevoSaldoEmisor);
                boolean actualizacionReceptor = actualizarSaldoCuenta(idReceptor, nuevoSaldoReceptor);

                if (actualizacionEmisor && actualizacionReceptor) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            } else {
                con.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                System.out.println(con.getAutoCommit());
                con.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
    public BigDecimal getSaldoCuenta(int idCuenta) throws SQLException {
        String query = "SELECT SALDO FROM cuenta WHERE ID = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, idCuenta);
            System.out.println("Consulta SQL: " + preparedStatement.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("SALDO");
                }
            }
        }
        throw new SQLException("No se pudo obtener el saldo de la cuenta");
    }
    public BigDecimal getSaldoCuentaPorUsuario(int idUsuario) throws SQLException {
        String query = "SELECT SALDO FROM cuenta WHERE ID_USUARIO = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, idUsuario);
            System.out.println("Consulta SQL: " + preparedStatement.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("SALDO");
                }
            }
        }
        throw new SQLException("No se pudo obtener el saldo de la cuenta");
    }

    public int obtenerIdUsuario(String email) {
        try {
            String query = "SELECT ID FROM usuario WHERE EMAIL = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("ID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int obtenerIdUsuarioPorMovil(String movil) {
        try {
            String query = "SELECT ID FROM usuario WHERE NUM_TLFN = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, movil);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("ID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String obtenerNombreUsuario(int idUsuario){
        try {
            String query = "SELECT NOMBRE FROM usuario WHERE ID = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, idUsuario);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("NOMBRE");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int obtenerIdUsuarioPorCuenta(String numCuenta) {
        try {
            String query = "SELECT ID_USUARIO FROM cuenta WHERE NUM_CUENTA = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, numCuenta);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("ID_USUARIO");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int obtenerIdCuenta(String numCuenta){
        try {
            String query = "SELECT ID FROM cuenta WHERE NUM_CUENTA = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, numCuenta);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("ID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Cliente obtenerClientePorId(int idUsuario) {
        try {
            String query = "SELECT * FROM usuario WHERE ID = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, idUsuario);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String nombre = resultSet.getString("NOMBRE");
                        String apellidos = resultSet.getString("APELLIDOS");
                        String movil = resultSet.getString("NUM_TLFN");
                        String email = resultSet.getString("EMAIL");

                        Cliente cliente = new Cliente(email, null, nombre, apellidos, movil);
                        cliente.setId(idUsuario);
                        return cliente;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();

        try {
            String query = "SELECT * FROM usuario";
            try (PreparedStatement preparedStatement = con.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String nombre = resultSet.getString("NOMBRE");
                    String apellidos = resultSet.getString("APELLIDOS");
                    String movil = resultSet.getString("NUM_TLFN");
                    String email = resultSet.getString("EMAIL");

                    Cliente cliente = new Cliente(email, null, nombre, apellidos, movil);
                    clientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }
    public ResultSet getTransaccion(int idUsuario, int limite){
        PreparedStatement preparedStatement;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM transaccion WHERE ID_EMISOR=? OR ID_RECEPTOR=? LIMIT ?";
            preparedStatement = this.con.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, idUsuario);
            preparedStatement.setInt(3, limite);

            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            System.out.println("Error al ejecutar la consulta SQL:");
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getCuenta(int idUsuario){
        PreparedStatement preparedStatement;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM cuenta WHERE ID_USUARIO=?";
            preparedStatement = this.con.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);

            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            System.out.println("Error al ejecutar la consulta SQL:");
            e.printStackTrace();
        }
        return resultSet;
    }
    private void initDatabase(){
        try {
            crearTablaUsuario();
            crearTablaAdmin();
            crearTablaCuenta();
            crearTablaTransaccion();
            if (!adminYaInsertado()) {
                insertarAdmin("admin", "admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void crearDatabase()throws SQLException{
        Connection conAux = DriverManager.getConnection("jdbc:mysql://localhost/",databaseUser,databasePass);
        String query = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (PreparedStatement preparedStatement = conAux.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }
    private void crearTablaUsuario() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS usuario (ID INT PRIMARY KEY AUTO_INCREMENT, EMAIL VARCHAR(255), PASS VARCHAR(255), NOMBRE VARCHAR(255), APELLIDOS VARCHAR(255), NUM_TLFN VARCHAR(15))";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    private void crearTablaAdmin() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS admin (ID INT PRIMARY KEY AUTO_INCREMENT, usuario VARCHAR(255), pass VARCHAR(255))";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }
    private void crearTablaCuenta() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS cuenta (ID INT PRIMARY KEY AUTO_INCREMENT, ID_USUARIO INT, NUM_CUENTA VARCHAR(20), SALDO DECIMAL(10, 2), FOREIGN KEY (ID_USUARIO) REFERENCES usuario(ID))";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    private void crearTablaTransaccion() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS transaccion (ID INT PRIMARY KEY AUTO_INCREMENT, ID_EMISOR INT, ID_RECEPTOR INT, CANTIDAD DECIMAL(10, 2), FECHA DATE, NOTA VARCHAR(255), FOREIGN KEY (ID_EMISOR) REFERENCES usuario(ID), FOREIGN KEY (ID_RECEPTOR) REFERENCES usuario(ID))";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }
    public boolean insertarAdmin(String usuario, String password) {
        try {
            String query = "INSERT INTO admin (usuario, pass) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, usuario);
                preparedStatement.setString(2, password);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean adminYaInsertado() {
        try {
            String query = "SELECT COUNT(*) FROM admin";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int rowCount = resultSet.getInt(1);
                return rowCount > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
