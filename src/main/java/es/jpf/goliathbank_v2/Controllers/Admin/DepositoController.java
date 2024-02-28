package es.jpf.goliathbank_v2.Controllers.Admin;

import es.jpf.goliathbank_v2.Models.Cliente;
import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Views.ClienteCeldaFactory;
import es.jpf.goliathbank_v2.Views.TransaccionCeldaFactory;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DepositoController implements Initializable {
    public TextField cajaMovil;
    public Button btnBuscar;
    public ListView<Cliente> listado_resultados;
    public TextField cajaCantidad;
    public Button btnDeposito;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listado_resultados.setItems(FXCollections.observableArrayList());
        listado_resultados.setCellFactory(e -> new ClienteCeldaFactory());
        btnBuscar.setOnAction(even -> buscarCuenta());
        btnDeposito.setOnAction(event -> depositar());
    }

    public void buscarCuenta() {
        String numCuenta = cajaMovil.getText();
        if (numCuenta.isEmpty()) {
            Cliente clienteError = new Cliente("No coincide", "No coincide", "No coincide", "No coincide", "No coincide");

            listado_resultados.getItems().clear();
            listado_resultados.getItems().add(clienteError);
        }else {
            int idUsuario = Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorCuenta(numCuenta);
            listado_resultados.getItems().clear();
            if (idUsuario != -1) {
                Cliente cliente = Model.getInstance().getDatabaseDriver().obtenerClientePorId(idUsuario);

                listado_resultados.getItems().add(cliente);
            } else {
                Cliente clienteError = new Cliente("No encontrado", "No encontrado", "No encontrado", "No encontrado", "No encontrado");

                listado_resultados.getItems().add(clienteError);
            }
        }
    }



    public void depositar(){
        String numCuenta = cajaMovil.getText();
        int idCuenta = Model.getInstance().getDatabaseDriver().obtenerIdCuenta(numCuenta);
        BigDecimal cantidad = BigDecimal.ZERO;

        if (!cajaCantidad.getText().isEmpty()) {
            try {
                cantidad = new BigDecimal(cajaCantidad.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            BigDecimal saldoActual = Model.getInstance().getDatabaseDriver().obtenerSaldoCuenta(idCuenta);

            BigDecimal nuevoSaldo = saldoActual.add(cantidad);

            boolean exitoDeposito = Model.getInstance().getDatabaseDriver().actualizarSaldoCuentaPorID(idCuenta, nuevoSaldo);

            if (exitoDeposito) {
                buscarCuenta();
            } else {
                System.out.println("Error al realizar el depósito.");
            }
        }
    }
}
