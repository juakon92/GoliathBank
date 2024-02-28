package es.jpf.goliathbank_v2.Controllers.Admin;

import es.jpf.goliathbank_v2.Models.Cliente;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClienteCeldaController implements Initializable {
    public Label etiquetaName;
    public Label etiquetaApellidos;
    public Label etiquetaMovil;
    public Label etiquetaEmail;

    private final Cliente cliente;

    public ClienteCeldaController(Cliente cliente){
        this.cliente = cliente;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        etiquetaName.setText(cliente.getName());
        etiquetaApellidos.setText(cliente.getApellidos());
        etiquetaMovil.setText(cliente.getMovil());
        etiquetaEmail.setText(cliente.getEmail());
    }
}
