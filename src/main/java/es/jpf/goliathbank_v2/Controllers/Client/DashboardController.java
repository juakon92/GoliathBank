package es.jpf.goliathbank_v2.Controllers.Client;

import es.jpf.goliathbank_v2.Models.Cliente;
import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Models.Transaccion;
import es.jpf.goliathbank_v2.Views.TransaccionCeldaFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label etiquetaFech;
    public Label balance_Ahorros;
    public Label num_cuenta_ahorros;
    public ListView<Transaccion> lista_transacciones;
    public TextField cajaBeneficiario;
    public TextField cajaCantidad;
    public TextArea cajaNota;
    public Button btnEnviar;
    public Button btnAddCuenta;
    public CheckBox checkBoxAddCuenta;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        realizarConexion();
        checkBoxAddCuenta.selectedProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue) {
                generarCadenaAleatoria();
            } else {
                num_cuenta_ahorros.setText("");
            }
        });
        btnAddCuenta.setOnAction(event -> addCuenta());
        cargarUltimasTransacciones();
        lista_transacciones.setItems(Model.getInstance().getUltimasTransacciones());
        lista_transacciones.setCellFactory(e -> new TransaccionCeldaFactory());
        btnEnviar.setOnAction(event -> realizarBizum());
    }

    private void addCuenta(){
        int idUsuario = obtenerIdUsuarioPorMovil(Model.getInstance().getCliente().getMovil());
        String numCuenta = num_cuenta_ahorros.getText();
        if(checkBoxAddCuenta.isSelected()){
            BigDecimal saldoInicial = BigDecimal.ZERO;

            boolean exitoCuenta = Model.getInstance().crearNuevaCuenta(idUsuario, numCuenta, saldoInicial);

            if (exitoCuenta) {
                mostrarVentanaExitoCuenta();
            } else {
                mostrarVentanaErrorCuenta();
            }
        }
    }

    private void realizarBizum() {
        String beneficiarioMovil = cajaBeneficiario.getText();
        String mensaje = cajaNota.getText();
        BigDecimal cantidad = BigDecimal.ZERO;

        try {
            if (!cajaCantidad.getText().isEmpty()) {
                cantidad = new BigDecimal(cajaCantidad.getText());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        Cliente usuarioActual = Model.getInstance().getCliente();

        if (usuarioActual != null) {

            int idUsuarioEmisor = obtenerIdUsuarioPorMovil(Model.getInstance().getCliente().getMovil());
            int idUsuarioReceptor = obtenerIdUsuarioPorMovil(beneficiarioMovil);

            if (idUsuarioReceptor != -1) {

                String nombreReceptor = Model.getInstance().getDatabaseDriver().obtenerNombreUsuario(idUsuarioReceptor);
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar Bizum");
                confirmacion.setHeaderText("Estás seguro de realizar Bizum a " + nombreReceptor + "?");
                confirmacion.setContentText("Presiona 'ACEPTAR' para confirmar");

                ButtonType resultado = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

                if (resultado == ButtonType.OK){
                    if (Model.getInstance().getDatabaseDriver().realizarTransaccion(idUsuarioEmisor, idUsuarioReceptor, cantidad,mensaje)) {
                        cajaBeneficiario.clear();
                        cajaCantidad.clear();
                        cajaNota.clear();
                    } else {
                        System.out.println("Error al realizar la transacción Bizum.");
                    }
                }else {
                    System.out.println("Operación Bizum cancelada.");
                }
            } else {
                System.out.println("El beneficiario no existe.");
            }
        }
    }

    private void realizarConexion(){
        user_name.textProperty().bind(Bindings.concat("Hola, ").concat(Model.getInstance().getCliente().getName()));
        etiquetaFech.setText("Hoy, " + LocalDate.now());
    }
    private int obtenerIdUsuarioPorMovil(String movil) {
        return Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorMovil(movil);
    }
    private void cargarUltimasTransacciones(){
        if (Model.getInstance().getUltimasTransacciones().isEmpty()){
            Model.getInstance().setUltimasTransacciones();
        }
    }

    private void generarCadenaAleatoria() {
        SecureRandom random = new SecureRandom();
        int numeroAleatorio = 100000 + random.nextInt(900000);

        num_cuenta_ahorros.setText("ES95" + numeroAleatorio);
    }
    private void mostrarVentanaExitoCuenta() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Cuenta creada exitosamente.");

        Stage stage = (Stage) btnAddCuenta.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }

    private void mostrarVentanaErrorCuenta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error al crear la cuenta.");
        Stage stage = (Stage) btnAddCuenta.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }
}
