package es.jpf.goliathbank_v2.Controllers.Admin;

import es.jpf.goliathbank_v2.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.security.SecureRandom;
import java.math.BigDecimal;
import java.util.ResourceBundle;

public class CrearClienteController implements Initializable {
    public TextField cajaName;
    public TextField cajaApellidos;
    public TextField cajaEmail;
    public PasswordField cajaPass;
    public TextField cajaMovil;
    public CheckBox cc_cuenta_box;
    public TextField cajaCC;
    public CheckBox ca_cuenta_box;
    public TextField cajaCA;
    public Button btnCrearCliente;
    public Label error_lbl;
    public Text etiquetaSaldo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ocultarElementos();

        cc_cuenta_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                generarCadenaAleatoria();
            } else {
                cajaCC.clear();
            }
        });
        ca_cuenta_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                etiquetaSaldo.setVisible(true);
                cajaCA.setVisible(true);
            } else {
                etiquetaSaldo.setVisible(false);
                cajaCA.setVisible(false);
            }
        });
        btnCrearCliente.setOnAction(event -> crearNuevoCliente());
    }

    public void crearNuevoCliente() {
        String email = cajaEmail.getText();
        String password = cajaPass.getText();
        String nombre = cajaName.getText();
        String apellidos = cajaApellidos.getText();
        String numTlfn = cajaMovil.getText();

        if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || numTlfn.isEmpty()) {
            mostrarVentanaError();
            return;
        }

        boolean exito = Model.getInstance().crearNuevoUsuario(email, password, nombre, apellidos, numTlfn);

        if (exito) {
            mostrarVentanaExito();

            int idUsuario = Model.getInstance().getDatabaseDriver().obtenerIdUsuario(email);
            String numCuenta = cajaCC.getText();

            if (ca_cuenta_box.isSelected()){
                BigDecimal saldoInicial = BigDecimal.ZERO;

                if (!cajaCA.getText().isEmpty()) {
                    try {
                        saldoInicial = new BigDecimal(cajaCA.getText());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                boolean exitoCuenta = Model.getInstance().crearNuevaCuenta(idUsuario, numCuenta, saldoInicial);

                if (exitoCuenta) {
                    mostrarVentanaExitoCuenta();
                } else {
                    mostrarVentanaErrorCuenta();
                }
            } else {
                boolean exitoCuenta = Model.getInstance().crearNuevaCuenta(idUsuario, numCuenta, BigDecimal.ZERO);

                if (exitoCuenta) {
                    mostrarVentanaExitoCuenta();
                } else {
                    mostrarVentanaErrorCuenta();
                }
            }
        } else {
            mostrarVentanaError();
        }
    }

    private void mostrarVentanaExito() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Usuario creado exitosamente.");

        Stage stage = (Stage) btnCrearCliente.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }

    private void mostrarVentanaExitoCuenta() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Cuenta creada exitosamente.");

        Stage stage = (Stage) btnCrearCliente.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }

    private void mostrarVentanaError() {
        error_lbl.setVisible(true);
    }

    private void mostrarVentanaErrorCuenta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error al crear la cuenta.");

        Stage stage = (Stage) btnCrearCliente.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }

    private void generarCadenaAleatoria() {
        SecureRandom random = new SecureRandom();
        int numeroAleatorio = 100000 + random.nextInt(900000);
        cajaCC.setText("ES95" + numeroAleatorio);
    }

    private void ocultarElementos(){
        error_lbl.setVisible(false);
        etiquetaSaldo.setVisible(false);
        cajaCA.setVisible(false);
    }
}
