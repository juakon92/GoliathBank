package es.jpf.goliathbank_v2.Views;

import es.jpf.goliathbank_v2.Controllers.Admin.AdminController;
import es.jpf.goliathbank_v2.Controllers.Client.ClientController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FactoriaView {
    private TipoCuenta accesoTipoCuenta;
    // Vista Cliente
    private final ObjectProperty<ClientMenuOpciones> itemSelecClienteMenu;
    private AnchorPane dashboarView;
    private AnchorPane transaccionesView;
    private AnchorPane cuentasView;

    //Vista Admin
    private final ObjectProperty<AdminMenuOpciones> itemSelectAdminMenu;
    private AnchorPane crearClienteView;
    private AnchorPane clientesView;
    private AnchorPane depositoView;

    public FactoriaView(){
        this.accesoTipoCuenta = TipoCuenta.CLIENTE;
        this.itemSelecClienteMenu = new SimpleObjectProperty<>();
        this.itemSelectAdminMenu = new SimpleObjectProperty<>();
    }

    public TipoCuenta getAccesoTipoCuenta() {
        return accesoTipoCuenta;
    }

    public void setAccesoTipoCuenta(TipoCuenta accesoTipoCuenta) {
        this.accesoTipoCuenta = accesoTipoCuenta;
    }

    public ObjectProperty<ClientMenuOpciones> getItemSelecClienteMenu() {
        return itemSelecClienteMenu;
    }
    /*
    * Sección Vista Cliente
    * */

    public ObjectProperty<ClientMenuOpciones> itemSelecClienteMenuProperty() {
        return itemSelecClienteMenu;
    }

    public AnchorPane getDashboarView() {
        if(dashboarView == null){
            try {
                dashboarView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboarView;
    }

    public AnchorPane getTransaccionesView(){
        if(transaccionesView == null){
            try {
                transaccionesView = new FXMLLoader(getClass().getResource("/Fxml/Client/Transacciones.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return transaccionesView;
    }

    public AnchorPane getCuentasView() {
        if(cuentasView == null){
            try {
                cuentasView = new FXMLLoader(getClass().getResource("/Fxml/Client/Cuentas.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return cuentasView;
    }

    public void mostrarVentanaCliente(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        crearStage(loader);
    }
    /*
    * Sección Admin Vista
    * */
    public ObjectProperty<AdminMenuOpciones> getItemSelectAdminMenu(){

        return itemSelectAdminMenu;
    }

    public AnchorPane getCrearClienteView() {
        if(crearClienteView == null){
            try {
                crearClienteView = new FXMLLoader(getClass().getResource("/Fxml/Admin/CrearCliente.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return crearClienteView;
    }

    public AnchorPane getClientesView() {
        if(clientesView == null){
            try {
                clientesView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clientes.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return clientesView;
    }

    public AnchorPane getDepositoView() {
        if (depositoView == null){
            try {
                depositoView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Deposito.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return depositoView;
    }

    public void mostrarVentanaAdmin(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController controller = new AdminController();
        loader.setController(controller);
        crearStage(loader);
    }

    public void mostrarVentanaLogin(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        crearStage(loader);
    }
    private void crearStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Goliath Bank");
        stage.show();
    }

    public void cerrarStage(Stage stage){
        stage.close();
    }
}
