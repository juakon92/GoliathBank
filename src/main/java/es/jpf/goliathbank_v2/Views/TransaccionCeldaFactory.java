package es.jpf.goliathbank_v2.Views;

import es.jpf.goliathbank_v2.Controllers.Client.TransaccionCeldaController;
import es.jpf.goliathbank_v2.Models.Transaccion;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class TransaccionCeldaFactory extends ListCell<Transaccion> {
    @Override
    protected void updateItem(Transaccion transaccion, boolean empty) {
        super.updateItem(transaccion, empty);
        if(empty) {
            setText(null);
            setGraphic(null);
        }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/TransaccionCelda.fxml"));
            TransaccionCeldaController controller = new TransaccionCeldaController(transaccion);
            loader.setController(controller);
            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
