package es.jpf.goliathbank_v2.Controllers.Client;

import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Views.ClientMenuOpciones;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button btnDashboard;
    public Button btnTransacciones;
    public Button btnCuentas;
    public Button btnPerfil;
    public Button btnLogOut;
    public Button btnInformar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    private void addListener(){
        btnDashboard.setOnAction(event -> onDashboard());
        btnTransacciones.setOnAction(event -> onTransacciones());
        btnCuentas.setOnAction(even -> onCuentas());
        btnLogOut.setOnAction(even -> onExit());
    }

    private void onDashboard(){
        Model.getInstance().getFactoriaView().getItemSelecClienteMenu().set(ClientMenuOpciones.DASHBOARD);
    }

    private void onTransacciones(){
        Model.getInstance().getFactoriaView().getItemSelecClienteMenu().set(ClientMenuOpciones.TRANSACCIONES);
    }

    private void onCuentas(){
        Model.getInstance().getFactoriaView().getItemSelecClienteMenu().set(ClientMenuOpciones.CUENTAS);
    }

    private void onExit(){
        btnLogOut.getScene().getWindow().hide();
        Model.getInstance().getFactoriaView().mostrarVentanaLogin();
    }
}
