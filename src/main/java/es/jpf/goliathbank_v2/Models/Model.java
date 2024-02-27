package es.jpf.goliathbank_v2.Models;

import es.jpf.goliathbank_v2.Views.FactoriaView;
import es.jpf.goliathbank_v2.Views.TipoCuenta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private final FactoriaView factoriaView;
    private final DatabaseDriverHibernate databaseDriver;
    private TipoCuenta tipoCuentaLogin = TipoCuenta.CLIENTE;

    private final ObservableList<Transaccion> ultimasTransacciones;
    private final ObservableList<Transaccion> todasTransacciones;
    private final ObservableList<Cuenta> cuentas;

    //Sección Datos Cliente
    private final Cliente cliente;
    private boolean clienteLogeado;
    //Sección Datos Admin
    private final Admin admin;
    private boolean adminLogeado;

    private Cliente clienteActual;
    private Admin adminActual;

    private Model(){

        this.factoriaView = new FactoriaView();
        this.databaseDriver = new DatabaseDriverHibernate();
        //Sección Datos Cliente
        this.clienteLogeado = false;
        this.cliente = new Cliente("","","",null,null);
        this.ultimasTransacciones = FXCollections.observableArrayList();
        this.todasTransacciones = FXCollections.observableArrayList();
        this.cuentas = FXCollections.observableArrayList();
        //Sección Datos Admin
        this.adminLogeado = false;
        this.admin = new Admin("","");
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }
    public FactoriaView getFactoriaView(){
        return factoriaView;
    }

    public DatabaseDriverHibernate getDatabaseDriver() {return databaseDriver;}

    public boolean getClienteLogeado(){return this.clienteLogeado;}

    public boolean getAdminLogeado() {
        return adminLogeado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ObservableList<Transaccion> getUltimasTransacciones() {
        return ultimasTransacciones;
    }
    public void setUltimasTransacciones(){
        ultimasTransacciones.clear();
        crearTransaccion(this.ultimasTransacciones, 5);
    }

    public void setTodasTransacciones(){
        System.out.println("Iniciando carga de todas las transacciones...");
        todasTransacciones.clear();
        crearTransaccion(this.todasTransacciones, 250);
        System.out.println("Carga de transacciones completada. Número de transacciones: " + todasTransacciones.size());
    }
    public void setCuentas(){
        cuentas.clear();
        crearCuenta(this.cuentas);
    }

    public ObservableList<Transaccion> getTodasTransacciones() {
        return todasTransacciones;
    }
    public ObservableList<Cuenta> getCuentas(){return cuentas;}
    public void evaluarCredencialesCliente(String email, String password) {
        ResultSet resultSet = null;
        try {
            resultSet = databaseDriver.getClientDatos(email, password);
            if (resultSet.next()) {
                String nombre = resultSet.getString("NOMBRE");
                String apellidos = resultSet.getString("APELLIDOS");
                String numTlfn = resultSet.getString("NUM_TLFN");

                this.cliente.nameProperty().set(nombre);
                this.cliente.apellidosProperty().set(apellidos);
                this.cliente.movilProperty().set(numTlfn);

                this.clienteLogeado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }
    }

    public void evaluarCredencialesAdmin(String email, String password) {
        ResultSet resultSet = null;
        try {
            resultSet = databaseDriver.getAdminDatos(email, password);
            if (resultSet.next()) {
                String usuario = resultSet.getString("USUARIO");
                String pass = resultSet.getString("PASS");

                this.admin.usuarioProperty().set(usuario);
                this.admin.passProperty().set(pass);

                this.adminLogeado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al evaluar credenciales de admin: " + e.getMessage());
        } finally {
            closeResultSet(resultSet);
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void crearTransaccion(ObservableList<Transaccion> transacciones, int limite){
        System.out.println("Iniciando creación de transacciones...");
        ResultSet resultSet = databaseDriver.getTransaccion(Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorMovil(this.cliente.movilProperty().get()),limite);
        System.out.println(this.cliente.movilProperty().get());
        try{
            while (resultSet.next()){
                int id_emisor = resultSet.getInt("ID_EMISOR");
                int id_receptor = resultSet.getInt("ID_RECEPTOR");
                double cantidad = resultSet.getDouble("CANTIDAD");
                String[] dateParts = resultSet.getString("FECHA").split("-");
                LocalDate fecha = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String mensaje = resultSet.getString("NOTA");
                String nombreRemitente = getDatabaseDriver().obtenerNombreUsuario(id_emisor);
                String nombreDestinatario = getDatabaseDriver().obtenerNombreUsuario(id_receptor);
                Transaccion transaccion = new Transaccion(id_emisor,id_receptor,cantidad,fecha,mensaje);
                transaccion.setNombreRemitente(nombreRemitente);
                transaccion.setNombreDestinatario(nombreDestinatario);
                transacciones.add(transaccion);
            }
            System.out.println("Creación de transacciones completada. Número de transacciones creadas: " + transacciones.size());
        }catch (Exception e){
            System.out.println("Error al crear transacciones:");
            e.printStackTrace();
        } finally {
            // Asegurémonos de cerrar el ResultSet después de su uso
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void crearCuenta(ObservableList<Cuenta> cuentas){
        System.out.println("Iniciando creación de cuentas...");
        ResultSet resultSet = databaseDriver.getCuenta(Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorMovil(this.cliente.movilProperty().get()));
        System.out.println(this.cliente.movilProperty().get());
        try {
            while (resultSet.next()){
                int id_emisor = resultSet.getInt("ID_USUARIO");
                String numCuenta = resultSet.getString("NUM_CUENTA");
                double saldo = resultSet.getDouble("SALDO");
                cuentas.add(new Cuenta(id_emisor,numCuenta,saldo));
            }
        }catch (Exception e){
            System.out.println("Error al crear cuentas:");
            e.printStackTrace();
        }
    }

    public boolean crearNuevoUsuario(String email, String password, String name, String apellidos, String movil){
        return databaseDriver.insertarUsuario(email, password, name, apellidos, movil);
    }

    public boolean crearNuevaCuenta(int idUsuario, String numCuenta, BigDecimal saldo){
        return databaseDriver.insertarCuenta(idUsuario, numCuenta, saldo);
    }

    public void setUsuarioActual(Cliente cliente) {
        this.clienteActual = cliente;
    }

    public void setUsuarioActual(Admin admin) {
        this.adminActual = admin;
    }

    public Cliente getUsuarioActualCliente() {
        return clienteActual;
    }

    public Admin getUsuarioActualAdmin() {
        return adminActual;
    }
}
