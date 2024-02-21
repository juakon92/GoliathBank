package es.jpf.goliathbank_v2.Controllers.Admin;

import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Views.AdminMenuOpciones;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button btnNuevoClient;
    public Button btnCliente;
    public Button btnDeposito;
    public Button btnExit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    private void addListener(){
        btnNuevoClient.setOnAction(event -> onCrearCliente());
        btnCliente.setOnAction(event -> onClientes());
        btnDeposito.setOnAction(even -> onDeposito());
        btnExit.setOnAction(event -> onExit());
    }

    private void onCrearCliente(){
        Model.getInstance().getFactoriaView().getItemSelectAdminMenu().set(AdminMenuOpciones.CREAR_CLIENTE);
    }

    private void onClientes(){
        Model.getInstance().getFactoriaView().getItemSelectAdminMenu().set(AdminMenuOpciones.CLIENTES);
    }

    private void onDeposito(){
        Model.getInstance().getFactoriaView().getItemSelectAdminMenu().set(AdminMenuOpciones.DEPOSITO);
    }

    private void onExit(){
        btnExit.getScene().getWindow().hide();

        Model.getInstance().getFactoriaView().mostrarVentanaLogin();
    }
}
