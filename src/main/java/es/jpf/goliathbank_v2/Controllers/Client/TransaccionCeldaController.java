package es.jpf.goliathbank_v2.Controllers.Client;

import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Models.Transaccion;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class TransaccionCeldaController implements Initializable {
    public FontAwesomeIconView in_icon;
    public FontAwesomeIconView out_icon;
    public Label etiquetaFechTran;
    public Label etiquetaRemitente;
    public Label etiquetaDestinatario;
    public Label etiquetaCantidad;

    private final Transaccion transaccion;

    public TransaccionCeldaController(Transaccion transaccion){
        this.transaccion = transaccion;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        etiquetaRemitente.textProperty().bind(transaccion.nombreRemitenteProperty());
        etiquetaCantidad.textProperty().bind(transaccion.cantidadProperty().asString());
        etiquetaDestinatario.textProperty().bind(transaccion.nombreDestinatarioProperty());
        etiquetaFechTran.textProperty().bind(transaccion.fechaProperty().asString());
        mostrarIconos();
    }
    private void mostrarIconos(){
        System.out.println(transaccion.origenProperty().getValue());
        if((transaccion.origenProperty().getValue()) == (obtenerIdUsuarioPorMovil(Model.getInstance().getCliente().movilProperty().getValue()))){
            in_icon.setFill(Color.rgb(240, 240, 240));
            out_icon.setFill(Color.RED);
        } else {
            in_icon.setFill(Color.GREEN);
            out_icon.setFill(Color.rgb(240, 240, 240));
        }
    }
    private int obtenerIdUsuarioPorMovil(String movil) {
        return Model.getInstance().getDatabaseDriver().obtenerIdUsuarioPorMovil(movil);
    }
}
