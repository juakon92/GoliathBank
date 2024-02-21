package es.jpf.goliathbank_v2.Controllers.Client;

import es.jpf.goliathbank_v2.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane client_parent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getFactoriaView().getItemSelecClienteMenu().addListener((observableValue, oldValue, newVal) -> {
            switch (newVal){
                case TRANSACCIONES -> client_parent.setCenter(Model.getInstance().getFactoriaView().getTransaccionesView());
                case CUENTAS-> client_parent.setCenter(Model.getInstance().getFactoriaView().getCuentasView());
                default -> client_parent.setCenter(Model.getInstance().getFactoriaView().getDashboarView());
            }
        });
    }
}
