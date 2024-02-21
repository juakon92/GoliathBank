package es.jpf.goliathbank_v2.Controllers;

import es.jpf.goliathbank_v2.Models.Admin;
import es.jpf.goliathbank_v2.Models.Cliente;
import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Views.TipoCuenta;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<TipoCuenta> tipo_cuenta;
    public Label email_lbl;
    public TextField cajaEmail;
    public PasswordField cajaPass;
    public Button btnLogin;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipo_cuenta.setItems(FXCollections.observableArrayList(TipoCuenta.CLIENTE,TipoCuenta.ADMIN));
        tipo_cuenta.setValue(Model.getInstance().getFactoriaView().getAccesoTipoCuenta());
        tipo_cuenta.valueProperty().addListener(observable -> Model.getInstance().getFactoriaView().setAccesoTipoCuenta(tipo_cuenta.getValue()));
        btnLogin.setOnAction(event -> onLogin());
    }

    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        if(Model.getInstance().getFactoriaView().getAccesoTipoCuenta() == TipoCuenta.CLIENTE){
            Model.getInstance().evaluarCredencialesCliente(cajaEmail.getText(),cajaPass.getText());
            if (Model.getInstance().getClienteLogeado()){
                Cliente usuarioActual = Model.getInstance().getUsuarioActualCliente();
                Model.getInstance().setUsuarioActual(usuarioActual);

                Model.getInstance().getFactoriaView().mostrarVentanaCliente();
                Model.getInstance().getFactoriaView().cerrarStage(stage);
            }else{
                cajaEmail.setText("");
                cajaPass.setText("");
                error_lbl.setText("No existen las credenciales insertadas");
            }
        }else {
            Model.getInstance().evaluarCredencialesAdmin(cajaEmail.getText(),cajaPass.getText());
            if(Model.getInstance().getAdminLogeado()){
                Admin usuarioActual = Model.getInstance().getUsuarioActualAdmin();
                Model.getInstance().setUsuarioActual(usuarioActual);

                Model.getInstance().getFactoriaView().mostrarVentanaAdmin();
                Model.getInstance().getFactoriaView().cerrarStage(stage);
            }else{
                cajaEmail.setText("");
                cajaPass.setText("");
                error_lbl.setText("No existen las credenciales insertadas");
            }
        }
    }
}
