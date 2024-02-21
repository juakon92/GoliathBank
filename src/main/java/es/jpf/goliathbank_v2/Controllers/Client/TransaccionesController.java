package es.jpf.goliathbank_v2.Controllers.Client;

import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Models.Transaccion;
import es.jpf.goliathbank_v2.Views.TransaccionCeldaFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransaccionesController implements Initializable {
    public ListView<Transaccion> listadoTransacciones;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarListadoTransacciones();
        listadoTransacciones.setItems(Model.getInstance().getTodasTransacciones());
        listadoTransacciones.setCellFactory(e -> new TransaccionCeldaFactory());
    }

    private void cargarListadoTransacciones(){
        if(Model.getInstance().getTodasTransacciones().isEmpty()){
            Model.getInstance().setTodasTransacciones();
        }
    }
}
