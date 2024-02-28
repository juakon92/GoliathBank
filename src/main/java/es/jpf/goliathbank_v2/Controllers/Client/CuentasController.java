package es.jpf.goliathbank_v2.Controllers.Client;

import es.jpf.goliathbank_v2.Models.Cuenta;
import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Views.CuentaCeldaFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.PropertySheet;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CuentasController implements Initializable {
    public ListView<Cuenta> listado_cuentas;
    public ComboBox<String> seleccion_cuenta;
    public TextField cajaCantidad;
    public CheckBox checkbox_add;
    public CheckBox checkbox_less;
    public Button bntOk;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarListadoCuentas();
        listado_cuentas.setItems(Model.getInstance().getCuentas());
        listado_cuentas.setCellFactory(e -> new CuentaCeldaFactory());
        rellenarComboBox();
        bntOk.setOnAction(event -> {
            try {
                realizarOperacion();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void cargarListadoCuentas(){
        if(Model.getInstance().getCuentas().isEmpty()){
            Model.getInstance().setCuentas();
        }
    }

    private void rellenarComboBox(){
        int idUsuario = Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorMovil(Model.getInstance().getUsuarioActualCliente().getMovil());

        System.out.println(idUsuario);

        if (idUsuario != -1) {
            Model.getInstance().getCuentas().forEach(cuenta -> {
                if (cuenta.getDuenio() == idUsuario) {
                    seleccion_cuenta.getItems().add(cuenta.getNumCuenta());
                }
            });
        } else {
            System.out.println("No se pudo obtener el ID del usuario.");
        }
    }
    private void realizarOperacion() throws SQLException {
        String numCuentaSeleccionada = seleccion_cuenta.getValue();
        int idCuenta = Model.getInstance().getDatabaseDriver().obtenerIdCuenta(numCuentaSeleccionada);
        Cuenta cuentaSeleccionada = null;

        for (Cuenta cuenta : Model.getInstance().getCuentas()) {
            if (cuenta.getNumCuenta().equals(numCuentaSeleccionada)) {
                cuentaSeleccionada = cuenta;
                break;
            }
        }
        if (cuentaSeleccionada != null) {
            BigDecimal cantidad;
            try {
                cantidad = new BigDecimal(cajaCantidad.getText());
            } catch (NumberFormatException e) {
                mostrarVentanaError("Error", "Cantidad inválida. Ingrese un número válido.");
                return;
            }

            if (checkbox_add.isSelected()) {
                // Realizar la operación de agregar fondos
                BigDecimal nuevoSaldo = Model.getInstance().getDatabaseDriver().obtenerSaldoCuenta(idCuenta).add(cantidad);
                if (Model.getInstance().getDatabaseDriver().actualizarSaldoCuentaPorID(idCuenta, nuevoSaldo)) {
                    mostrarVentanaCorrecto("Operación Exitosa", "Se ha añadido fondos a la cuenta correctamente.");
                } else {
                    mostrarVentanaError("Error", "Error al actualizar el saldo de la cuenta.");
                }
            } else if (checkbox_less.isSelected()) {
                System.out.println(cuentaSeleccionada.getBalance());
                // Realizar la operación de retirar fondos
                if (cuentaSeleccionada.getBalance() >= 0) {
                    BigDecimal nuevoSaldo = Model.getInstance().getDatabaseDriver().obtenerSaldoCuenta(idCuenta).subtract(cantidad);
                    System.out.println(nuevoSaldo);
                    if (Model.getInstance().getDatabaseDriver().actualizarSaldoCuentaPorID(idCuenta, nuevoSaldo)) {
                        mostrarVentanaCorrecto("Operación Exitosa", "Se ha retirado fondos de la cuenta correctamente.");
                    } else {
                        mostrarVentanaError("Error", "Error al actualizar el saldo de la cuenta.");
                    }
                } else {
                    mostrarVentanaError("Error", "Fondos insuficientes para realizar la operación.");
                }
            } else {
                mostrarVentanaError("Error", "Seleccione una operación para continuar.");
            }

            // Limpiar el ComboBox y la cajaCantidad después de la operación
            seleccion_cuenta.getSelectionModel().clearSelection();
            cajaCantidad.clear();
        } else {
            mostrarVentanaError("Error", "No se ha seleccionado ninguna cuenta.");
        }
    }
    private void mostrarVentanaCorrecto(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarVentanaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
