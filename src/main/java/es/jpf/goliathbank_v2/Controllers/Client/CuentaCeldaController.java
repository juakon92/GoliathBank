package es.jpf.goliathbank_v2.Controllers.Client;

import es.jpf.goliathbank_v2.Models.Cuenta;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CuentaCeldaController implements Initializable {
    public Label etiquetaNumCuenta;
    public Label etiquetaSaldo;
    private final  Cuenta cuenta;

    public CuentaCeldaController(Cuenta cuenta){this.cuenta = cuenta;}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        etiquetaNumCuenta.setText(cuenta.getNumCuenta());
        etiquetaSaldo.setText(String.valueOf(cuenta.getBalance()));
    }
}
