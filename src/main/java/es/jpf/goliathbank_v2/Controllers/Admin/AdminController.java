package es.jpf.goliathbank_v2.Controllers.Admin;

import es.jpf.goliathbank_v2.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getFactoriaView().getItemSelectAdminMenu().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case CLIENTES -> admin_parent.setCenter(Model.getInstance().getFactoriaView().getClientesView());
                case DEPOSITO -> admin_parent.setCenter(Model.getInstance().getFactoriaView().getDepositoView());
                default -> admin_parent.setCenter(Model.getInstance().getFactoriaView().getCrearClienteView());
            }
        });
    }
}
