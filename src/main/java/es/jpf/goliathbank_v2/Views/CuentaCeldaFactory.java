package es.jpf.goliathbank_v2.Views;

import es.jpf.goliathbank_v2.Controllers.Client.CuentaCeldaController;
import es.jpf.goliathbank_v2.Controllers.Client.TransaccionCeldaController;
import es.jpf.goliathbank_v2.Models.Cuenta;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class CuentaCeldaFactory extends ListCell<Cuenta> {
    @Override
    protected void updateItem(Cuenta cuenta, boolean empty) {
        super.updateItem(cuenta, empty);
        if(empty) {
            setText(null);
            setGraphic(null);
        }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/CuentaCelda.fxml"));
            CuentaCeldaController controller = new CuentaCeldaController(cuenta);
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
