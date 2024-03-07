package es.jpf.goliathbank_v2.Models;

import es.jpf.goliathbank_v2.Views.FactoriaView;
import es.jpf.goliathbank_v2.Views.TipoCuenta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

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
    private Admin admin;
    private boolean adminLogeado;

    private Cliente clienteActual;
    private Admin adminActual;

    private Model(){

        this.factoriaView = new FactoriaView();
        this.databaseDriver = new DatabaseDriverHibernate();
        //Sección Datos Cliente
        this.clienteLogeado = false;
        this.cliente = new Cliente("","","","","");
        this.setUsuarioActual(cliente);
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
        try {
            Cliente cliente = databaseDriver.getClientDatos(email,password);

            if (cliente != null){
                this.clienteActual = cliente;
                this.clienteLogeado = true;
                System.out.println("Número de móvil: " + cliente.getMovil());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al evaluar credenciales de cliente: " + e.getMessage());
        }
    }

    public void evaluarCredencialesAdmin(String email, String password) {
        try {
            Admin admin = databaseDriver.getAdminDatos(email, password);

            if (admin != null) {
                this.admin = admin;
                this.adminLogeado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al evaluar credenciales de admin: " + e.getMessage());
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

    private void crearTransaccion(ObservableList<Transaccion> transacciones, int limite) {
        System.out.println("Iniciando creación de transacciones...");

        try {
            int idUsuario = Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorMovil(this.cliente.getMovil());
            List<Transaccion> listaTransacciones = Model.getInstance().getDatabaseDriver().getTransaccion(idUsuario, limite);

            for (Transaccion transaccion : listaTransacciones) {
                int id_emisor = transaccion.getOrigen();
                int id_receptor = transaccion.getDestino();
                double cantidad = transaccion.getCantidad();
                LocalDate fecha = transaccion.getFecha();
                String mensaje = transaccion.getMensaje();
                String nombreRemitente = Model.getInstance().getDatabaseDriver().obtenerNombreUsuario(id_emisor);
                String nombreDestinatario = Model.getInstance().getDatabaseDriver().obtenerNombreUsuario(id_receptor);

                Transaccion nuevaTransaccion = new Transaccion(id_emisor, id_receptor, cantidad, fecha, mensaje);
                nuevaTransaccion.setNombreRemitente(nombreRemitente);
                nuevaTransaccion.setNombreDestinatario(nombreDestinatario);

                transacciones.add(nuevaTransaccion);
            }

            System.out.println("Creación de transacciones completada. Número de transacciones creadas: " + transacciones.size());
        } catch (Exception e) {
            System.out.println("Error al crear transacciones:");
            e.printStackTrace();
        }
    }


    private void crearCuenta(ObservableList<Cuenta> cuentas) {
        System.out.println("Iniciando creación de cuentas...");

        try {
            int idUsuario = Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorMovil(this.cliente.getMovil());
            System.out.println(idUsuario);
            List<Cuenta> listaCuentas = Model.getInstance().getDatabaseDriver().getCuenta(idUsuario);

            cuentas.addAll(listaCuentas);

            System.out.println("Creación de cuentas completada. Número de cuentas creadas: " + cuentas.size());
        } catch (Exception e) {
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
